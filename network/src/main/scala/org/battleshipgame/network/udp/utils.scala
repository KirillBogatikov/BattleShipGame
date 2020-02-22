package org.battleshipgame.network.udp

import scala.language.postfixOps

import org.battleshipgame.network.Packet

import javax.crypto.Cipher
import javax.crypto.Cipher._
import javax.crypto.spec.SecretKeySpec

object Crypto {
    private val PASS = "HelloManLetsPlayBattleShipGameOnDifferentDevices"
    private val METHOD = "AES"
    private val KEY = new SecretKeySpec(PASS getBytes (), METHOD)

    def encrypt(content: Array[Byte]): Array[Byte] = {
        val cipher = Cipher getInstance(METHOD)
        cipher init(ENCRYPT_MODE, KEY)
        return cipher doFinal(content)
    }

    def decrypt(content: Array[Byte]): Array[Byte] = {
        val cipher = Cipher getInstance(METHOD)
        cipher init(DECRYPT_MODE, KEY)
        return cipher doFinal(content)
    }
}

object Parser {
    private val DELIMITER = "|"

    def parse(content: Array[Byte]): Packet = {
        val parts = new String(content) split(DELIMITER)

        if (parts.length < 3) return null

        return new Packet(parts(0), parts(1), parts(2))
    }

    def bytesOf(packet: Packet): Array[Byte] = {
        if (packet == null) return null

        val builder = new StringBuilder()

        builder append(packet hash) append(DELIMITER)
        builder append(packet group) append(DELIMITER)
        builder append(packet value)

        return builder toString() getBytes()
    }
}