package org.battleshipgame.ui

import org.battleshipgame.render.{Point, Rectangle}
import org.battleshipgame.ui.ShipOrientation.HORIZONTAL

/**
 * Кораблик:
 * <ul>
 * 	<li>размер</li>
 * 	<li>самая левая/верхняя точка</li>
 * 	<li>ориентация</li>
 * </ul> 
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
case class Ship(val size: ShipSize, val point: Point, val orientation: ShipOrientation) { 
    def area(): Rectangle = {
        var w = 0
        var h = 0
        
        if (orientation == HORIZONTAL) {
            w = size.toInt()
            h = 1
        } else {
            h = size.toInt()
            w = 1    
        }        
        
        return new Rectangle(point.x - 1, point.y - 1, w + 2, h + 2)
    }
    
    def rect(): Rectangle = {
        var w = 0
        var h = 0
        
        if (orientation == HORIZONTAL) {
            w = size.toInt()
            h = 1
        } else {
            h = size.toInt()
            w = 1    
        }        
        
        return new Rectangle(point.x, point.y, w, h)
    }
}
