package org.battleshipgame.network

case class Packet(val hash: String, val group: String, val value: String) {}

case class GameId(val hash: String, val connection: String) {}

object Shot {
    val SHOT = "shotpos"
    val HURT = "shothurt"
    val KILL = "shotkill"
    val MISS = "shotmiss"
}

case class Shot(val x: Int, val y: Int) {}

object Auth {
    val HOW_ARE_YOU = "helloman, how are you?"
    val I_M_FINE = "helloman, i'm fine"
}

object EndGame {
    val WIN = "winner"
    val LOSE = "loser"
}