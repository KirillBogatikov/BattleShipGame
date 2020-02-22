package org.battleshipgame.render

case class Size(var width: Int, var height: Int) {}

class Rectangle(var x: Int, var y: Int, 
                var width: Int, var height: Int, 
                var cornerRadius: Double = 0.0) {
    
    def contains(point: Point): Boolean = {
        return point.x >= x && point.y >= y &&
               point.x <= x + width && point.y <= y + height
    }
}

case class Point(var x: Int, var y: Int) {}

trait Image {
    def size(): Size
}