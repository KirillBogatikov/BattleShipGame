package org.battleshipgame.network

trait EventListener {
    def onFriendConnected(): Unit
    def onConnectedToFriend(): Unit
    def onShot(x: Int, y: Int): ShotResult
    def onGameEnd(win: Boolean): Unit
    def onShotResult(result: String): Unit
}