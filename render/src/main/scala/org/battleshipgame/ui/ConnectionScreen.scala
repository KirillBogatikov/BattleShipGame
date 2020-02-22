package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.InputListener
import org.battleshipgame.render.Point
import org.battleshipgame.render.Rectangle
import org.battleshipgame.render.Renderer
import org.battleshipgame.render.Screen
import org.battleshipgame.render.View

object ConnectionScreen {
    protected var instance: ConnectionScreen = null
    
    def impl(): ConnectionScreen = {
        return instance;
    }
}

abstract class ConnectionScreen extends Screen with InputListener {
    ConnectionScreen.instance = this
    
    def connect(): View
    def create(): View
    def gameId(): View
    
    private var gameIdCursorPosition: Int = 0
    
    override def render(renderer: Renderer): Unit = {
        renderer begin()
        
        renderer image(new Rectangle(0, 0, size width, size height), content background)
        
        button(renderer, connect)
        button(renderer, create)
        input(renderer, gameId)
        
        renderer end()
    }
    
    override def update(renderer: Renderer): Unit = {
        //TODO: hover button
    }
    
    override def onKeyPress(key: Int): Unit = {
        var text = gameId text
        
        if (key == 8) {
            if (gameIdCursorPosition > 0) {
                gameIdCursorPosition -= 1
                val left = text substring(0, gameIdCursorPosition) 
                val right = text substring(gameIdCursorPosition + 1)
                
                text = left + right
            }
        } else if (key == 46) {
            if (gameIdCursorPosition < text.length) {
                val left = text substring(0, gameIdCursorPosition)
                val right = text substring(gameIdCursorPosition + 1)
                gameIdCursorPosition -= 1
                
                text = left + right
            }
        } else {
            text = text + (key toChar)
        }
        
        gameId text(text)
    }
    
    override def onMouseMove(x: Int, y: Int): Unit = {
        //TODO: hover button
    }
    
    override def onClick(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        if (connect.rectangle contains(point)) {
            connect onClick()
        } else if (create.rectangle contains(point)) {
            create onClick();   
        }
    }
}