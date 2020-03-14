package org.battleshipgame.player

import org.battleshipgame.ui.{ Bay, ShotListener }
import org.battleshipgame.core.{ ShotResultListener, GameResultListener }
import org.battleshipgame.render.{ Point, Rectangle }
import java.util.concurrent.{ Executor, Executors }

abstract class Player(var friend: Player) {
    var executor: Executor = Executors.newSingleThreadExecutor()
    var listener: ShotResultListener = null
    val bay: Bay = new Bay()
    
    def shotListener(): ShotListener
    def resultListener(): ShotResultListener
    
    def hasWholeShips(): Boolean = {
        bay.ships foreach(ship => { 
            if (ship.totalDamage < ship.size.toInt) {
                return true 
            }
        })
        return false
    }
    
    protected def occupied(x: Int, y: Int): Boolean = {
        val point = Point(x, y)
        
        bay.misses.contains(point) || bay.wrecks.contains(point)
    }
    
    protected def addWreck(point: Point, flame: Boolean): Unit = {
        bay wreck(point)
        
        if (flame) bay flame(point)
    }
    
    protected def shipAreaMisses(area: Rectangle): Unit = {
        println("Ship Area: " + area.x + ", " + area.y + " -> " + area.width + ", " + area.height)
        for (xo <- 0 until area.width) {
            var top = Point(area.x + xo, area.y)
            var bottom = Point(area.x + xo, area.y + area.height - 1)
            
            println(top)
            println(bottom)
            
            if (top.x >= 0 && top.x < 10) {
                if (!bay.misses.contains(top) && top.y >= 0) bay miss(top)
                if (!bay.misses.contains(bottom) && bottom.y < 10) bay miss(bottom)
            }
        }
        
        for (yo <- 0 until area.height - 1) {
            var left = Point(area.x, area.y + yo)
            var right = Point(area.x + area.width - 1, area.y + yo)
            
            if (left.y >= 0 && left.y < 10) {
                if (!bay.misses.contains(left) && left.x >= 0) bay miss(left)
                if (!bay.misses.contains(right) && right.x < 10) bay miss(right)
            }
        }
    }
}