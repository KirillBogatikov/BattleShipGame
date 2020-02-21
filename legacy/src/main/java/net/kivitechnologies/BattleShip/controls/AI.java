package net.kivitechnologies.BattleShip.controls;

import java.awt.Rectangle;
import java.util.ArrayList;

public class AI { 
    public Ship[] ships;
    private ArrayList<Point> attacks; 
    private Ship lastSunkedShip;
    
    public AI() {
        ships = new Ship[10];
        attacks = new ArrayList<Point>();
        attacks.add(new Point(-2, 1));
        mapShipsPattern();
    }
    
    public void mapShipsPattern() {
       int id = (int)(Math.random() * Resources.patterns.size());
       System.out.println(id);
       ships = Resources.patterns.get(id);
    }
    
    @Deprecated
    public void mapShipsRandom() {
        genShip(0, 4);
        genShip(1, 3);
        genShip(2, 3);
        genShip(3, 2);
        genShip(4, 2);
        genShip(5, 2);
        genShip(6, 1);
        genShip(7, 1);
        genShip(8, 1);
        genShip(9, 1);
    }
    
    private void genShip(int i, int size) {
        int x = generate110(), y = generate110();
        Ship ship = new Ship(x, y, Math.random() > 0.5 ? 1 : 0, size);
        for(Ship vship : ships) {
            if(vship != null && vship.getZone().intersects(ship.getZone())) { 
               genShip(i, size);
               return;
            }
        }
        
        ships[i] = ship;
        
    }
    
    private int generate110() {
        return (int)(Math.random() * 10 + 1);
    }
    
    public Ship getLastSunkedShip() {
        return lastSunkedShip;
    }
    
    public int takeAttack(int x, int y) {
        int ar;
        for(Ship ship : ships) {
            if(ship.isBroken())
                continue;
            
            ar = ship.takeAttack(x, y);
            if(ar > 0) {
                if(ar == 2)
                    lastSunkedShip = ship;
                
                return ar;
            }
        }
        return 0;
    }
    
    public void applyAttackResult(Ship ship, int r) {
        Point attack = attacks.get(attacks.size() - 1);
        attack.type = r;
        
        if(r == 2) {
            Rectangle zone = ship.getZone();
            
            Point point;
            for(int i = 0; i < zone.width; i++) {
                point = new Point(zone.x + i, zone.y);
                if(!attacks.contains(point))
                    attacks.add(point);
                point = new Point(zone.x + i, zone.y + zone.height - 1);
                if(!attacks.contains(point))
                    attacks.add(point);
            }
            
            for(int i = 0; i < zone.height; i++) {
                point = new Point(zone.x, zone.y + i);
                if(!attacks.contains(point))
                    attacks.add(point);
                point = new Point(zone.x + zone.width - 1, zone.y + i);
                if(!attacks.contains(point))
                    attacks.add(point);
            }
        }
    }
    
    public int[] createAttack() {
        Point attack = new Point(generate110(), generate110());
        if(attacks.contains(attack))
            return createAttack();
            
        attacks.add(attack);
        return new int[]{ attack.x, attack.y };
    }
    
    private int[] lastAttack = new int[]{ -2, 0 };
    private int k = 2;
    public int[] generateAttack() {
        int x = lastAttack[0], y = lastAttack[1];
        x += 2;
        
        if(x > 10) { 
            x = k - y % 2;
            if(x == -1)
                x = 1;
            
            y++;
        }
        
        if(y > 10) {
            y = 1;
            k--;
        }
        
        lastAttack[0] = x;
        lastAttack[1] = y;
        
        Point p = new Point(x, y);
        if(attacks.contains(p))
            return generateAttack();
        
        System.err.println(x + ":" + y);
        return lastAttack;
    }
    
    public Ship[] getShips() {
        return ships;
    }
}

