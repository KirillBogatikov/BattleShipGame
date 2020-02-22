package org.battleshipgame.render

import scala.language.postfixOps

trait Screen {
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
}