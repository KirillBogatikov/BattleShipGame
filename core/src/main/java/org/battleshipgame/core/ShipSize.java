package org.battleshipgame.core;

public enum ShipSize {
    WARSHIP   (4),
    CRUISER   (3),
    DESTROYER (2),
    TORPEDO   (1);

    public final int size;

    private ShipSize(int size) {
        this.size = size;
    }
}
