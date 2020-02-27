package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Point, Rectangle, Screen, ImageView, TextView, Button, MapGridView}
import org.battleshipgame.ui.ShipOrientation._
import scala.util.control.Breaks

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
     * Дай кнопочку "Повернуть"
     */
    def rotateImage(): ImageView
    
    /**
     * Дай текст к кнопочке "Повернуть"
     */
    def rotateLabel(): TextView
    
    /**
     * Дай кнопочку "Назад"
     */
    def backImage(): ImageView
    
    /**
     * Дай текст к кнопочке "Назад"
     */
    def backLabel(): TextView
    
    /**
     * Дай сетку поля
     */
    def grid(): MapGridView 
    
    /**
     * Дай кораблик по размеру и ориентации
     */
    def ship(size: ShipSize, orientation: ShipOrientation): ImageView
            
    override def render(): Unit = {
        renderer begin()
        
        background()
        
        renderer image(rotateImage rectangle, rotateImage image)
        label(rotateLabel)
        
        var canStart = dock.left().length == 0
        dock left() foreach(s => {
            val view = ship(s size, s orientation) 
            renderer image(view rectangle, view image)
        })
        
        if (canStart) {
            button(start)
        }
        
        grid(grid)
        
        dock placed() foreach(s => {
            val view = ship(s size, s orientation)
            var gridRect = grid rectangle
                        
            val rect = new Rectangle(s point, view.rectangle size)
                           
            renderer image(rect, view image)
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
    
    /**
     * Чекаем, может юзер начал тащить кораблик (drag'n'drop)
     */
    override def onMouseDown(x: Int, y: Int): Boolean = {
        val point = new Point(x, y)
        var result = false
        
        dock left() foreach(s => {
            val view = ship(s size, s orientation)
            if(view.rectangle contains(point)) {
                dock draggedShip = s.size
                dock orientation = s.orientation
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
                val gridPoint = gridCoords(point)
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
    
    /**
     * Пиксельные координаты -> координаты сетки
     */
    private def gridCoords(pixel: Point): Point = {
        val gridRect = grid rectangle
        
        val ox = gridRect.x - pixel.x;
        val oy = gridRect.y - pixel.y
        
        val i = ox / grid.cellSize
        var j = oy / grid.cellSize
        
        return new Point(i, j)
    }
    
    /**
     * Координаты сетки -> пиксельные координаты
     */
    private def realCoords(point: Point): Point = {
        val gridRect = grid rectangle
        
        val x = point.x * grid.cellSize
        var y = point.y * grid.cellSize
        
        return new Point(x, y)
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
         * Проверяем кнопку Повернуть
         */
        if (rotateImage.rectangle.contains(point) || rotateLabel.rectangle.contains(point)) {
            if (dock.orientation == HORIZONTAL) {
                dock orientation = VERTICAL
            } else {
                dock orientation = HORIZONTAL
            }
            return true
        }
        
        /*
         * Проверяем кнопку Назад
         */
        if (backImage.rectangle.contains(point) || backLabel.rectangle.contains(point)) {
            backImage.listener onClick()
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