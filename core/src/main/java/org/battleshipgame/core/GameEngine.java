package org.battleshipgame.core;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.battleshipgame.network.ShotResult;
import org.battleshipgame.render.Point;
import org.battleshipgame.ui.Bay;	
import org.battleshipgame.ui.Ship;

import scala.util.Random;

public class GameEngine {
	private Player friend;
	private Bay playerBay;
	private Bay friendBay;
	private Point lastShot;
	private Random random;
	
	public GameEngine() {
		random = new Random();
	}
	
	public void setPlayerBay(Bay playerBay) {
		this.playerBay = playerBay;
	}
	
	public void setFriend(Player friend, Bay friendBay) {
		this.friend = friend;
		this.friendBay = friendBay;
	}

	public void onShot(Point point) {
		friendBay.locked_$eq(true);
		
		ShotResult result;
		Stream<Ship> ships = Arrays.stream(playerBay.ships());
		
		Optional<Ship> option = ships.filter(ship -> ship.rect().contains(point)).findFirst();
		
		if(option.isPresent()) {
			Ship ship = option.get();
		
			playerBay.wreck(point);
			
			if(random.nextBoolean()) {
				playerBay.flame(point);
			}
			
			if(ship.damage(point) == ship.size().toInt()) {
				result = ShotResult.KILL;
			} else {
				result = ShotResult.HURT;
			}
		} else {
			playerBay.miss(point);
			result = ShotResult.MISS;
		}
		friend.lastShot(result);
	}

	public void onShotResult(ShotResult result) {
		switch(result) {
			case MISS: 
				friendBay.miss(lastShot);
				friendBay.locked_$eq(true);
			break;
			case HURT: case KILL:
				friendBay.locked_$eq(false);
				friendBay.wreck(lastShot);
				
				if(random.nextBoolean()) {
					friendBay.flame(lastShot);
				}
			break;
			default:
		}
	}
}