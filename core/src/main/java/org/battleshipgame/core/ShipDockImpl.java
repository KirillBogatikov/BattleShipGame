package org.battleshipgame.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.battleshipgame.render.Point;
import org.battleshipgame.render.Rectangle;
import org.battleshipgame.ui.Ship;
import org.battleshipgame.ui.ShipOrientation;
import org.battleshipgame.ui.ShipSize;
import org.battleshipgame.ui.ShipsDock;

public class ShipDockImpl extends ShipsDock {
	private Ship draggedShip;
	private List<Ship> placedShips;
	private List<Ship> leftShips;
	private Rectangle invalid;
	private ShipOrientation orientation;
	
	public ShipDockImpl() {
		placedShips = new ArrayList<>();
		placedShips.add(new Ship(ShipSize.WARSHIP, new Point(1, 1), ShipOrientation.VERTICAL));
		leftShips = new ArrayList<>();
		
		for(ShipSize size : ShipSize.values()) {
			for(int i = 0; i < 5 - size.toInt(); i++) {
				leftShips.add(new Ship(size));
			}
		}
		
		orientation = ShipOrientation.HORIZONTAL;
	}
	
	public void switchOrientation() {
		ShipOrientation orientation = null;
		
		switch(this.orientation) {
			case HORIZONTAL: 
				orientation = ShipOrientation.VERTICAL;
			break;
			case VERTICAL:
				orientation = ShipOrientation.HORIZONTAL;
			break;
		}
		
		this.orientation = orientation;
		for(Ship ship : leftShips) {
			ship.orientation_$eq(orientation);
		}
	}
	
	@Override
	public Ship[] left() {
		return leftShips.toArray(new Ship[0]);
	}

	@Override
	public Ship[] placed() {
		return placedShips.toArray(new Ship[0]);
	}
	
	@Override
	public Ship draggedShip() {
		return draggedShip;
	}
	
	public void dropShip() {
		draggedShip = null;
	}

	@Override
	public void onShipDrag(ShipSize size, ShipOrientation orientation) {
		draggedShip = new Ship(size, new Point(0, 0), orientation);
	}

	@Override
	public void onShipDrop(int x, int y) {
		Point point = new Point(x, y);
		Optional<Ship> o = placedShips.stream().filter(s -> s.area().contains(point)).findFirst();
		
		if(o.isPresent()) {
			invalid = o.get().area();
		} else {
			Ship dragged = draggedShip();
			placedShips.add(new Ship(dragged.size(), new Point(x, y), dragged.orientation()));
			Optional<Ship> option = leftShips.stream().filter(ship -> ship.size().equals(dragged.size())).findFirst();
			leftShips.remove(option.get());
		}
		dropShip();
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
