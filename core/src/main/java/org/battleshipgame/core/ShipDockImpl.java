package org.battleshipgame.core;

import java.util.List;
import java.util.Optional;

import org.battleshipgame.render.Point;
import org.battleshipgame.render.Rectangle;
import org.battleshipgame.ui.Ship;
import org.battleshipgame.ui.ShipOrientation;
import org.battleshipgame.ui.ShipSize;
import org.battleshipgame.ui.ShipsDock;

public class ShipDockImpl extends ShipsDock {
	private List<Ship> placedShips;
	private List<Ship> leftShips;
	private Rectangle invalid;
	
	@Override
	public Ship[] left() {
		return leftShips.toArray(new Ship[0]);
	}

	@Override
	public Ship[] placed() {
		return placedShips.toArray(new Ship[0]);
	}

	@Override
	public void onShipDrag(ShipSize ship, ShipOrientation orientation) {
		
	}

	@Override
	public void onShipDrop(int x, int y) {
		Point point = new Point(x, y);
		Optional<Ship> o = placedShips.stream().filter(s -> s.area().contains(point)).findFirst();
		
		if(o.isPresent()) {
			invalid = o.get().area();
		} else {
			placedShips.add(new Ship(draggedShip(), point, orientation()));
		}
	}

	@Override
	public void onShipClick(int x, int y) {
		Point point = new Point(x, y);
		Optional<Ship> o = placedShips.stream().filter(s -> s.rect().contains(point)).findFirst();
		
		if(o.isPresent()) {
			Ship ship = o.get();
			placedShips.remove(ship);
			leftShips.add(ship);
		}
	}

	@Override
	public Rectangle invalid() {
		return invalid;
	}

}
