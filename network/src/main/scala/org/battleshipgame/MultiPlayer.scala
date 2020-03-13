package org.battleshipgame

import java.util.concurrent.{ Executor, Executors}
import java.util.concurrent.atomic.AtomicBoolean

import scala.language.postfixOps

import org.battleshipgame.network.{ EventListener, Networker, Packet, PacketProcessor }
import org.battleshipgame.network.Auth.HOW_ARE_YOU
import org.cuba.log.Log
import org.battleshipgame.network.GameId

class MultiPlayer(
    private val log: Log,
    private val networker: Networker) {
    private val TAG = this getClass() getSimpleName()

    private var hash: String = null
    private var alive = new AtomicBoolean(true)
    private var pool: Executor = _
    var listener: EventListener = _

    def start(): Unit = {
        this hash = HashGenerator next()
        log d (TAG, "Generated hash: " + hash)
        
        pool = Executors newFixedThreadPool(2)
        log d (TAG, "Threads pooled")
        
        pool execute(() => {
            while (alive get()) {
                val time = System currentTimeMillis()
                log d(TAG, "Current time: " + time)
                log d(TAG, "Waiting Packet...")

                var packet = networker receive(hash);

                log d(TAG, "Packet received after " + (System.currentTimeMillis() - time) + "ms. Processing Packet")

                val processor = new PacketProcessor(log, networker, listener)
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

    def createId(): GameId = {
        val gameId = networker gameId(hash)
        log d(TAG, "GameId:\nhash: " + hash + ", connection: " + gameId.connection)
        return gameId
    }

    def connect(gameId: GameId): Unit = {
        log d(TAG, "Try to connect with GameId: " + gameId)
        pool execute(() => {
            log d(TAG, "Parsed GameId:\nhash: " + gameId.hash + ", connection: " + gameId.connection)
            networker host(gameId)

            val packet = new Packet(hash, HOW_ARE_YOU, null)
            log d(TAG, "Sending \"" + HOW_ARE_YOU + "\"")
            networker send(packet)
        })
    }
    
    def send(group: String, value: String): Unit = {
        log d(TAG, "Sending \"" + group + "/" + value + "\"")
        pool execute(() => {
            val packet = new Packet(hash, group, value)
            networker send(packet)
        })
    }

    def stop(): Unit = {
        log d(TAG, "Stopping server...")
        alive set(false)
    }
}