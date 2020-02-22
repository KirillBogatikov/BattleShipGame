package org.battleshipgame.ui

import scala.language.postfixOps
import org.battleshipgame.render.Rectangle
import org.battleshipgame.render.Renderer
import org.battleshipgame.render.Screen
import org.battleshipgame.render.View
import org.battleshipgame.render.InputListener
import org.battleshipgame.render.Point

object StartScreen {
    protected var instance: StartScreen = null
    
    def impl(): StartScreen = {
        return instance;
    }
}

abstract class StartScreen extends Screen with InputListener {
    StartScreen.instance = this
    
    def buttons(): List[View]
    
    override def render(renderer: Renderer): Unit = {
        renderer begin()
        
        renderer image(new Rectangle(0, 0, size width, size height), content background)
        
        buttons foreach(view => button(renderer, view))
        
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
        val button = buttons find(view => view.rectangle contains(point)) get
        
        if (button != null) {
            button onClick()
        }
    }
}