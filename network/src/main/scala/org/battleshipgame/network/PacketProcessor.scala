package org.battleshipgame.network

import scala.language.postfixOps

import org.battleshipgame.network.Auth.HOW_ARE_YOU
import org.battleshipgame.network.Auth.I_M_FINE
import org.cuba.log.Log

class PacketProcessor(
    private val log: Log,
    private val networker: Networker,
    private val listener: EventListener) {
    private val TAG = this getClass() getSimpleName()

    def process(packet: Packet): Unit = {
        if (packet == null) {
            log i(TAG, "Received invalid packet")
        } else {
            packet group match {
                case HOW_ARE_YOU => {
                    listener onFriendConnected()

                    var responsePacket = new Packet(packet hash, I_M_FINE, null)
                    networker send(responsePacket)
                }
                case I_M_FINE => {
                    listener onConnectedToFriend()
                }
                case _ => {
                    listener onPacketReceived(packet group, packet value)
                }
            }
        }
    }
}