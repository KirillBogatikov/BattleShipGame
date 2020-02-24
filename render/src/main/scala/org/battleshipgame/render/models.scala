package org.battleshipgame.render

case class Point(var x: Int, var y: Int) {}

case class Size(var width: Int, var height: Int) {}

class Rectangle(var x: Int, var y: Int, 
                var width: Int, var height: Int, 
                var cornerRadius: Double) {
    
    def this(x: Int, y: Int, width: Int, height: Int) = 
        this(x, y, width, height, 0.0)
        
    def this(point: Point, size: Size, cornerRadius: Double) =
        this(point.x, point.y, size.width, size.height, cornerRadius)
        
    def this(point: Point, size: Size) =
        this(point, size, 0.0)

    def point(): Point = new Point(x, y)
    def size(): Size = new Size(width, height)
    
    def contains(point: Point): Boolean = {
        return point.x >= x && point.y >= y &&
               point.x <= x + width && point.y <= y + height
    }
}

trait Image {
    def size(): Size
}