package org.battleshipgame.core

import org.battleshipgame.render.Rectangle

trait RenderListener {
	def render(): Unit
}

trait RemotePlayerListener {
    def onGameWin(): Unit
    def onGameLose(): Unit
    def onFriendConnected(): Unit
    def onConnectedToFriend(): Unit
}

trait ShotResultListener {
    def onShotResult(x: Int, y: Int, r: ShotResult, flame: Boolean = false, area: Rectangle = null): Unit
}