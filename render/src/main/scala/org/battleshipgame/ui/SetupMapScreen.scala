package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Point, Rectangle, Screen, ImageView, TextView, Button, MapGridView, View}
import org.battleshipgame.ui.ShipOrientation._
import scala.util.control.Breaks
import org.battleshipgame.Ship

/**
 * Док кораблей, этакий мэнэджэр
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
abstract class ShipsDock {    
    /**
     * Кораблик, который сейчас тащит юзер
     */
    var draggedShip: Ship = null
    
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

/**
 * Экран расставления корабликов
 * 
 * @author Кирилл Испольнов
 * @version 1.3
 * @since 2.0.0
 */
trait SetupMapScreen extends Screen {
    /**
     * Платформа, дай док
     */
    def dock(): ShipsDock 
    
    /**
     * Дай кнопочку "Начать игру"
     */
    def start(): Button
        
    /**
     * Дай сетку поля
     */
    def grid(): MapGridView 
    
    /**
     * Дай кораблик по размеру и ориентации
     */
    def ship(size: ShipSize, orientation: ShipOrientation): View
            
    override def render(): Unit = {
        super.render()
        
        renderer begin()
        
        var canStart = dock.left().length == 0
        dock left() foreach(s => {
            val view = ship(s size, s orientation) 
            val img = styles ship(s size, s orientation)
            renderer image(view rectangle, img)
        })
        
        if (canStart) {
            button(start)
        }
        
        grid(grid)
        
        dock placed() foreach(s => {
            val img = styles ship(s size, s orientation)
                        
            val point = grid toPixelCoords(s.rect point)
            val size = grid toPixelSize(s.rect size)
            
            val rect = new Rectangle(point, size)
                           
            renderer image(rect, img)
        })
        
        if (dock.invalid != null) {
            renderer fill(true)
            renderer stroke(true)
            renderer fill(styles highlightBackground)
            renderer stroke(styles highlightStroke)
            renderer rectangle(dock invalid)
        }
            
        renderer end()
    }
    
    override def onMouseMove(x: Int, y: Int): Boolean = {
        val result = super.onMouseMove(x, y)
        val hasDragged = dock.draggedShip != null
        
        if (hasDragged) {
            dock.draggedShip.point = new Point(x, y)   
        }        
        
        return result || hasDragged
    }
    
    /**
     * Чекаем, может юзер начал тащить кораблик (drag'n'drop)
     */
    override def onMouseDown(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        var result = false
        
        dock left() foreach(s => {
            val view = ship(s size, s orientation)
            if(view.rectangle contains(point)) {
                s.point = view.rectangle.point
                dock draggedShip = s
                result = true
            }
        })
        
        return result
    }
        
    /**
     * Чекаем, может бросил кораблик (drag'n'drop)
     */
    override def onMouseUp(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        
        if (dock.draggedShip != null) {
            if (grid.rectangle.contains(point)) {
                val gridPoint = grid toGridCoords(point)
                dock onShipDrop(gridPoint x, gridPoint y)
                return true
            } else {
                //нельзя бросать кораблики где попало
                dock draggedShip = null
                return true
            }
        }
        
        return false
    }
    
    override def onClick(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        
        /*
         * Проверяем кнопку Начать игру
         */
        if (start.rectangle contains(point)) {
            start.listener onClick()
            return true
        }
        
        /*
         * в Scala нет break. Просто раз - и нет
         * но есть расширение Breaks (делает то же самое и даже чуточку больше) :D
         */
        var result: Boolean = false
        val mybreaks = new Breaks()
        import mybreaks.{break, breakable}
        
        breakable {
            /*
             * Проверяем кораблики
             */
            dock placed() foreach(s => {
                val rect = s rect
                
                if (rect contains(point)) {
                    dock onShipClick(s.point.x, s.point.y)
                    result = true
                    break
                }
            })
        }
        
        return result
    }
}