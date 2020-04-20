package org.battleshipgame.core;

import org.battleshipgame.geometry.Point;

public interface InfoChangeListener {
    public void onMiss(Point point);
    public void onFlame(Point point);
    public void onWreck(Point point);
    public default void onTryAgain(Point point) {

    }
}
