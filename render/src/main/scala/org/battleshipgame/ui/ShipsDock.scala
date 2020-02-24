package org.battleshipgame.ui

/**
 * Док кораблей, этакий мэнэджэр
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
trait ShipsDock {
    /**
     * Текущая ориентация корабликов (вынесена для удобства)
     */
    var orientation: ShipOrientation = ShipOrientation.HORIZONTAL    
    
    /**
     * Кораблик, который сейчас тащит юзер
     */
    var draggedShip: ShipSize = null
    
    /**
     * Оставшиеся (не расставленные) кораблики
     */
    def left(): Array[Ship]
    
    /**
     * Раставленные кораблики
     */
    def placed(): Array[Ship]
    
    /**
     * Если юзер взял кораблик
     */
    def onShipDrag(ship: ShipSize, orientation: ShipOrientation): Unit
    
    /**
     * Если юзер поставил кораблик
     */
    def onShipDrop(x: Int, y: Int): Unit
    
    /**
     * Если юзер ткнул в кораблик
     */
    def onShipClick(x: Int, y: Int): Unit
}