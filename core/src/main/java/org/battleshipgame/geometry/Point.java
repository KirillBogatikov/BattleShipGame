package org.battleshipgame.geometry;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Point)) {
            return false;
        }

        Point point = (Point)obj;
        return point.x == x && point.y == y;
    }
}
