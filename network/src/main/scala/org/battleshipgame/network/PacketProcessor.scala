package org.battleshipgame.network

import scala.language.postfixOps

import org.battleshipgame.network.Auth.HOW_ARE_YOU
import org.battleshipgame.network.Auth.I_M_FINE
import org.battleshipgame.network.EndGame.LOSE
import org.battleshipgame.network.EndGame.WIN
import org.battleshipgame.network.Shot.SHOT
import org.battleshipgame.network.ShotParser.parse
import org.cuba.log.Log

class PacketProcessor(private val log: Log, 
                     private val networker: Networker, 
                     private val listener: EventListener) {
    private val TAG = this getClass() getSimpleName()
        
    def process(packet: Packet) :Unit = {
        if (packet == null) {
            log i(TAG, "Received invalid packet")
        } else {
            packet group match {
                case HOW_ARE_YOU => {
                    listener.onFriendConnected()
                    
                    var responsePacket = new Packet(packet.hash, I_M_FINE, null)
                    networker send(responsePacket)
                }
                case I_M_FINE => {
                    listener.onConnectedToFriend()
                }
                case WIN   => listener.onGameEnd(true)
                case LOSE  => listener.onGameEnd(false)
                case SHOT  => {
                    val result = listener.onShot(parse(packet value))
                    var responsePacket = new Packet(packet.hash, result, null)
                    networker send(responsePacket)
                }
            }                    
        }
    }
}