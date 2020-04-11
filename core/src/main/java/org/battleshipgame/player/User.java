package org.battleshipgame.player;

import org.battleshipgame.core.Ship;
import org.battleshipgame.geometry.Point;
import org.battleshipgame.geometry.Rectangle;
import org.battleshipgame.utils.Stream;

import java.util.ArrayList;
import java.util.List;

public class User extends Player {
    private final Object mutex = new Object();
    private Point shot;
    private List<Ship> ships = new ArrayList<>(10);

    @Override
    public Point nextShot() {
        Point s;
        while(true) {
            try {
                Thread.sleep(34L);
            } catch(Throwable ignored) {}

            synchronized (mutex) {
                if(shot != null) {
                    s = shot;
                    shot = null;
                    break;
                }
            }
        }
        return s;
    }

    @Override
    public List<Ship> ships() {
        return ships;
    }

    public void setShot(Point shot) {
        synchronized (mutex) {
            this.shot = shot;
        }
    }

    public Rectangle placeShip(Ship ship) {
        List<Point> points = ship.getPoints(false);
        Ship intersected = new Stream<>(ships).firstOrNull(s -> {
            for (Point point : points) {
                if(s.getArea().contains(point, true)) {
                    return true;
                }
            }
            return false;
        });

        if(intersected != null) {
            return intersected.getArea();
        }

        ships.add(ship);
        return null;
    }
}
