package org.battleshipgame.render

trait View {
    def rectangle(): Rectangle
}

trait TextView extends View {
    def text(): String
    def text(text: String): Unit
    def textSize(): Double
}

trait Button extends TextView {
    var state: ButtonState = null
    
    def onClick(): Unit
}

trait ImageView extends View {
    def image(): Image
    
    def onClick(): Unit = {}
}

trait MapGridView extends View {
    def strokeSize(): Int
    def cellSize(): Int
    
    def onShot(i: Int, j: Int): Unit
}