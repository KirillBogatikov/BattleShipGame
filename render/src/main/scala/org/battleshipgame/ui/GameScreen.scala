package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.{Screen, Point, Rectangle, GridView}
import org.battleshipgame.render.Size
import org.battleshipgame.render.Image
import scala.collection.mutable.Buffer

trait ShotListener {
    def onShot(x: Int, y: Int): Unit
}

class Bay(var listener: ShotListener = (x: Int, y: Int) => {},
          var ships: Array[Ship] = Array(),
          var wrecks: Array[Point] = Array(),
          var flames: Array[Point] = Array(),
          var misses: Array[Point] = Array(),
          var locked: Boolean = false) {
    
    def ship(index: Int): Unit = {
        var buf = ships.toBuffer
        buf.remove(index)
        ships = buf.toArray
    }
    
    def wreck(point: Point): Unit = {
        wrecks = wrecks:+ point
    }
    
    def flame(point: Point): Unit = {
        flames = flames:+ point
    }
    
    def miss(point: Point): Unit = {
        misses = misses:+ point
    }
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
        renderBay(userBay, userView)
        
        renderBay(opponentBay, opponentView)
        
        renderer end()
    }
    
    override def onClick(x: Int, y: Int): Boolean = {
        val result = super.onClick(x, y)
        
        if (userBay.locked || opponentBay.locked) {
            return result
        }
        
        if (!result) {
            val point = new Point(x, y)
            if (userView.bounds contains(point)) {
                val rel = userView toGridCoords(point)
                userBay.listener onShot(rel x, rel y)
            } else if (opponentView.bounds contains(point)) {
                val rel = opponentView toGridCoords(point)
                opponentBay.listener onShot(rel x, rel y)
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
        point.x += view.x
        point.y += view.y
        
        val size = view toPixelSize(new Size(1, 1))
        
        renderer image(new Rectangle(point, size), img)
    }
}