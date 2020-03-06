package org.battleshipgame.core

trait RenderListener {
	def needRender(): Unit
}

trait CoreListener {
    def onConnected(client: Boolean): Unit
    def onGameEnd(win: Boolean): Unit
}