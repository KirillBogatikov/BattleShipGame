package org.battleshipgame.ui

import org.battleshipgame.render.Point

object Ship {
    val WARSHIP   = 4
    val CRUISER   = 3
    val DESTROYER = 2
    val TORPEDO   = 1
    val VERTICAL   = 2
    val HORIZONTAL = 1
}

case class Ship(val size: Int, val point: Point, val orientation: Int) { }

trait BattleField {
    def count(ship: Int): Int
    def ships(ship: Int): Array[Ship]
    def onShipDrag(ship: Int): Unit
    def onShipDrop(x: Int, y: Int): Unit
    def onShipClick(x: Int, y: Int): Unit
    def draggedShip(): Int
    def invalidate(): Unit
}