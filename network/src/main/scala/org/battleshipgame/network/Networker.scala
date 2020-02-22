package org.battleshipgame.network

trait Networker {
    def receive(hash: String): Packet
    def send(packet: Packet): Unit
    def useGameHost(gameId: GameId): Unit
    def gameId(hash: String): GameId
}