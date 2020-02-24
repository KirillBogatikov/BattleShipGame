package org.battleshipgame.ui

trait ShipsDock {
    var orientation: ShipOrientation = ShipOrientation.HORIZONTAL    
    
    def left(): Array[Ship]
    def placed(): Array[Ship]
    
    def onShipDrag(ship: ShipSize, orientation: ShipOrientation): Unit
    def onShipDrop(x: Int, y: Int): Unit
    def onShipClick(x: Int, y: Int): Unit
    
    def draggedShip(): ShipSize
}