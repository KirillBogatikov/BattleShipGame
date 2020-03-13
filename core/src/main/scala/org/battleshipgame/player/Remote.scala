package org.battleshipgame.player

import scala.language.postfixOps
import scala.util.Random

import org.battleshipgame.MultiPlayer
import org.battleshipgame.core.RemotePlayerListener
import org.battleshipgame.core.ShotResult
import org.battleshipgame.core.ShotResult._
import org.battleshipgame.core.ShotResultListener
import org.battleshipgame.network.EventListener
import org.battleshipgame.render.Point
import org.battleshipgame.render.Rectangle
import org.battleshipgame.ui.ShotListener

class RemotePlayer(friend: Player, val multiplayer: MultiPlayer, listener: RemotePlayerListener) extends Player(friend) {
     private val random = new Random()
     multiplayer listener = new EventListener() { 
         def onFriendConnected(): Unit = listener.onFriendConnected()
         def onConnectedToFriend(): Unit = listener.onConnectedToFriend()
        
         def onPacketReceived(group: String, value: String): Unit = {
             group match {
                 case "shot_result" => {
                     val parts = value split(";")
                     val point = Point(parts(0) toInt, parts(1) toInt)
                     val result = ShotResult valueOf(parts(2))
                     val flame = parts(3) toBoolean
                     var area: Rectangle = null
                     
                     if (parts.length > 4) {
                         area = Rectangle(parts(4) toInt, parts(5) toInt, parts(6) toInt, parts(7) toInt)
                     }
                     
                     resultListener onShotResult(point x, point y, result, flame, area)
                 }
                 case "game_result" => {
                     if (value toBoolean) {
                         listener onGameWin()
                     } else {
                         listener onGameLose()
                     }
                 }
             }
         }
     }
    
     def shotListener(): ShotListener = (x, y) => {
         multiplayer send("shot", x + ", " + y)
     }
     
     def resultListener(): ShotResultListener = (x, y, r, f, a) => {
         val point = Point(x, y)
         r match {
             case OCCUPIED => {}
             case MISS => bay.miss(point)
             case HURT => addWreck(point, f)
             case KILL => {
                 addWreck(point, f)
                 shipAreaMisses(a)
             }
         }
     }
}