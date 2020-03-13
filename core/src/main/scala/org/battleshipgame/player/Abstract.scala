package org.battleshipgame.player

import org.battleshipgame.ui.{ Bay, ShotListener }
import org.battleshipgame.core.ShotResultListener
import org.battleshipgame.render.{ Point, Rectangle }

abstract class Player(var friend: Player) {
    def bay(): Bay = new Bay()
    
    def shotListener(): ShotListener
    def resultListener(): ShotResultListener
    
    protected def empty(x: Int, y: Int): Boolean = {
        val point = Point(x, y)
        
        bay.misses.contains(point) || bay.wrecks.contains(point)
    }
    
    protected def addWreck(point: Point, flame: Boolean): Unit = {
        bay wreck(point)
        
        if (flame) bay flame(point)
    }
    
    protected def shipAreaMisses(area: Rectangle): Unit = {
        for (xo <- 0 until area.width) {
            var top = Point(area.x + xo, area.y)
            var bottom = Point(area.x + xo, area.y + area.height)
            
            if (top.x >= 0 && top.x < 10) {
                if (top.y >= 0) bay miss(top)
                if (bottom.y >= 0) bay miss(bottom)
            }
        }
        
        for (yo <- 0 until area.height) {
            var left = Point(area.x, area.y + yo)
            var right = Point(area.x + area.width, area.y + yo)
            
            if (left.y >= 0 && left.y < 10) {
                if (left.x >= 0) bay miss(left)
                if (right.x >= 0) bay miss(right)
            }
        }
    }
}