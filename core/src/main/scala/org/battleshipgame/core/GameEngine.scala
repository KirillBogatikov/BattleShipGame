package org.battleshipgame.core

import scala.language.postfixOps

import org.battleshipgame.MultiPlayer
import org.battleshipgame.network.EventListener
import org.battleshipgame.network.udp.UdpNetworker
import org.battleshipgame.ui.Bay
import org.battleshipgame.ui.ShotListener
import org.battleshipgame.core.AreaParser.{ parse => areaParse }
import org.cuba.log.Log

import PacketGroup._
import ShotParser.parse
import org.battleshipgame.ui.Ship
import org.battleshipgame.render.Point

class GameEngine(
        val playerShips: Array[Ship],
        val log: Log, 
        val coreListener: CoreListener) {
    
	var playerBay: Bay = new Bay((x, y) => {}, playerShips)
	var friendBay: Bay = new Bay((x, y) => {
	    val point = new Point(x, y)
	    player.nextShot(point)
	    friend.processShot(point)
	})
    var player: Player = new Player(playerBay, friendBay)
    var friend: Player = _
	var multiplayer: MultiPlayer = _
	
	private def startMultiPlayer(): Unit = {
	    multiplayer = new MultiPlayer(log, new UdpNetworker(6747), new EngineEventListener(coreListener, player))
	    friend = new RemotePlayer(null, null, multiplayer)
        player.friend = friend
        multiplayer start()
	}
			    
    def connect(gameId: String): Unit = {
        startMultiPlayer()
        multiplayer connect(gameId)
    }
	
    def gameId(): String = {
        startMultiPlayer()
        return multiplayer createId
    }
    
    def skynet(): Unit = {
        friend = new ComputerPlayer(playerBay, friendBay, player)
        player.friend = friend
    }
}

class EngineEventListener(coreListener: CoreListener, player: Player) extends EventListener {
    override def onFriendConnected(): Unit = {
        coreListener.onConnected(true)
    }
    
    override def onConnectedToFriend(): Unit = {
        coreListener.onConnected(false)
    }
    
    override def onPacketReceived(group: String, value: String): Unit = {
        if (group == SHOT_POINT.toString)
            player.processShot(parse(value))
        else if (group == SHOT_RESULT.toString) {
            val parts = value.split(";")
            player.lastShot(ShotResult valueOf(parts(0)), areaParse(parts(1)))
        } else if (group == GAME_RESULT.toString)
            coreListener.onGameEnd(value toBoolean)
    }
}