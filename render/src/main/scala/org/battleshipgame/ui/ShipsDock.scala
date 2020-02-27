package org.battleshipgame.ui

import org.battleshipgame.render.Rectangle

/**
 * Док кораблей, этакий мэнэджэр
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
abstract class ShipsDock {
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
    
    /**
     * Дай зону ошибки
     * 
     * Не инвалид, а прямоугольник с ограниченными возможностями (с)
     */
    def invalid(): Rectangle
}