package org.battleshipgame.core

import org.battleshipgame.render.Rectangle

trait RenderListener {
	def render(): Unit
}

trait GameResultListener {
    def onGameWin(): Unit
    def onGameLose(): Unit
}

trait RemotePlayerListener extends GameResultListener {
    def onFriendConnected(): Unit
    def onConnectedToFriend(): Unit
}

trait ShotResultListener {
    def onShotResult(x: Int, y: Int, r: ShotResult, flame: Boolean = false, area: Rectangle = null): Unit
}