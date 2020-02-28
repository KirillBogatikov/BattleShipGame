package org.battleshipgame.core;

import org.battleshipgame.network.ShotResult;
import org.battleshipgame.render.Point;

public interface Player {
	public ShotResult processShot(Point point);
	public Point nextShot();
	public void lastShot(ShotResult result);
}
