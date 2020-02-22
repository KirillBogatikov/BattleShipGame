package org.battleshipgame.network

import java.util.Base64

import scala.language.postfixOps

object ShotParser {
    private val DELIMITER = ","

    def parse(string: String): Shot = {
        var parts = string split(DELIMITER)

        if (parts.length != 2) return null

        return new Shot(parts(0) toInt, parts(1) toInt)
    }

    def stringify(shot: Shot): String = {
        var builder = new StringBuilder()
        builder append(shot.x) append(DELIMITER) append(shot.y)

        return builder toString
    }
}

object GameIdParser {
    private val DELIMITER = "|"

    def parse(string: String): GameId = {
        val bytes = Base64 getDecoder() decode(string getBytes())
        val decoded = new String(bytes)

        var parts = decoded split(DELIMITER)

        return new GameId(parts(0), parts(1))
    }

    def stringify(gameId: GameId): String = {
        val builder = new StringBuilder()
        builder append(gameId hash) append(DELIMITER) append(gameId connection)

        var string = builder toString
        val bytes = Base64 getEncoder() encode(string getBytes())

        return new String(bytes)
    }
}