package org.battleshipgame.core

import scala.language.postfixOps
import scala.util.Random
import org.battleshipgame.core.ShotResult.{ HURT, KILL, MISS }
import org.battleshipgame.core.ShotParser.{ stringify => shotStr }
import org.battleshipgame.core.AreaParser.{ stringify => areaStr }
import org.battleshipgame.render.Point
import org.battleshipgame.ui.Ship
import org.battleshipgame.ui.ShipSize._
import org.battleshipgame.ui.ShipOrientation._
import org.battleshipgame.ui.Bay
import java.util.Arrays

object PacketGroup extends Enumeration {
    type PacketGroup = Value
    val SHOT_POINT = Value("shot point")
    val SHOT_RESULT = Value("shot result")
    val GAME_RESULT = Value("game result")
}

import PacketGroup.{ SHOT_POINT, SHOT_RESULT }
import org.battleshipgame.ui.Ship
import org.battleshipgame.MultiPlayer
import org.battleshipgame.render.Rectangle

class Player(val playerBay: Bay, val friendBay: Bay) {
    var friend: Player = _
    private val random = new Random()
    private var lastShotPoint: Point = _
    
    def processShot(point: Point): ShotResult = {
        var result: ShotResult = null
                
        val option = playerBay.ships find(ship => ship.rect.contains(point))
        var rect: Rectangle = null
        
        if (option isDefined) {
            val ship = option get
                        
            ship.damage(point)
            wreck(playerBay, point)
            
            if (ship.totalDamage == ship.size.toInt) {
                result = KILL
                rect = ship area
            } else {
                result = HURT
            }
        } else {
            result = MISS
        }
        
        friend lastShot(result, rect)
        return result
    }
    
    def nextShot(point: Point): Unit = {
        lastShotPoint = point
    }
    
    protected def wreck(bay: Bay, point: Point): Unit = {
        if (random.nextDouble() < 0.25) {
	        bay flame(point)
	    }
	    
	    bay wreck(point)
    }
	
	def lastShot(result: ShotResult, area: Rectangle): Unit = {
	    if (result == HURT || result == KILL) {
    	    wreck(friendBay, lastShotPoint)
    	    friendBay locked = false
    	    //wait next shot
	    } else {
	        friendBay miss(lastShotPoint)   
	    }
	    
	    if(result == KILL) {
	        for(x <- 0 until area.width) {
	            if (area.x + x >= 0 && area.x + x < 10 && 
                    area.y >= 0) {
	                friendBay miss(new Point(area.x + x, area y))   
	            }
	            if (area.x + x >= 0 && area.x + x < 10 && 
                    area.y + area.height - 1 >= 0) {
	                friendBay miss(new Point(area.x + x, area.y + area.height - 1))
	            }
	        }
	        
	        for(y <- 0 until area.height) {
	            if (area.x >= 0 && 
                    area.y + y >= 0 && area.y + y < 9) {
	                friendBay miss(new Point(area x, area.y + y))
	            }
	            if (area.x + area.width - 1 >= 0 &&
                    area.y + y >= 0 && area.y + y < 9) {
	                friendBay miss(new Point(area.x + area.width - 1, area.y + y))
	            }
	        }
	    }
	}
}

class RemotePlayer(playerBay: Bay, friendBay: Bay, val multiplayer: MultiPlayer) extends 
    Player(playerBay, friendBay) {
    
    override def processShot(point: Point): ShotResult = {
        multiplayer send(SHOT_POINT toString, shotStr(point))
        return null
    }
    
    override def lastShot(result: ShotResult, area: Rectangle): Unit = {
        multiplayer send(SHOT_RESULT toString, result.toString + ";" + areaStr(area))
    }
}

class ComputerPlayer(playerBay: Bay, friendBay: Bay, val player: Player) extends Player(friendBay, playerBay) {
    friendBay.ships = Array(
        new Ship(TORPEDO, new Point(0, 0), VERTICAL),
        new Ship(TORPEDO, new Point(6, 2), VERTICAL),
        new Ship(TORPEDO, new Point(1, 8), HORIZONTAL),
        new Ship(TORPEDO, new Point(3, 9), HORIZONTAL),
        new Ship(DESTROYER, new Point(2, 0), HORIZONTAL),
        new Ship(DESTROYER, new Point(8, 8), VERTICAL),
        new Ship(DESTROYER, new Point(0, 5), VERTICAL),
        new Ship(CRUISER, new Point(2, 2), VERTICAL),
        new Ship(CRUISER, new Point(3, 7), HORIZONTAL),
        new Ship(WARSHIP, new Point(5, 4), HORIZONTAL)
    )
    friend = player
    
    private var x: Int = -2;
    private var y: Int = 0;
    
    private def next(): Unit = {
        x += 2
        if (x > 9) {
            y += 1
            x = (y % 2)
        }
    }
    
    override def processShot(point: Point): ShotResult = {
        println("AI")
        val result = super.processShot(point)
        if (result == MISS) {
            next()
            
            val point = new Point(x, y)
            nextShot(point)
            friend processShot(point)
        }
        return result
    }
        
    override def lastShot(result: ShotResult, area: Rectangle): Unit = {
        super.lastShot(result, area)
        
        
	    if (result == HURT || result == KILL) {
            next()
            println(x + ", " + y)
            
	        val point = new Point(x, y)
            nextShot(point)
	        player processShot(point)
	    }
	}
}