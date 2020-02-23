package org.battleshipgame.ui;

public enum ShipOrientation {
	VERTICAL   (2),
	HORIZONTAL (1);
	
	private final int id;
	
	private ShipOrientation(int id) {
		this.id = id;
	}
	
	public int toInt() {
		return id;
	}
	
	public static ShipOrientation of(int size) {
		switch(size) {
			case 2: return VERTICAL;
			case 1: return HORIZONTAL;
			default: return null;
		}
	}
}
