package org.battleshipgame.network

import org.cuba.log.Log

abstract class Networker {
    def logger(log: Log): Unit
    def receive(hash: String): Packet
    def send(packet: Packet): Unit
    def host(gameId: GameId): Unit
    def gameId(hash: String): GameId
}