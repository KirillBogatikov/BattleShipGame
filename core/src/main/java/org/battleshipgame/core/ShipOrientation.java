package org.battleshipgame.core;

public enum ShipOrientation {
    X_AXIS (0),
    Y_AXIS (1);

    public final int flag;

    private ShipOrientation(int flag) {
        this.flag = flag;
    }
}
