package org.battleshipgame.ui

import scala.language.postfixOps

import org.battleshipgame.render.{Screen, TextView, Button}

trait ConnectionScreen extends Screen {
    def buttons(): Array[Button]
    def gameId(): TextView
    
    override def inputs(): Array[TextView] = Array(gameId)
    
    private var gameIdCursorPosition: Int = 0
        
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
}