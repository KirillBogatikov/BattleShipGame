package org.battleshipgame.ui

import org.battleshipgame.render.Point

/**
 * Кораблик:
 * <ul>
 * 	<li>размер</li>
 * 	<li>самая левая/верхняя точка</li>
 * 	<li>ориентация</li>
 * </ul> 
 * 
 * @author Кирилл Испольнов
 * @version 1.0
 * @since 2.0.0
 */
case class Ship(val size: ShipSize, val point: Point, val orientation: ShipOrientation) { }
