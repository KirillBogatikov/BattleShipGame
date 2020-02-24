package org.battleshipgame.ui

import org.battleshipgame.render.Point

case class Ship(val size: ShipSize, val point: Point, val orientation: ShipOrientation) { }
