package org.battleshipgame.player;

import org.battleshipgame.geometry.Rectangle;

public class ShotResult {
    public enum State {
        KILL, HURT, MISS, TRY_AGAIN
    }

    public State state;
    public int x;
    public int y;
    public Rectangle area;

    public ShotResult(State state, int x, int y) {
        this(state, x, y, null);
    }

    public ShotResult(State state, int x, int y, Rectangle area) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.area = area;
    }
}
