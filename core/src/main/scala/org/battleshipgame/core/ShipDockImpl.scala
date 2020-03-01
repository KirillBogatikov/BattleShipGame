package org.battleshipgame.core

import scala.language.postfixOps
import org.battleshipgame.ui.{Ship, ShipSize, ShipOrientation, ShipsDock}
import org.battleshipgame.ui.ShipOrientation._
import org.battleshipgame.render.{Rectangle, Size, Point}

class ShipDockImpl extends ShipsDock {
    private var dragged: Ship = _
	private var placedShips: Array[Ship] = Array()
	private var leftShips: Array[Ship] = Array()
	private var highlighted: Rectangle = _
	private var orientation: ShipOrientation = HORIZONTAL
	private var listener: RenderListener = _
	
	def this(listener: RenderListener) = {
	    this() 
	    
		ShipSize.values foreach(size => {
			for(i <- 0 until 5 - size.toInt()) {
			    val ship = new Ship(size)
				leftShips = leftShips:+ ship
			}
		})
		
		orientation = HORIZONTAL
		this listener = listener
	}
	
	def switchOrientation(): Unit = {
		this.orientation = this.orientation match {
			case HORIZONTAL => VERTICAL
			case VERTICAL => HORIZONTAL
		}
		
		leftShips foreach(ship => {
			ship.orientation = orientation;
		})
	}
	
	override def left(): Array[Ship] = leftShips

	override def placed(): Array[Ship] = placedShips
	
	override def draggedShip(): Ship = dragged
	
	override def dropShip(): Unit = {
		dragged = null;
	}

	override def onShipDrag(ship: Ship): Unit = {
		dragged = ship;
	}

	override def onShipDrop(x: Int, y: Int): Unit = {
		var size = dragged.rect size
				
		if (x + size.width > 10 || y + size.height > 10) {
			highlighted = new Rectangle(0, 0, 10, 10)
		} else {
			var option = placedShips find(ship => checkIntersects(ship, size, x, y))
				
			if(option isDefined) {
				highlighted = option.get area
			} else {
			    val ship = new Ship(dragged size, new Point(x, y), dragged orientation)
				placedShips = placedShips:+ ship
				
			    val option = leftShips find(ship => ship.size.equals(dragged size))
				if (option isDefined) {
				    removeShip(option get, leftShips)
				}
			}
		}
		
		dropShip();
	}
	
	private def removeShip(ship: Ship, array: Array[Ship]): Array[Ship] = {
	    val buf = array.toBuffer
        buf.remove(array indexOf(ship))
        return buf.toArray
	}
	
	private def checkIntersects(ship: Ship, size: Size, x: Int, y: Int): Boolean = {
		for(i <- 0 until size.width) {
			for(j <- 0 until size.height) {
				if (ship.area contains(new Point(x + i, y + j))) {
					return true
				}
			}
		}
		
		return false
	}

	override def onShipClick(x: Int, y: Int): Unit = {
		var point = new Point(x, y);
		var option = placedShips find(ship => ship.rect().contains(point))
		
		if (option isDefined) {
		    removeShip(option get, placedShips)
			leftShips = leftShips:+ (option get)
		}
	}

	override def invalid(): Rectangle = {
		if (highlighted != null) {
			val timeout = new Thread(() => {
				try {
					Thread sleep(3000L)
					listener needRender
				} catch {
				    case _: Throwable => 
				}
				highlighted = null;
			});
			timeout start;
		}
		
		return highlighted;
	}

}