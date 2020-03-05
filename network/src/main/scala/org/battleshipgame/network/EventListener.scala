package org.battleshipgame.network

trait EventListener {
    def onFriendConnected(): Unit
    def onConnectedToFriend(): Unit
    def onPacketReceived(group: String, value: String): Unit
}