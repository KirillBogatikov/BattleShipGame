package org.battleshipgame.player;

import org.battleshipgame.core.Ship;
import org.battleshipgame.geometry.Point;
import org.battleshipgame.geometry.Rectangle;
import org.battleshipgame.utils.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class Player {
    private List<Point> wrecks, misses, flames;
    private Random random;

    public Player() {
        wrecks = new ArrayList<>();
        misses = new ArrayList<>();
        flames = new ArrayList<>();
        random = new Random();
    }

    public abstract Point nextShot();

    public abstract List<Ship> ships();

    public ShotResult process(Point shot) {
        if(wrecks.contains(shot) || misses.contains(shot)) {
            return new ShotResult(ShotResult.State.TRY_AGAIN, shot.x, shot.y);
        }

        Ship ship = new Stream<>(ships()).firstOrNull(s -> s.getRect().contains(shot, true));

        if (ship != null) {
            cellDown(shot);

            ShotResult.State state = ShotResult.State.HURT;
            Rectangle area = null;
            if(ship.damage() == ship.getSize().size) {
                state = ShotResult.State.KILL;
                area = ship.getArea();
            }

            return new ShotResult(state, shot.x, shot.y, area);
        }

        return new ShotResult(ShotResult.State.MISS, shot.x, shot.y);
    }

    public List<Point> wrecks() {
        return wrecks;
    }

    public List<Point> misses() {
        return misses;
    }

    public List<Point> flames() {
        return flames;
    }

    boolean cellDown(Point point) {
        wrecks.add(point);
        if (random.nextBoolean()) {
            flames.add(point);
            return true;
        }
        return false;
    }
}
