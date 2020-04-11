package org.battleshipgame.core;

import org.battleshipgame.geometry.Point;
import org.battleshipgame.geometry.Rectangle;
import org.battleshipgame.player.Player;
import org.battleshipgame.player.ShotResult;
import org.battleshipgame.player.User;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BattleShipGame {
    private Executor threadPool;
    private User user;
    private Player friend;
    private Info userInfo;
    private Info friendInfo;
    private AtomicBoolean gameActive;

    public BattleShipGame() {
        threadPool = Executors.newFixedThreadPool(3);
        user = new User();
        userInfo = new Info();
        friendInfo = new Info();
        gameActive = new AtomicBoolean(true);
    }

    public User getUser() {
        return user;
    }

    public Player getFriend() {
        return friend;
    }

    public Info getUserInfo() {
        return userInfo;
    }

    public Info getFriendInfo() {
        return friendInfo;
    }

    public void setFriend(Player friend) {
        this.friend = friend;
    }

    private Player playerA, playerB;
    private Info info;

    public void start() {
        Random random = new Random();
        playerA = user;
        playerB = friend;
        info = friendInfo;

        threadPool.execute(() -> {
            while(gameActive.get()) {
                Point shot = playerA.nextShot();
                ShotResult result = playerB.process(shot);
                switch(result.state) {
                    case MISS:
                        info.miss(shot);

                        if(playerA == user) {
                            playerA = friend;
                            playerB = user;
                            info = userInfo;
                        } else {
                            playerA = user;
                            playerB = friend;
                            info = friendInfo;
                        }
                    break;
                    case HURT:
                        info.wreck(shot);
                        if(random.nextBoolean()) {
                            info.flame(shot);
                        }
                    break;
                    case KILL:
                        info.wreck(shot);
                        if(random.nextBoolean()) {
                            info.flame(shot);
                        }

                        Rectangle area = result.area;

                        for(int x = 0; x < area.getWidth(); x++) {
                            info.miss(new Point(area.start.x + x, area.start.y));
                            info.miss(new Point(area.start.x + x, area.end.y));
                        }

                        for(int y = 1; y < area.getHeight() - 1; y++) {
                            info.miss(new Point(area.start.x, area.start.y + y));
                            info.miss(new Point(area.end.x, area.start.y + y));
                        }
                    break;
                    case TRY_AGAIN:
                        info.tryAgain(shot);
                    break;
                }

                try {
                    Thread.sleep(34L);
                } catch (Throwable t) { }
            }
        });
    }

    public void stop() {
        gameActive.set(false);
    }
}
