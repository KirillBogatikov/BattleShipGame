package org.battleshipgame.network

trait EventListener {
    def onFriendConnected(): Unit
    def onConnectedToFriend(): Unit
    def onShot(shot: Shot): String
    def onGameEnd(win: Boolean): Unit
    def onShotResult(result: String): Unit
}