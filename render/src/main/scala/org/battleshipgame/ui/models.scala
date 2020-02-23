package org.battleshipgame.ui

import org.battleshipgame.render.Point

case class Ship(val size: ShipSize, val point: Point, val orientation: ShipOrientation) { }

trait BattleField {
    def count(ship: ShipSize): Int
    def ships(ship: ShipSize): Array[Ship]
    def onShipDrag(ship: ShipSize, orientation: ShipOrientation): Unit
    def onShipDrop(x: Int, y: Int): Unit
    def onShipClick(x: Int, y: Int): Unit
    def draggedShip(): ShipSize
    def invalidate(): Unit
}