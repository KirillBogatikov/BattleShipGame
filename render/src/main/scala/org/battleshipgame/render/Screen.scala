package org.battleshipgame.render

import scala.language.postfixOps

import org.battleshipgame.render.ButtonState.{DEFAULT, HOVERED, PRESSED}
import org.battleshipgame.render.ColorUtils.alpha

trait Screen extends InputListener {
    def renderer(): Renderer
    def styles(): StylesResolver
    def size(): Size
    def strokeSize(): Int
    
    def buttons(): Array[Button] = Array()
    def images(): Array[ImageView] = Array()
    def labels(): Array[TextView] = Array()
    def inputs(): Array[TextView] = Array()
    
    def render(): Unit = {
        renderer begin()
        
        background()
        buttons foreach(button)
        images foreach(view => renderer image(view rectangle, view image))
        labels foreach(label)
        inputs foreach(input)
        
        renderer end()
    }
        
    protected def button(button: Button): Unit = {
        renderer fill(true)
        renderer stroke(false)
        
        val color = button state match {
            case HOVERED => styles buttonHovered
            case PRESSED => styles buttonPressed
            case DEFAULT => styles buttonDefault
        }
        
        renderer fill(color)
        renderer text(styles textColor)
        
        renderer rectangle(button rectangle)
        renderer text(button rectangle, button text, button textSize)
    }
    
    protected def input(input: TextView): Unit = {
        renderer fill(true)
        renderer stroke(true)
        renderer stroke(strokeSize)
        
        val bg = alpha(styles inputBackground, 64) //25%
        renderer fill(bg)
        renderer stroke(styles linesColor)
        renderer text(styles textColor)
        
        renderer rectangle(input rectangle)
        renderer text(input rectangle, input text, input textSize)
    }
    
    protected def grid(grid: MapGridView): Unit = {
        val rect = grid rectangle
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
        
    protected def label(view: TextView): Unit = {
        renderer text(styles textColor)
        renderer text(view rectangle, view text, view textSize)
    }
    
    protected def background(): Unit = {
        val rect = new Rectangle(0, 0, size width, size height)
        renderer image(rect, styles background)
    }
    
    override def onKeyPress(key: Int): Unit = {}
    
    override def onMouseDown(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        val option = buttons find(view => view.rectangle contains(point))
        
        if (!option.isEmpty) {
            option.get state = PRESSED
        }
    }
    
    override def onMouseMove(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        val prevent = buttons find(view => view.state == HOVERED)
        val current = buttons find(view => view.rectangle contains(point)) 
                
        if (!prevent.isEmpty) {
            prevent.get state = DEFAULT
        }
        if (!current.isEmpty) {
            current.get state = HOVERED
        }
    }
    
    override def onMouseUp(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        val option = buttons find(view => view.state == PRESSED)
        
        if (!option.isEmpty) {
            val btn = option.get
            if (btn.rectangle contains(point))
                btn state = HOVERED
            else
                btn state = DEFAULT
                
            btn onClick()
        }
    }
    
    override def onClick(x: Int, y: Int): Unit = {
        val point = new Point(x, y)
        val option = buttons find(view => view.rectangle contains(point))
                
        if (!option.isEmpty) {
            option.get onClick()
        }
    }
}