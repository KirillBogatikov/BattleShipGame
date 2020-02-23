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

trait ImageView extends View {
    def image(): Image
}

trait BattleFieldView extends View {
    override def text(): String = ""
    override def text(text: String): Unit = {}
    override def textSize(): Double = 0.0
    override def textSize(size: Double): Unit = {}
    override def onClick(): Unit = {}
}