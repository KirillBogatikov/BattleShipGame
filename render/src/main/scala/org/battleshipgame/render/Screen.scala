package org.battleshipgame.render

import scala.language.postfixOps

import org.battleshipgame.render.ColorUtils._

trait Screen extends InputListener {
    def content(): ContentResolver
    def size(): Size
    def strokeSize(): Int
    def render(renderer: Renderer): Unit
    def update(renderer: Renderer): Unit
        
    protected def button(renderer: Renderer, button: View): Unit = {
        renderer fill(true)
        renderer stroke(false)
        
        renderer fill(content primaryColor)
        
        renderer rectangle(button rectangle)
        renderer text(button rectangle, button text, button textSize)
    }
    
    protected def input(renderer: Renderer, input: View): Unit = {
        renderer fill(true)
        renderer stroke(true)
        renderer stroke(strokeSize)
        
        val bg = alpha(content primaryColor, 64) //25%
        renderer fill(bg)
        renderer stroke(content secondaryColor)
        
        renderer rectangle(input rectangle)
        renderer text(input rectangle, input text, input textSize)
    }
    
    protected def battlefield(renderer: Renderer, field: BattleFieldView): Unit = {
        val rect = field rectangle
        val cellSize = (rect.width - strokeSize * 11) / 10 + strokeSize
        
        renderer fill(true)
        renderer stroke(true)
        
        renderer rectangle(rect)
        
        for(i <- 1 until 10) {
            var x = rect.x + i * cellSize
            renderer line(x, rect.y, x, rect.y + rect.height)
            
            var y = rect.y + i * cellSize
            renderer line(rect.x, y, rect.x + rect.height, y)
        }
    }
    
    protected def background(renderer: Renderer, clear: Boolean = false): Unit = {
        val rect = new Rectangle(0, 0, size width, size height)
        renderer image(rect, content background)
        
        if (!clear) {
            renderer fill(argb(64, 0, 0, 0))
            renderer rectangle(rect)
        }
    }
    
    override def onKeyPress(key: Int): Unit = {}
    
    override def onMouseDown(x: Int, y: Int): Unit = {}
    
    override def onMouseMove(x: Int, y: Int): Unit = {}
    
    override def onMouseUp(x: Int, y: Int): Unit = {}
    
    override def onClick(x: Int, y: Int): Unit = {}
}