package org.battleshipgame.core

import scala.language.postfixOps
import org.battleshipgame.render.{ Point, Rectangle }

object ShotParser {
    val DELIMITER = ", "
    
    def parse(string: String): Point = {
        val parts = string.split(DELIMITER)
        return new Point(parts(0) toInt, parts(1) toInt)
    }
    
    def stringify(point: Point): String = {
        return point.x + DELIMITER + point.y
    }
}

object AreaParser {
    val DELIMITER = ", "
    
    def parse(string: String): Rectangle = {
        val parts = string.split(DELIMITER)
        return new Rectangle(parts(0) toInt, parts(1) toInt, parts(2) toInt, parts(3) toInt)
    }
    
    def stringify(area: Rectangle): String = {
        return area.x + DELIMITER + area.y + DELIMITER + area.width + DELIMITER + area.height
    }
}