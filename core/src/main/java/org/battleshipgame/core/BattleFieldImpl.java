package org.battleshipgame.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.battleshipgame.render.Point;
import org.battleshipgame.render.Rectangle;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Screen;
import org.battleshipgame.ui.BattleField;
import org.battleshipgame.ui.Ship;
import org.battleshipgame.ui.ShipOrientation;
import org.battleshipgame.ui.ShipSize;

public class BattleFieldImpl implements BattleField {
	private Renderer renderer;
	private Screen screen;
	private Map<ShipSize, Integer> availableShips;
	private List<Ship> placedShips;
	private ShipSize draggedShipSize;
	private ShipOrientation draggedShipOrientation;
	
	public BattleFieldImpl(Renderer renderer, Screen screen) {
		this.renderer = renderer;
		this.screen = screen;
		
		availableShips = new HashMap<>();
		availableShips.put(ShipSize.WARSHIP, 1);
		availableShips.put(ShipSize.CRUISER, 2);
		availableShips.put(ShipSize.DESTROYER, 3);
		availableShips.put(ShipSize.TORPEDO, 4);
		
		placedShips = new ArrayList<>();
	}
	
	@Override
	public int count(ShipSize size) {
		return availableShips.get(size);
	}

	@Override
	public Ship[] ships(ShipSize size) {
		return placedShips.stream().filter(s -> s.size() == size).toArray(Ship[]::new);
	}

	@Override
	public void onShipDrag(ShipSize size, ShipOrientation orientation) {
		draggedShipSize = size;
		draggedShipOrientation = orientation;
	}

	@Override
	public void onShipDrop(int x, int y) {
		int width = 0, height = 0;
		
		switch(draggedShipOrientation) {
			case VERTICAL: width = draggedShipSize.toInt(); height = 1; break;
			case HORIZONTAL: width = 1; height = draggedShipSize.toInt(); break;
		}
		
		Rectangle zone = new Rectangle(x - 1, y - 1, width + 2, height + 2, 0.0);
		long count = placedShips.stream().filter(s -> zone.contains(s.point())).count();
		
		if(count == 0) {
			placedShips.add(new Ship(draggedShipSize, new Point(x, y), draggedShipOrientation));
			
			draggedShipSize = null;
			draggedShipOrientation = null;
			
			int size = availableShips.get(draggedShipSize);
			availableShips.put(draggedShipSize, size - 1);
			screen.render(renderer);
		}
	}

	@Override
	public void onShipClick(int x, int y) {
		Ship ship = placedShips.stream().filter(s -> {
			Point point = s.point();
			return point.x() == x && point.y() == y;
		}).findFirst().get();
		if(ship != null) {
			placedShips.remove(ship);
		}
	}

	@Override
	public ShipSize draggedShip() {
		return draggedShipSize;
	}

	@Override
	public void invalidate() {
		screen.render(renderer);
	}

}
