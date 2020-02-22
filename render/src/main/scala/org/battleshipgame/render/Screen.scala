package org.battleshipgame.render

import scala.language.postfixOps
import org.battleshipgame.render.ColorUtils._

trait Screen extends InputListener {
    def content(): ContentResolver
    def size(): Size
    def render(renderer: Renderer): Unit
    def update(renderer: Renderer): Unit
        
    protected def button(renderer: Renderer, button: View): Unit = {
        renderer fill(content primaryColor)
        renderer stroke(false)
        renderer rectangle(button rectangle)
        renderer text(button rectangle, button text, button textSize)
    }
    
    protected def input(renderer: Renderer, input: View): Unit = {
        val bg = alpha(content primaryColor, 64) //25%
        renderer fill(content primaryColor)
    }
    
    override def onKeyPress(key: Int): Unit = {}
    
    override def onMouseMove(x: Int, y: Int): Unit = {}
    
    override def onClick(x: Int, y: Int): Unit = {}
}