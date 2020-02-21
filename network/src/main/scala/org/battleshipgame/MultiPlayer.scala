package org.battleshipgame

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

import scala.language.postfixOps

import org.battleshipgame.network.EventListener
import org.battleshipgame.network.Networker
import org.battleshipgame.network.Packet
import org.battleshipgame.network.PacketProcessor
import org.battleshipgame.network.Auth.HOW_ARE_YOU
import org.battleshipgame.network.Shot
import org.battleshipgame.network.Shot.SHOT
import org.battleshipgame.network.ShotParser.{stringify => shotStringify}
import org.battleshipgame.network.GameIdParser.{parse => gameParse}
import org.battleshipgame.network.GameIdParser.{stringify => gameStringify}
import org.cuba.log.Log

class MultiPlayer(private val log :Log, 
                  private val networker :Networker, 
                  private val listener :EventListener) {
    private val TAG = this getClass() getSimpleName()
    
    private var hash :String = null
    private var alive = new AtomicBoolean(true)
    private var pool = Executors newFixedThreadPool(2)
    private val processor = new PacketProcessor(log, networker, listener)
       
    def start() :Unit = {
        this hash = HashGenerator.next()
        log d(TAG, "Using hash: " + hash)
        this hash = hash
        pool execute(() => {
            while(alive get()) { 
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
    
    def shot(shot: Shot) :Unit = {
        pool execute(() => {
            val packet = new Packet(hash, SHOT, shotStringify(shot))
            networker send(packet)
        })
    }
    
    def gameId() :String = {
        return gameStringify(networker gameId(hash))
    }
    
    def connect(gameIdString: String) :Unit = {
        pool execute(() => {
            networker.useGameHost(gameParse(gameIdString))
            
            val packet = new Packet(hash, HOW_ARE_YOU, null)
            networker send(packet)
        })
    }
    
    def stop() :Unit = {
        alive set(false)
    }
}