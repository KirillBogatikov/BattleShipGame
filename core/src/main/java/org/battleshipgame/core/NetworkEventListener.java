package org.battleshipgame.core;

import java.util.List;
import java.util.Optional;

import org.battleshipgame.network.EventListener;
import org.battleshipgame.network.ShotResult;
import org.battleshipgame.render.Point;
import org.battleshipgame.ui.Ship;

public class NetworkEventListener implements EventListener {
	private List<Ship> ships;
	
    public void onFriendConnected() {
    	
    }
    
    public void onConnectedToFriend() {
    	
    }
    
    public ShotResult onShot(int x, int y) {
    	Point point = new Point(x, y);
    	Optional<Ship> option = ships.stream().filter(ship -> ship.rect().contains(point)).findFirst();
    	if(option.isPresent()) {
    		Ship ship = option.get();
    		if(ship.damage(point) == ship.size().toInt()) {
    			return ShotResult.KILL;
    		}
    		return ShotResult.HURT;
    	}
    	
    	return ShotResult.MISS;
    }
    
    public void onGameEnd(boolean win) {
    	
    }
    
    public void onShotResult(ShotResult result) {
    	
    }
}
