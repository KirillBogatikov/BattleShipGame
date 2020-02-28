package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.{Screen, Point, Rectangle, MapGridView}
import org.battleshipgame.render.Size
import org.battleshipgame.render.Image

trait Bay {
    def ships(): Array[Ship] = Array()
    def wrecks(): Array[Point] = Array()
    def flames(): Array[Point]
    def misses(): Array[Point]
    def onShot(x: Int, y: Int): Unit
}

abstract class GameScreen extends Screen {
    def userBay(): Bay
    def opponentBay(): Bay
    def userView(): MapGridView
    def opponentView(): MapGridView
    
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
        
        if (!result) {
            val point = new Point(x, y)
            if (userView.rectangle contains(point)) {
                userBay onShot(x, y)
            } else if (opponentView.rectangle contains(point)) {
                opponentBay onShot(x, y)
            }
        }
        
        return result
    }
    
    private def renderBay(bay: Bay, view: MapGridView): Unit = {
        bay.wrecks foreach(wreck => renderCell(styles wreck, wreck, view))
        bay.flames foreach(flame => renderCell(styles flame, flame, view))
        bay.misses foreach(miss => renderCell(styles miss, miss, view))
    }
    
    private def renderShip(ship: Ship, view: MapGridView): Unit = {
        val point = view toPixelCoords(ship point)
        val size = view toPixelSize(ship.rect() size)
        val img = styles ship(ship size, ship orientation)
        
        renderer image(new Rectangle(point, size), img)
    }
    
    private def renderCell(img: Image, wreck: Point, view: MapGridView): Unit = {
        val point = view toPixelCoords(wreck)
        val size = view toPixelSize(new Size(1, 1))
        
        renderer image(new Rectangle(point, size), img)
    }
}