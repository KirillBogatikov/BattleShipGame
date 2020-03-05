package org.battleshipgame

import java.util.concurrent.{ Executor, Executors}
import java.util.concurrent.atomic.AtomicBoolean

import scala.language.postfixOps

import org.battleshipgame.network.{ EventListener, Networker, Packet, PacketProcessor }
import org.battleshipgame.network.Auth.HOW_ARE_YOU
import org.battleshipgame.network.GameIdParser.{ parse => gameParse, stringify => gameStringify }
import org.cuba.log.Log

class MultiPlayer(
    private val log: Log,
    private val networker: Networker,
    private val listener: EventListener) {
    private val TAG = this getClass() getSimpleName()

    private var hash: String = null
    private var alive = new AtomicBoolean(true)
    private var pool: Executor = _
    private val processor = new PacketProcessor(log, networker, listener)

    def start(): Unit = {
        this hash = HashGenerator next()
        log d (TAG, "Using hash: " + hash)
        pool = Executors newFixedThreadPool(2)
        log d (TAG, "Threads pooled")
        pool execute(() => {
            while (alive get()) {
                val time = System currentTimeMillis()
                log d(TAG, "Waiting Packet")

                var packet = networker receive(hash);

                log d(TAG, "Packet received after " + (System.currentTimeMillis() - time) + "ms. Processing Packet")

                processor process(packet);

                log d(TAG, "Packet processed. Try to sleep")

                try {
                    Thread sleep(17L)
                } catch {
                    case t: Throwable => log.e(TAG, "Thread.sleep failed", t)
                }
            }
        })
        log d(TAG, "Processor started in thread pool")
    }

    def createId(): String = {
        return gameStringify(networker gameId(hash))
    }

    def connect(gameId: String): Unit = {
        pool execute(() => {
            networker.useGameHost(gameParse(gameId))

            val packet = new Packet(hash, HOW_ARE_YOU, null)
            networker send(packet)
        })
    }
    
    def send(group: String, value: String): Unit = {
        pool execute(() => {
            val packet = new Packet(hash, group, value)
            networker send(packet)
        })
    }

    def stop(): Unit = {
        alive set(false)
    }
}