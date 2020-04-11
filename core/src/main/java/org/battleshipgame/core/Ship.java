package org.battleshipgame.core;

import org.battleshipgame.geometry.Point;
import org.battleshipgame.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private ShipSize size;
    private ShipOrientation orientation;
    private Rectangle rect, area;
    private int damage;

    public Ship(ShipSize size, ShipOrientation orientation, Point start) {
        this.size = size;
        this.orientation = orientation;

        int width = 1, height = size.size;

        if (orientation == ShipOrientation.X_AXIS) {
            width = height;
            height = 1;
        }

        this.rect = new Rectangle(start, width, height);

        width += 2;
        height += 2;

        int x = start.x - 1, y = start.y - 1;

        if (x < 0) {
            x = 0;
            width--;
        }
        if (y < 0) {
            y = 0;
            height--;
        }

        if (x + width > 10) {
            width--;
        }
        if(y + height > 10) {
            height--;
        }

        this.area = new Rectangle(new Point(x, y), width, height);
    }

    public ShipSize getSize() {
        return size;
    }

    public ShipOrientation getOrientation() {
        return orientation;
    }

    public Rectangle getRect() {
        return rect;
    }

    public Rectangle getArea() {
        return area;
    }

    public List<Point> getPoints(boolean area) {
        ArrayList<Point> list = new ArrayList<>();
        Rectangle r = area ? this.area : rect;

        for(int x = 0; x < r.getWidth(); x++) {
            for(int y = 0; y < r.getHeight(); y++) {
                list.add(new Point(x + r.start.x, y + r.start.y));
            }
        }
        return list;
    }

    public int damage() {
        return ++damage;
    }
}
