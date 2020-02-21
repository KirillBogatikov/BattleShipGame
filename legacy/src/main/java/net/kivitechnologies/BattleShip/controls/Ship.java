package net.kivitechnologies.BattleShip.controls;

import java.awt.Rectangle;
import java.util.ArrayList;
 
public class Ship {
    private Point[] points;
    
    public Ship(int x, int y, int o, int size) {
        points = new Point[size];
        for(int i = 0; i < size; i++) {
            points[i] = new Point(x + (o == 0 ? i : 0), y + (o == 0 ? 0 : i));
        }
    }
    
    public boolean isBroken() {
        return getBrokenPoints().length == points.length;
    }
    
    public Rectangle getZone() {
        int w = points.length > 1 && points[0].x == points[1].x ? 3 : points.length + 2;
        int h = points.length > 1 && points[0].x == points[1].x ? points.length + 2 : 3;
        
        return new Rectangle(points[0].x - 1, points[0].y - 1, w, h);
    }
    
    public int getSize() {
        return points.length;
    }
    
    public Point[] getBrokenPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for(Point p : this.points) {
            if(p.type == 1) {
                points.add(p);
            }
        }
        return points.toArray(new Point[points.size()]);
    }
    
    public int[] toInternalFormat() {
        return new int[]{ points[0].x, points[0].y, points.length > 1 && points[0].x == points[1].x ? 1 : 0, points.length };
    }
    
    public int takeAttack(int x, int y) {
        int r = 0;
        boolean s = true;
        for(Point p : points) {
            if(p.type == 0 && p.x == x && p.y == y) {
                r = p.type = 1;
                continue;
            }
            
            if(p.type >= 1 && p.x == x && p.y == y) {
                r = -1;
            }
            s = s && p.type == 1;
        }
        
        return s ? 2 : r;    
    }
    
    public ArrayList<Integer> savingDump() {
        ArrayList<Integer> dump = new ArrayList<Integer>();
        dump.add(points[0].x);
        dump.add(points[0].y);
        dump.add(points[0].x == points[1].x ? 1 : 0);
        dump.add(points.length);
       
        for(Point p : points) {
            if(p.type == 1) {
                dump.add(p.x);
                dump.add(p.y);
            }
        }
        
        return dump;
    }
}
