package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.Screen
import org.battleshipgame.render.View
import org.battleshipgame.render.Renderer
import org.battleshipgame.render.Rectangle
import org.battleshipgame.render.InputListener
import org.battleshipgame.render.Point

object GameModeScreen {
    protected var instance: GameModeScreen = null
    
    def impl(): GameModeScreen = {
        return instance;
    }
}

abstract class GameModeScreen extends Screen with InputListener {
    GameModeScreen.instance = this
    
    def singleGame(): View
    def multiPlayer(): View
    
    override def render(renderer: Renderer): Unit = {
        renderer begin()
        
        background(renderer)
        button(renderer, singleGame)
        button(renderer, multiPlayer)
        
        renderer end()
    }
    
    override def update(renderer: Renderer): Unit = {
        //TODO: hover button
    }
    
    override def onMouseMove(x: Int, y: Int): Unit = {
        //TODO: hover button
    }
    
    override def onClick(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        
        if (singleGame.rectangle contains(point)) {
            singleGame onClick()
        } else if (multiPlayer.rectangle contains(point)) {
            multiPlayer onClick();   
        }
    }
}