package org.battleshipgame.render

trait InputListener {
    def onKeyPress(key: Int): Unit
    def onMouseMove(x: Int, y: Int): Unit
    def onMouseUp(x: Int, y: Int): Unit
    def onMouseDown(x: Int, y: Int): Unit
    def onClick(x: Int, y: Int): Unit
}