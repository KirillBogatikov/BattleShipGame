package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.BattleFieldView
import org.battleshipgame.render.ImageView
import org.battleshipgame.render.Point
import org.battleshipgame.render.Rectangle
import org.battleshipgame.render.Renderer
import org.battleshipgame.render.Screen
import org.battleshipgame.render.View
import org.battleshipgame.ui.ShipOrientation._

object SetupMapScreen {
    protected var instance: SetupMapScreen = null
    
    def impl(): SetupMapScreen = {
        return instance;
    }
}

abstract class SetupMapScreen extends Screen {
    SetupMapScreen.instance = this
    
    protected var battlefield: BattleField = null
    protected var orientation: ShipOrientation = HORIZONTAL
    
    def init(battlefield: BattleField): Unit = {
        this battlefield = battlefield
    }
    
    def start(): View
    def rotate(): ImageView
    def field(): BattleFieldView 
    def forShip(size: ShipSize, orientation: ShipOrientation): ImageView
    
    override def render(renderer: Renderer): Unit = {
        renderer begin()
        
        background(renderer)
                
        var canStart = true
        ShipSize.values() foreach(size => {
            if (battlefield.count(size) > 0) {
                val view = forShip(size, orientation)
                renderer image(view rectangle, view image)
                canStart = false
            }
        })
        
        if (canStart) {
            button(renderer, start)
        }
        
        battlefield(renderer, field)
        
        ShipSize.values() foreach(size => {
            battlefield ships(size) foreach(ship => {
                val view = forShip(size, ship orientation)
                var fieldRect = field rectangle
                
                val ox = ship.point.x * (fieldRect.width / 10)
                var oy = ship.point.y * (fieldRect.height / 10)
                
                val rect = new Rectangle(fieldRect.x + ox, fieldRect.y + oy, view.rectangle width, view.rectangle height)
                               
                renderer image(rect, view image)
            })
        })
        
        renderer end()
    }
    
    override def update(renderer: Renderer): Unit = {
        render(renderer)
        //TODO: hover button
    }
    
    override def onMouseDown(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        ShipSize.values() foreach(size => {
            val ship = forShip(size, orientation)
            if(ship.rectangle contains(point)) {
                battlefield onShipDrag(size, orientation)
            }
        })
    }
    
    override def onMouseMove(x: Int, y: Int): Unit = {
        if (battlefield.draggedShip() != null) {
            battlefield invalidate()
        }
        //TODO: hover button
    }
    
    override def onMouseUp(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        val rect = field rectangle
        
        if (battlefield.draggedShip() != null && rect.contains(point)) {
            val ox = rect.x - x;
            val oy = rect.y - y
            val i = ox / (rect.width / 10)
            var j = oy / (rect.height / 10)
            
            battlefield onShipDrop(i, j)
        }
    }
    
    override def onClick(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        if (start.rectangle contains(point)) {
            start onClick()
        }
        
        if (rotate.rectangle contains(point)) {
            rotate onClick();
            
            if (orientation == HORIZONTAL) {
                orientation = VERTICAL
            } else {
                orientation = HORIZONTAL
            }
        }
    }
}