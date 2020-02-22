package org.battleshipgame.render

trait InputListener {
    def onKeyEnter(key: Char): Unit
    def onMouseMove(x: Int, y: Int): Unit
    def onClick(x: Int, y: Int): Unit
}