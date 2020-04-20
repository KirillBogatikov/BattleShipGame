package org.battleshipgame.android;

import org.battleshipgame.core.BattleShipGame;
import org.battleshipgame.player.Player;
import org.battleshipgame.player.User;

public class GameInstance {
    private static BattleShipGame game;

    public static void create() {
        game = new BattleShipGame();
    }

    public static BattleShipGame get() {
        return game;
    }

    public static User getUser() {
        return get().getUser();
    }

    public static Player getFriend() {
        return get().getFriend();
    }
}
