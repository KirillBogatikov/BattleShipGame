package org.battleshipgame.core;

import java.util.List;
import java.util.Optional;

import org.battleshipgame.network.ShotResult;
import org.battleshipgame.render.Point;
import org.battleshipgame.ui.Ship;

public class GameEngine implements Player {
	private Player friend;
	private List<Ship> ships;
	private List<Point> flames;
	private List<Ship> killed; 
	
	public GameEngine() {
		
	}
	
	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}
	
	public void setFriend(Player player) {
		this.friend = player;
	}

	@Override
	public ShotResult processShot(Point point) {
		Optional<Ship> option = ships.stream().filter(ship -> ship.rect().contains(point)).findFirst();
		if(option.isPresent()) {
			Ship ship = option.get();
			if(ship.damage(point) == ship.size().toInt()) {
				ships.remove(ship);
				return ShotResult.KILL;
			} else {
				return ShotResult.HURT;
			}
		} else {
			return ShotResult.MISS;
		}
	}

	@Override
	public Point nextShot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lastShot(ShotResult result) {
		// TODO Auto-generated method stub
		
	}
}