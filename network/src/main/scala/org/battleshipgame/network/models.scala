package org.battleshipgame.network

case class Packet(val hash: String, val group: String, val value: String) {}

case class GameId(val hash: String, val connection: String) {}

object Auth {
    val HOW_ARE_YOU = "helloman, how are you?"
    val I_M_FINE = "helloman, i'm fine"
}
