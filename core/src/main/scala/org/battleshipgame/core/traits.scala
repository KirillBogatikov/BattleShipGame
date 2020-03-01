package org.battleshipgame.core

import org.battleshipgame.network.ShotResult
import org.battleshipgame.render.Point

trait Player {
    def processShot(point: Point): ShotResult
	def nextShot(): Point
	def lastShot(result: ShotResult): Unit
}

trait RenderListener {
	def needRender(): Unit
}