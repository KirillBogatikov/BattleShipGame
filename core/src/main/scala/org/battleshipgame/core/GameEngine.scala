package org.battleshipgame.core

import scala.language.postfixOps
import java.util.Arrays
import org.battleshipgame.network.ShotResult
import org.battleshipgame.network.ShotResult._
import org.battleshipgame.ui.Bay
import scala.util.Random
import org.battleshipgame.render.Point

class GameEngine {
    private var friendPlayer: Player = _
	private var playerBay: Bay = _
	private var friendBay: Bay = _
	private var lastShot: Point = _
	private var random = new Random()
	
	def player(playerBay: Bay): Unit = {
		this playerBay = playerBay
	}
    
    def player(): Bay = playerBay
	
	def friend(friend: Player, friendBay: Bay): Unit = {
		this friendPlayer = friend
		this friendBay = friendBay
	}
    
    def friend(): Bay = friendBay
	
	def onShot(point: Point): Unit = {
		friendBay.locked = true
		
		var result: ShotResult = null;
		val option = playerBay.ships.find(ship => ship.rect().contains(point))
		
		if (option isDefined) {
			val ship = option get
		
			playerBay wreck(point)
			
			if (random nextBoolean) {
				playerBay flame(point)
			}
			
			if (ship.damage(point) == ship.size.toInt) {
				result = KILL
			} else {
				result = HURT
			}
		} else {
			playerBay miss(point)
			result = MISS
		}
		
		friendPlayer lastShot(result)
	}

	def onShotResult(result: ShotResult): Unit = {
		result match {
			case MISS => { 
				friendBay miss(lastShot);
				friendBay locked = true;
			}
			case HURT => case KILL => {
				friendBay locked = false;
				friendBay wreck(lastShot);
				
				if(random nextBoolean) {
					friendBay flame(lastShot);
				}
			}
			case _ =>
		}
	}
}