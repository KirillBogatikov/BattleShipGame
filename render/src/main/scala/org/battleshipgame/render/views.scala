package org.battleshipgame.render

trait View {
    def rectangle(): Rectangle
    def rectangle(rectangle: Rectangle): Unit
    def text(): String
    def text(text: String): Unit
    def textSize(): Double
    def textSize(size: Double): Unit
    
    def onClick(): Unit
}