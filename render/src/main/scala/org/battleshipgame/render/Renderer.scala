package org.battleshipgame.render

import scala.language.postfixOps

trait Renderer {
    def begin(): Unit
    def end(): Unit
    
    def stroke(paint: Boolean): Unit
    def fill(paint: Boolean): Unit

    def stroke(color: Long): Unit
    def fill(color: Long): Unit

    def polygon(vertices: Array[Point]): Unit
    def rectangle(rectangle: Rectangle): Unit
    def rectangle(vertex: Point, size: Size): Rectangle = {
        val rect = new Rectangle(vertex x, vertex y, size width, size height)
        rectangle(rect)
        return rect
    }
    def arc(start: Point, corner: Point, end: Point): Unit
    def line(start: Point, end: Point): Unit

    def image(rectangle: Rectangle, image: Image): Unit

    def text(rectangle: Rectangle, text: String, size: Double): Unit
}