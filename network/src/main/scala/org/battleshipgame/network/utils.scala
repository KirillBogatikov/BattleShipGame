package org.battleshipgame.network

import java.util.Base64

import scala.language.postfixOps

object GameIdParser {
    private val DELIMITER = ";"

    def parse(string: String): GameId = {
        val bytes = Base64 getDecoder() decode(string getBytes())
        val decoded = new String(bytes)

        println(decoded)
        
        var parts = decoded split(DELIMITER)

        println(parts(0), parts(1))
        
        return new GameId(parts(0), parts(1))
    }

    def stringify(gameId: GameId): String = {
        val builder = new StringBuilder()
        builder append(gameId hash) append(DELIMITER) append(gameId connection)

        println(builder)
        
        var string = builder toString
        val bytes = Base64 getEncoder() encode(string getBytes())

        println(new String(bytes))
        
        return new String(bytes)
    }
}