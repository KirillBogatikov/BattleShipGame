package org.battleshipgame.player

import scala.language.postfixOps
import org.battleshipgame.core.{ ShotResult, ShotResultListener }
import org.battleshipgame.ui.ShotListener
import org.battleshipgame.core.ShotResult._
import org.battleshipgame.render.{ Point, Rectangle }
import scala.util.control.Breaks
import scala.util.Random

class User extends Player(null) {
    var listener: ShotResultListener = null
    private val random = new Random()
    
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
    }
    
    def shotFriend(x: Int, y: Int): Unit = {
        friend.shotListener onShot(x, y)
    }
    
    def resultListener(): ShotResultListener = (x, y, r, f, a) => {
        val point = Point(x, y)
        r match {
            case MISS => bay.misses = bay.misses:+ point
            case HURT => addWreck(point, f)
            case KILL => {
                addWreck(point, f)
                shipAreaMisses(a)
            }
            case OCCUPIED => { /* TODO */ }
        }
        listener.onShotResult(x, y, r, f, a)
    }
}