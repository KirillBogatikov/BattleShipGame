package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Point, Rectangle, Screen, ImageView, Button, MapGridView}
import org.battleshipgame.ui.ShipOrientation._
import scala.util.control.Breaks

trait SetupMapScreen extends Screen {
    def dock(): ShipsDock 
    def start(): Button
    def rotate(): ImageView
    def grid(): MapGridView 
    def ship(size: ShipSize, orientation: ShipOrientation): ImageView
    
    override def render(): Unit = {
        renderer begin()
        
        background()
        
        var canStart = true
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
        
        renderer end()
    }
    
    override def onMouseDown(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        dock left() foreach(s => {
            val view = ship(s size, s orientation)
            if(view.rectangle contains(point)) {
                dock onShipDrag(s size, s orientation)
            }
        })
    }
        
    override def onMouseUp(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        if (dock.draggedShip() != null && grid.rectangle.contains(point)) {
            val gridPoint = gridCoords(point)
            dock onShipDrop(gridPoint x, gridPoint y)
        }
    }
    
    def gridCoords(pixel: Point): Point = {
        val gridRect = grid rectangle
        
        val ox = gridRect.x - pixel.x;
        val oy = gridRect.y - pixel.y
        
        val i = ox / grid.cellSize
        var j = oy / grid.cellSize
        
        return new Point(i, j)
    }
    
    def realCoords(point: Point): Point = {
        val gridRect = grid rectangle
        
        val x = point.x * grid.cellSize
        var y = point.y * grid.cellSize
        
        return new Point(x, y)
    }
    
    override def onClick(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        if (start.rectangle contains(point)) {
            start onClick()
            return
        }
        
        if (rotate.rectangle contains(point)) {
            if (dock.orientation == HORIZONTAL) {
                dock orientation = VERTICAL
            } else {
                dock orientation = HORIZONTAL
            }
            return
        }
        
        val mybreaks = new Breaks()
        import mybreaks.{break, breakable}
        
        breakable {
            dock placed() foreach(s => {
                val view = ship(s size, s orientation)
                val realPoint = realCoords(s point)
                val rect = new Rectangle(realPoint, view.rectangle size)
                
                if (rect contains(point)) {
                    dock onShipClick(s.point.x, s.point.y)
                    break
                }
            })
        }
    }
}