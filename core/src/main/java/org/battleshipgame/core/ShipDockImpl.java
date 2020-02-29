package org.battleshipgame.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.battleshipgame.render.Point;
import org.battleshipgame.render.Rectangle;
import org.battleshipgame.render.Size;
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
	private RenderListener listener;
	
	public ShipDockImpl(RenderListener listener) {
		placedShips = new ArrayList<>();
		leftShips = new ArrayList<>();
		
		for(ShipSize size : ShipSize.values()) {
			for(int i = 0; i < 5 - size.toInt(); i++) {
				leftShips.add(new Ship(size));
			}
		}
		
		orientation = ShipOrientation.HORIZONTAL;
		this.listener = listener;
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
	public void onShipDrag(Ship ship) {
		draggedShip = ship;
	}

	@Override
	public void onShipDrop(int x, int y) {
		Size size = draggedShip.rect().size();
				
		if(x + size.width() > 10 || y + size.height() > 10) {
			invalid = new Rectangle(0, 0, 10, 10);
		} else {
			Optional<Ship> o = placedShips.stream().filter(s -> checkIntersects(s, size, x, y)).findFirst();
				
			if(o.isPresent()) {
				invalid = o.get().area();
			} else {
				Ship dragged = draggedShip();
				placedShips.add(new Ship(dragged.size(), new Point(x, y), dragged.orientation()));
				Optional<Ship> option = leftShips.stream().filter(ship -> ship.size().equals(dragged.size())).findFirst();
				leftShips.remove(option.get());
			}
		}
		dropShip();
	}
	
	private boolean checkIntersects(Ship ship, Size size, int x, int y) {
		Rectangle area = ship.area();
		for(int i = 0; i < size.width(); i++) {
			for(int j = 0; j < size.height(); j++) {
				if(area.contains(new Point(x  + i, y + j))) {
					return true;
				}
			}
		}
		return false;
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
		if(invalid != null) {
			Thread timeout = new Thread(() -> {
				try {
					Thread.sleep(3000L);
					listener.needRender();
				} catch (InterruptedException e) { }
				invalid = null;
			});
			timeout.start();
		}
		
		return invalid;
	}

}
