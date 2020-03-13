package org.battleshipgame.player

import scala.language.postfixOps
import org.battleshipgame.core.ShotResult
import org.battleshipgame.ui.{ Ship, ShotListener }
import org.battleshipgame.core.ShotResultListener
import scala.util.Random
import org.battleshipgame.render.{ Point, Rectangle }
import org.battleshipgame.core.ShotResult._
import org.battleshipgame.ui.ShipSize._
import org.battleshipgame.ui.ShipOrientation._
import scala.util.control.Breaks

class AI(friend: Player) extends Player(friend) {
    private var currentX: Int = 0
    private var currentY: Int = 0
    
    private var lastResult: ShotResult = null
    private var lastX: Int = 0
    private var lastY: Int = 0
    
    private var vectorFound: Boolean = false
    
    private var topChecked   : Boolean = false
    private var topVector    : Boolean = false
    
    private var leftChecked  : Boolean = false
    private var leftVector    : Boolean = false
    
    private var rightChecked : Boolean = false
    private var rightVector    : Boolean = false
    
    private var bottomChecked: Boolean = false
    private var bottomVector    : Boolean = false
    
    private val random = new Random()
    
    def placeShips(): Unit = {
        bay.ships = Array(
            new Ship(TORPEDO,   new Point(0, 0), VERTICAL),
            new Ship(TORPEDO,   new Point(6, 2), VERTICAL),
            new Ship(TORPEDO,   new Point(1, 8), HORIZONTAL),
            new Ship(TORPEDO,   new Point(3, 9), HORIZONTAL),
            new Ship(DESTROYER, new Point(2, 0), HORIZONTAL),
            new Ship(DESTROYER, new Point(8, 8), VERTICAL),
            new Ship(DESTROYER, new Point(0, 5), VERTICAL),
            new Ship(CRUISER,   new Point(2, 2), VERTICAL),
            new Ship(CRUISER,   new Point(3, 7), HORIZONTAL),
            new Ship(WARSHIP,   new Point(5, 4), HORIZONTAL)
        )
    }
    
    def shotListener(): ShotListener = (x, y) => {
        if (empty(x, y)) {
            friend.resultListener onShotResult(x, y, OCCUPIED)
        }
        
        val point = Point(x, y)
        
        var result = MISS
        var area: Rectangle = null
        val breaks = new Breaks();
        import breaks.{ breakable, break }
        
        breakable {
            bay.ships foreach(ship => {
                if (ship.rect contains(point)) {
                    ship.damage(point)
                    
                    var result: ShotResult = null
                    if (ship.totalDamage == ship.size.toInt) {
                        result = KILL
                        area = ship area
                    } else {
                        result = HURT
                    }
                                        
                    break
                }
            })
        }
                
        friend.resultListener onShotResult(x, y, result, random nextBoolean, area)
        if (result == MISS) {
            shot()
        }
    }
    
    private def shot(): Unit = {
        lastResult match {
            case MISS => 
            case KILL => {
                currentX += 2
                
                if (currentX > 9) {
                    currentX = 0
                    currentY += 1
                }
            }
            case OCCUPIED => { /* TODO */ }
            case HURT => {
                if (!topChecked && lastY > 0) { 
                    topChecked = true
                    lastY -= 1
                } else if(!leftChecked && lastX > 0) {
                    leftChecked = true
                    lastX -= 1
                } else if(!bottomChecked && lastY < 8) {
                    bottomChecked = true
                    lastY += 1
                } else if(!rightChecked && lastX < 8) {
                    rightChecked = true
                    lastX += 1
                } else {
                    if (vectorFound) {
                        if (leftVector) {
                            lastX -= 1
                        } else if(rightVector) {
                            lastX += 1
                        } else if(topVector) {
                            lastY -= 1
                        } else if(bottomVector) {
                            lastY += 1
                        }
                    }
                }
                
                friend.shotListener onShot(lastX, lastY)
                return
            }
        }
        
        currentX += 2
        
        if (currentX > 9) {
            currentX = 0
            currentY += 1
        }
        
        friend.shotListener onShot(currentX, currentY)
    }
     
    def resultListener(): ShotResultListener = (x, y, r, f, a) => {
        if (r == HURT) {
            shot()
        } else if(r == MISS) {
            if (vectorFound) {
                if (lastResult == HURT) {
                    if (leftVector) {
                        leftVector = false
                        rightVector = true
                        lastX = currentX                        
                    } else if (rightVector) {
                        leftVector = true
                        rightVector = false
                        lastX = currentX  
                    } else if (topVector) {
                        topVector = false
                        bottomVector = true
                        lastY = currentY  
                    } else if (bottomVector) {
                        topVector = true
                        bottomVector = false
                        lastY = currentY  
                    }
                }
            }
        } else if(r == KILL) {
            vectorFound = false
    
            topChecked = false
            topVector  = false
    
            leftChecked = false
            leftVector  = false
    
            rightChecked = false
            rightVector  = false
    
            bottomChecked = false
            bottomVector  = false
            
            shot()
        } else if(r == OCCUPIED) {
            currentX += 2
            if (currentX > 9) {
                currentX = 0
                currentY += 1 
            }
        }
        
        lastResult = r
    }
        
}