package net.kivitechnologies.BattleShip.controls;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
 
public class Resources {
    public static Image[] wholeShips, brokenShips;
    public static Image startScreenLogo, flame, missSignal;
    public static Font applicationFont;
    public static ArrayList<Ship[]> patterns;
    
    public static void initiate() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        URL startScreenLogoPath = Resources.class.getClassLoader().getResource("res/images/start_screen_logo.png");
        startScreenLogo = toolkit.getImage(startScreenLogoPath);
        
        wholeShips = new Image[4];
        URL shipPath;
        for(int i = 0; i < wholeShips.length; i++) {
            shipPath = Resources.class.getClassLoader().getResource(String.format("res/images/ships/whole/%d.png", i+1));
            wholeShips[i] = toolkit.getImage(shipPath);
        }
        
        brokenShips = new Image[4];
        for(int i = 0; i < brokenShips.length; i++) {
            shipPath = Resources.class.getClassLoader().getResource(String.format("res/images/ships/broken/%d.png", i+1));
            brokenShips[i] = toolkit.getImage(shipPath);
        }
        
        URL flamePath = Resources.class.getClassLoader().getResource("res/images/flame.png");
        flame = toolkit.getImage(flamePath);
        URL missPath = Resources.class.getClassLoader().getResource("res/images/miss.png");
        missSignal = toolkit.getImage(missPath);
        
        URL fontPath = Resources.class.getClassLoader().getResource("res/fonts/OwnHand.ttf");
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, fontPath.openStream());
        } catch (Exception e) {
            
        }
        
        try {
            patterns = new ArrayList<Ship[]>();
            JsonParser jp = new JsonParser();
            InputStream ins = Resources.class.getClassLoader().getResourceAsStream("res/mappingPatterns.json");
            File tmpf = new File(System.getProperty("user.dir"), "ptrns.json.tmp");
            tmpf.deleteOnExit();
            OutputStream tmp = new FileOutputStream(tmpf);
            int b;
            while((b = ins.read()) != -1) {
                tmp.write(b);
            }
            
            tmp.flush();
            tmp.close();
            JsonArray root = (JsonArray)jp.parse(new FileReader(tmpf));
            
            int count = root.get(0).getAsInt();
            
            JsonArray arr, ship;
            Ship[] pattern;
            for(int i = 1; i <= count; i++) {
                arr = (JsonArray)root.get(i);
                pattern = new Ship[10];
                for(int j = 0; j < 10; j++) {
                    ship = (JsonArray)arr.get(j);
                    pattern[j] = new Ship(ship.get(0).getAsInt() - 1, ship.get(1).getAsInt() - 1, ship.get(2).getAsInt(), ship.get(3).getAsInt());
                }
                patterns.add(pattern);
            }
            
            System.gc();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
