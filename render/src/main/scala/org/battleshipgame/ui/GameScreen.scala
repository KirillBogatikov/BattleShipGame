package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.{Screen, Point, Rectangle, GridView, Size, Image, ImageView, TextView }
import scala.collection.mutable.Buffer

trait ShotListener {
    def onShot(x: Int, y: Int): Unit
}

class Bay(var ships: Array[Ship] = Array(),
          var wrecks: Array[Point] = Array(),
          var flames: Array[Point] = Array(),
          var misses: Array[Point] = Array()) {
    
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
    def shotListener(): ShotListener
    def lockerImage(): ImageView
    def lockerText(): TextView
    
    override def render(): Unit = {
        super.render();
        
        println(if (lockerImage == null) "ready" else "locked")
        
        renderer begin()
        
        grid(userView)
        grid(opponentView)
        
        userBay.ships foreach(ship => renderShip(ship, userView))
        renderBay(userBay, userView)
        
        renderBay(opponentBay, opponentView)
        
        if (lockerImage != null) {
            renderer fill(true)
            renderer fill(styles lockerColor)
            renderer stroke(false)
            renderer rectangle(new Rectangle(Point(0, 0), size), 0.0)
            
            label(lockerText)
            
            renderer image(lockerImage bounds, lockerImage image)
        }
        
        renderer end()
    }
    
    override def onClick(x: Int, y: Int): Boolean = {
        val result = super.onClick(x, y)
        
        if (lockerImage != null) {
            return result
        }
        
        if (!result) {
            val point = new Point(x, y)
            if (opponentView.bounds contains(point)) {
                val rel = opponentView toGridCoords(point)
                shotListener onShot(rel x, rel y)
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