package org.battleshipgame.geometry;

import static java.util.Arrays.asList;

public class Rectangle {
    public Point start;
    public Point end;

    public Rectangle(int startX, int startY, int endX, int endY) {
        this(new Point(startX, startY), new Point(endX, endY));
    }

    public Rectangle(Point start, int width, int height) {
        this(start, new Point(start.x + width - 1, start.y + height - 1));
    }

    public Rectangle(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public int getWidth() {
        return Math.abs(start.x - end.x) + 1;
    }

    public int getHeight() {
        return Math.abs(start.y - end.y) + 1;
    }

    public boolean contains(Point point, boolean allowOnBorder) {
        int sx = Math.min(start.x, end.x);
        int sy = Math.min(start.y, end.y);
        int ex = Math.max(start.x, end.x);
        int ey = Math.max(start.y, end.y);

        if (allowOnBorder) {
            return point.x >= sx && point.y >= sy && point.x <= ex && point.y <= ey;
        }
        return point.x > sx && point.y > sy && point.x < ex && point.y < ey;
    }

    @Override
    public String toString() {
        return "(" + start.x + ", " + start.y + ") -> (" + end.x + ", " + end.y + ")";
    }
}
