package org.battleshipgame.network.udp

import scala.language.postfixOps
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

import org.battleshipgame.network.Packet
import org.battleshipgame.network.Networker
import org.battleshipgame.network.udp.Crypto.decrypt
import org.battleshipgame.network.udp.Crypto.encrypt
import org.battleshipgame.network.udp.Parser.bytesOf
import org.battleshipgame.network.udp.Parser.parse
import org.battleshipgame.network.GameId
import org.cuba.log.Log

class UdpNetworker extends Networker {  
    private val TAG = this getClass() getSimpleName()
    private var log: Log = _
    private var serverSocket: DatagramSocket = _
    private var clientSocket: DatagramSocket = _
    
    def logger(log: Log): Unit = {
        this log = log
    }
    
    def this(serverPort: Int) = {
        this()
        log d(TAG, "UDP server port (requests) " + serverPort)
        serverSocket = new DatagramSocket()
        serverSocket connect(InetAddress.getLocalHost(), serverPort);
    }

    def receive(hash: String): Packet = {
        var bytes = new Array[Byte](4)
        var dp = new DatagramPacket(bytes, 0, 4)
        serverSocket receive(dp)

        val contentLength = (bytes(0) << 24) | (bytes(1) << 16) | (bytes(2) << 8) | bytes(3)

        bytes = new Array[Byte](contentLength)
        dp setData(bytes, 0, contentLength)
        serverSocket receive(dp)

        bytes = decrypt(bytes)
        val packet = parse(bytes)

        if (packet.hash != hash) {
            return null
        }

        return packet
    }

    def send(packet: Packet): Unit = {
        val content = bytesOf(packet)
        val contentLength = content length

        val size = new Array[Byte](4)
        size(0) = ((contentLength >> 24) & 0xFF).toByte
        size(1) = ((contentLength >> 16) & 0xFF).toByte
        size(2) = ((contentLength >> 8) & 0xFF).toByte
        size(3) = (contentLength & 0xFF).toByte

        var dp = new DatagramPacket(size, 0, 4)
        clientSocket send(dp)

        val bytes = encrypt(content)
        dp setData(bytes, 0, bytes length)
        clientSocket send(dp)
    }
    
    def host(gameId: GameId): Unit = {
        val parts = gameId.connection split(":")
        val ip = parts(0)
        val port = parts(1)
        
        log d(TAG, "UDP host:\n\tclient port (responses) " + port + "\n\tclient address " + ip)
        clientSocket = new DatagramSocket()
        clientSocket connect(InetAddress getByName(ip), port toInt);
    }

    def gameId(hash: String): GameId = {        
        val ip = serverSocket getInetAddress() getHostAddress()
        val port = serverSocket getPort()
        return new GameId(hash, ip + ":" + port)
    }
}