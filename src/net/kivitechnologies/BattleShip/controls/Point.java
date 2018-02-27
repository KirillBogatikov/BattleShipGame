package net.kivitechnologies.BattleShip.controls;
 
public class Point {
    public int x;
    public int y;
    public int type;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = 0;
    }
    
    public boolean equals(Object o) {
        return o instanceof Point && ((Point)o).x == x && ((Point)o).y == y;
    }
}