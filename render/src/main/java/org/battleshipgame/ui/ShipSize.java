package org.battleshipgame.ui;

/**
 * Размер корабля - Линкор, Крейсер, Эсминец, Торпедный катер
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
public enum ShipSize {
	WARSHIP   (4),
	CRUISER   (3),
	DESTROYER (2),
	TORPEDO   (1);
	
	private final int size;
	
	private ShipSize(int size) {
		this.size = size;
	}
	
	public int toInt() {
		return size;
	}
	
	public static ShipSize of(int size) {
		switch(size) {
			case 4: return WARSHIP;
			case 3: return CRUISER;
			case 2: return DESTROYER;
			case 1: return TORPEDO;
			default: return null;
		}
	}
}
