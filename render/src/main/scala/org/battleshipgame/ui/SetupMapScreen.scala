package org.battleshipgame.ui

import scala.language.postfixOps
import scala.util.control.Breaks

import org.battleshipgame.render.Button
import org.battleshipgame.render.MapGridView
import org.battleshipgame.render.Point
import org.battleshipgame.render.Rectangle
import org.battleshipgame.render.Screen
import org.battleshipgame.render.View

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
    def draggedShip(): Ship
    
    /**
     * Выкинь каку
     */
    def dropShip(): Unit
    
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
    def onShipDrag(ship: Ship): Unit
    
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
abstract class SetupMapScreen extends Screen {
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
    
    override def buttons(): Array[Button] = {
        var array: Array[Button] = Array()
        if(dock.left().length == 0)
            array = array :+ start
        
        return array
    }
            
    override def render(): Unit = {
        super.render()
        
        renderer begin()
        
        dock left() foreach(s => {
            val view = ship(s size, s orientation) 
            val img = styles ship(s size, s orientation)
            renderer image(view rectangle, img)
        })
                
        grid(grid)
        
        if (dock.invalid != null) {
            renderer fill(true)
            renderer stroke(true)
            renderer fill(styles highlightBackground)
            renderer stroke(styles highlightStroke)
            val point = grid toPixelCoords(dock.invalid point)
            point.x += grid.rectangle.x 
            point.y += grid.rectangle.y 
            val size = grid toPixelSize(dock.invalid size) 
            renderer rectangle(new Rectangle(point, size))
        }
        
        dock placed() foreach(renderShip)
        
        if (dock.draggedShip != null) {
            val s = dock.draggedShip
            val img = styles ship(s size, s orientation)
            val size = grid toPixelSize(s.rect size)
            val min = Math min(size width, size height)
            
            val point = new Point(s.point.x - min / 2, s.point.y - min / 2)
            
            renderer image(new Rectangle(point, size), img)
        }
            
        renderer end()
    }
    
    private def renderShip(s: Ship): Unit = {
        val img = styles ship(s size, s orientation)
                        
        val point = grid toPixelCoords(s.rect point)
        point.x += grid.rectangle.x 
        val size = grid toPixelSize(s.rect size)
        point.y += grid.rectangle.y 
        
        val rect = new Rectangle(point, size)
                       
        renderer image(rect, img)
    }
    
    override def onMouseMove(x: Int, y: Int): Boolean = {
        val point = new Point(x, y);
        val result = super.onMouseMove(x, y)
        val hasDragged = dock.draggedShip != null
        
        if (hasDragged) {
            dock.draggedShip.point = point   
        }
        
        return result || hasDragged
    }
    
    /**
     * Чекаем, может юзер начал тащить кораблик (drag'n'drop)
     */
    override def onMouseDown(x: Int, y: Int): Boolean = {
        var result = super.onMouseDown(x, y)
        val point = new Point(x, y)
        
        dock left() foreach(s => {
            val view = ship(s size, s orientation)
            if(view.rectangle contains(point)) {
                s.point = point
                dock onShipDrag(s)
                result = true
            }
        })
        
        return result
    }
        
    /**
     * Чекаем, может бросил кораблик (drag'n'drop)
     */
    override def onMouseUp(x: Int, y: Int): Boolean = {
        var result = super.onMouseDown(x, y)
        val point = new Point(x, y)
        
        if (dock.draggedShip != null) {
            if (grid.rectangle.contains(point)) {
                val gridPoint = grid toGridCoords(point)
                dock onShipDrop(gridPoint x, gridPoint y)
                return true
            } else {
                //нельзя бросать кораблики где попало
                dock dropShip()
                return true
            }
        }
        
        return result
    }
    
    override def onClick(x: Int, y: Int): Boolean = {
        if (super.onClick(x, y)) {
            return true;
        }
        
        val point = new Point(x, y)
        
        /*
         * в Scala нет break. Просто раз - и нет
         * но есть расширение Breaks (делает то же самое и даже чуточку больше) :D
         */
        var result: Boolean = false
        val mybreaks = new Breaks()
        import mybreaks.{ break, breakable }
        
        breakable {
            /*
             * Проверяем кораблики
             */
            val coords = grid toGridCoords(point)
            dock placed() foreach(s => {
                val rect = s rect
                
                if (rect contains(coords)) {
                    dock onShipClick(coords.x, coords.y)
                    result = true
                    break
                }
            })
        }
        
        return result
    }
}