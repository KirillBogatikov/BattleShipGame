package org.battleshipgame.player;

import static java.util.Arrays.asList;
import static org.battleshipgame.core.ShipOrientation.Y_AXIS;
import static org.battleshipgame.core.ShipSize.TORPEDO;

import java.util.List;

import org.battleshipgame.core.Ship;
import org.battleshipgame.geometry.Point;

public class AI extends Player {
    private List<Ship> ships;

    public AI() {
        ships = asList(
            new Ship(TORPEDO,   Y_AXIS, new Point(0, 0))/*,
            new Ship(TORPEDO,   Y_AXIS, new Point(6, 2)),
            new Ship(TORPEDO,   X_AXIS, new Point(1, 8)),
            new Ship(TORPEDO,   X_AXIS, new Point(3, 9)),
            new Ship(DESTROYER, X_AXIS, new Point(2, 0)),
            new Ship(DESTROYER, Y_AXIS, new Point(8, 8)),
            new Ship(DESTROYER, Y_AXIS, new Point(0, 5)),
            new Ship(CRUISER,   Y_AXIS, new Point(2, 2)),
            new Ship(CRUISER,   X_AXIS, new Point(3, 7)),
            new Ship(WARSHIP,   X_AXIS, new Point(5, 4))*/
        );
    }

    private int x = 0;
    private int y = 0;

    @Override
    public Point nextShot() {
        try {
            Thread.sleep(1000L);
        } catch(Throwable t) { }

        Point p = new Point(x, y);
        if(++x == 10) {
            y++;
            x = y % 2;
        }

        return p;
    }

    @Override
    public List<Ship> ships() {
        return ships;
    }
}
