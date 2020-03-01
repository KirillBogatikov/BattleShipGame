package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.{Screen, Point, Rectangle, GridView}
import org.battleshipgame.render.Size
import org.battleshipgame.render.Image
import scala.collection.mutable.Buffer

abstract class Bay(var ships: Array[Ship] = Array(),
          var wrecks: Array[Point] = Array(),
          var flames: Array[Point],
          var misses: Array[Point],
          var locked: Boolean) {
    
    def ship(index: Int): Unit = {
        var buf = ships.toBuffer
        buf.remove(index)
        ships = buf.toArray
    }
    
    def wreck(point: Point): Unit = {
        wrecks:+ point
    }
    
    def flame(point: Point): Unit = {
        flames:+ point
    }
    
    def miss(point: Point): Unit = {
        misses:+ point
    }
    
    def onShot(x: Int, y: Int): Unit
}

abstract class GameScreen extends Screen {
    def userBay(): Bay
    def opponentBay(): Bay
    def userView(): GridView
    def opponentView(): GridView
    
    override def render(): Unit = {
        super.render();
        
        renderer begin()
        
        grid(userView)
        grid(opponentView)
        
        userBay.ships foreach(ship => renderShip(ship, userView))
        renderBay(userBay, opponentView)
        
        renderBay(opponentBay, opponentView)
        
        renderer end()
    }
    
    override def onClick(x: Int, y: Int): Boolean = {
        val result = super.onClick(x, y)
        
        if (opponentBay locked) {
            return result
        }
        
        if (!result) {
            val point = new Point(x, y)
            if (userView.bounds contains(point)) {
                val rel = userView toGridCoords(point)
                userBay onShot(rel x, rel y)
            } else if (opponentView.bounds contains(point)) {
                val rel = opponentView toGridCoords(point)
                opponentBay onShot(rel x, rel y)
            }
        }
        
        return result
    }
    
    private def renderBay(bay: Bay, view: GridView): Unit = {
        bay.wrecks foreach(wreck => renderCell(styles wreck, wreck, view))
        bay.flames foreach(flame => renderCell(styles flame, flame, view))
        bay.misses foreach(miss => renderCell(styles miss, miss, view))
    }
    
    private def renderShip(ship: Ship, view: GridView): Unit = {
        val point = view toPixelCoords(ship point)
        point.x += view.bounds.x
        point.y += view.bounds.y
        val size = view toPixelSize(ship.rect() size)
        val img = styles ship(ship size, ship orientation)
        
        renderer image(new Rectangle(point, size), img)
    }
    
    private def renderCell(img: Image, wreck: Point, view: GridView): Unit = {
        val point = view toPixelCoords(wreck)
        val size = view toPixelSize(new Size(1, 1))
        
        renderer image(new Rectangle(point, size), img)
    }
}