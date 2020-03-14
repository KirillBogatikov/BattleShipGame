package org.battleshipgame.impl

import scala.language.postfixOps
import org.battleshipgame.ui.{Ship, ShipSize, ShipOrientation, ShipsDock}
import org.battleshipgame.ui.ShipOrientation._
import org.battleshipgame.render.{Rectangle, Size, Point}
import org.battleshipgame.core.RenderListener
import scala.util.control.Breaks

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
		highlighted = new Rectangle(0, 0, 10, 10)
		dragged = null;
	}

	override def onShipDrag(ship: Ship): Unit = {
		dragged = ship;
	}

	override def onShipDrop(x: Int, y: Int): Unit = {
		var size = dragged.rect size
				
		var option = placedShips find(ship => checkIntersects(ship, size, x, y))
				
		if(option isDefined) {
			highlighted = option.get area
			
			if (highlighted.start.x < 0) {
			    highlighted.x = 0
			    highlighted.width -= 1
			}
			
			if (highlighted.start.y < 0) {
			    highlighted.y = 0
			    highlighted.height -= 1
			}
			
			if (highlighted.end.x > 9) {
			    highlighted.width -= 1
			}
			
			if (highlighted.end.y > 9) {
			    highlighted.height -= 1
			}
		} else {
		    val ship = new Ship(dragged size, new Point(x, y), dragged orientation)
			placedShips = placedShips:+ ship
			leftShips = removeShip(dragged, leftShips)
		}
		
		dragged = null;
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
		    placedShips = removeShip(option get, placedShips)
		    
		    var index: Int = 0
		    
		    val breaks = new Breaks
		    import breaks.{ break, breakable }
		    
		    breakable {
		        leftShips foreach(ship => {
		            index += 1
		            if (ship.size == option.get.size) { 
		                break
		            }
		        })
		    }
		    
    	    val buf = leftShips.toBuffer
    	    buf.insert(index, option.get)
    	    leftShips = buf.toArray
		}
	}

	override def highlightedArea(): Rectangle = {
		if (highlighted != null) {
			val timeout = new Thread(() => {
				try {
					Thread sleep(3000L)
					listener render
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