package org.battleshipgame.core

import scala.language.postfixOps

import org.battleshipgame.MultiPlayer
import org.battleshipgame.network.{ EventListener, GameId }
import org.battleshipgame.network.udp.UdpNetworker
import org.battleshipgame.ui.{ Bay, ShotListener }
import org.battleshipgame.core.AreaParser.{ parse => areaParse }
import org.cuba.log.Log

import org.battleshipgame.ui.Ship
import org.battleshipgame.render.Point
import org.battleshipgame.player.{ Player, RemotePlayer, User, AI }

class GameEngine(val log: Log) {
    	
    var user: User = new User()
    var friend: Player = _
    
	def startMultiPlayer(listener: RemotePlayerListener): Unit = {
        val mp = new MultiPlayer(log, new UdpNetworker(6747))
	    friend = new RemotePlayer(user, mp, listener)
        user.friend = friend
	}
    
    def connectToFriend(hash: String, address: String): Unit = {
        val remoteFriend = user.friend.asInstanceOf[RemotePlayer]
        remoteFriend.multiplayer connect(new GameId(hash, address))
    }
    
    def gameId(): GameId = {
        val remoteFriend = user.friend.asInstanceOf[RemotePlayer]
        return remoteFriend.multiplayer createId()
    }
			 
    def startAI(): Unit = {
        friend = new AI(user)
        user.friend = friend
    }
}