package org.battleshipgame.network.udp

import scala.language.postfixOps
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

import org.battleshipgame.network.Packet
import org.battleshipgame.network.Networker
import org.battleshipgame.network.udp.Crypto.decrypt
import org.battleshipgame.network.udp.Parser.bytesOf
import org.battleshipgame.network.udp.Parser.parse
import org.battleshipgame.network.GameId

class UdpNetworker(val port: Int) extends Networker {
    private var socket = new DatagramSocket(port)
    private var clientIp :InetAddress = null
    private var clientPort :Int = 0
        
    def receive(hash: String) :Packet = {
        var bytes = new Array[Byte](4)
        var dp = new DatagramPacket(bytes, 0, 4)
        socket receive(dp)
        
        val contentLength = (bytes(0) << 24) | (bytes(1) << 16) | (bytes(2) << 8) | bytes(3)
        
        bytes = new Array[Byte](contentLength)
        dp setData(bytes, 0, contentLength)
        socket receive(dp)
        
        bytes = decrypt(bytes)
        val packet = parse(bytes)
        
        if (packet != null && packet.hash == hash) {
            clientIp = dp getAddress()
            clientPort = dp getPort()
            
            return packet
        }
        
        return null
    }
    
    def send(packet: Packet) :Unit = {
        val content = bytesOf(packet)
        val contentLength = content.length
        
        val bytes = new Array[Byte](4)
        bytes(0) = ((contentLength >> 24) & 0xFF).toByte
        bytes(1) = ((contentLength >> 16) & 0xFF).toByte
        bytes(2) = ((contentLength >> 8) & 0xFF).toByte
        bytes(3) = (contentLength & 0xFF).toByte
        
        var dp = new DatagramPacket(bytes, 0, 4)
        dp setAddress(clientIp)
        dp setPort(clientPort)
        
        socket send(dp)
        
        dp setData(bytes, 0, contentLength)
        socket send(dp)
    }
    
    def gameId(hash: String) :GameId = {
        val ip = socket getInetAddress() getHostAddress()
        return new GameId(hash, ip + ":" + port)
    }
    
    def useGameHost(gameId: GameId) :Unit = {
        val parts = gameId.connection.split(":")
        clientIp = InetAddress.getByName(parts(0))
        clientPort = parts(1) toInt
    }
}