package org.battleshipgame.player

import scala.language.postfixOps
import org.battleshipgame.core.{ ShotResult, ShotResultListener }
import org.battleshipgame.ui.ShotListener
import org.battleshipgame.core.ShotResult._
import org.battleshipgame.render.{ Point, Rectangle }
import scala.util.control.Breaks
import scala.util.Random

class User extends Player(null) {
    private val random = new Random()
    
    def shotListener(): ShotListener = (x, y) => {
        println("USER: processing shot at " + x + ", " + y)
        
        if (occupied(x, y)) {
            friend.resultListener onShotResult(x, y, OCCUPIED)
        }
        
        val point = Point(x, y)
        
        var result = MISS
        var area: Rectangle = null
        val flame = random nextBoolean
        val breaks = new Breaks();
        import breaks.{ breakable, break }
        
        breakable {
            bay.ships foreach(ship => {
                if (ship.rect contains(point)) {
                    ship.damage(point)
                    
                    if (ship.totalDamage == ship.size.toInt) {
                        result = KILL
                        area = ship.area
                        shipAreaMisses(area)
                    } else {
                        result = HURT
                    }
                    
                    addWreck(point, flame)
                                        
                    break
                }
            })
        }
        
        if (result == MISS) {
            bay miss(point)
        }
        
        friend.resultListener onShotResult(x, y, result, flame, area)
    }
    
    def shotFriend(x: Int, y: Int): Unit = {
        println("USER: shot friend at " + x +", " + y)
        friend.shotListener onShot(x, y)
    }
    
    def resultListener(): ShotResultListener = (x, y, r, f, a) => {
        println("USER: shot result at " + x +", " + y + ": " + r)
        val point = Point(x, y)
        r match {
            case MISS => {}
            case HURT => {}
            case KILL => {}
            case OCCUPIED => { /* TODO */ }
        }
        listener.onShotResult(x, y, r, f, a)
    }
}