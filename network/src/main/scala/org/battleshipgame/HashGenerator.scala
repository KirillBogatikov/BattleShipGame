package org.battleshipgame

import scala.language.postfixOps
import scala.util.Random

object HashGenerator {
    private val ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val random = new Random

    def next(): String = {
        val builder = new StringBuilder
        for (i <- 0 until 6) {
            val j = random nextInt(ALPHABET length)
            builder append(ALPHABET(j))
        }

        return builder toString
    }
}