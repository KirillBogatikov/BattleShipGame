package org.battleshipgame.ui;

/**
 * Ориентация корабля - горизонтальная или вертикальная (относительно экрана пользователя есесено)
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
public enum ShipOrientation {
	VERTICAL   (2),
	HORIZONTAL (1);
	
	private final int id;
	
	private ShipOrientation(int id) {
		this.id = id;
	}
	
	/**
	 * Конвертировать в числовой флаг
	 * 
	 * @return число какое-то
	 */
	public int toInt() {
		return id;
	}
	
	/**
	 * Конвертировать из числового флага
	 * 
	 * @param size какое-то число
	 * @return ориентация
	 */
	public static ShipOrientation of(int size) {
		switch(size) {
			case 2: return VERTICAL;
			case 1: return HORIZONTAL;
			default: return null;
		}
	}
}
