package net.kivitechnologies.BattleShip.view;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.kivitechnologies.BattleShip.controls.Application;
import net.kivitechnologies.BattleShip.controls.Point;
import net.kivitechnologies.BattleShip.controls.Resources;
import net.kivitechnologies.BattleShip.controls.Ship;

 
@SuppressWarnings("serial")
public class BattleScreen extends Screen implements MouseListener {
    private Rectangle aiField, userField, start, exit, surrender;
    private int stepCount;
    private boolean isUserWin = true, isUserStep = true, isGameFinished = false;
    private boolean isExitPressed, isSurrenderPressed, isStartPressed, ignoreCallback = false;
    private ArrayList<Ship> userShips;
    private Stroke mainBorderStroke, cellBorderStroke; 
    private long startTime = -1, lessTime = 0, pauseTime = -1, stopTime = -1;
    private Application myApp;
    private Timer timer;
    private TimerTask task;
    private ArrayList<Point> userAttacks, aiAttacks;
    
    public BattleScreen(Application app) {
        super();
        myApp = app;
        aiField = new Rectangle();
        userField = new Rectangle();
        exit = new Rectangle();
        surrender =  new Rectangle();
        start = new Rectangle();
        
        mainBorderStroke = new BasicStroke(2.5f);
        cellBorderStroke = new BasicStroke(0.75f);
        
        userAttacks = new ArrayList<Point>();
        aiAttacks = new ArrayList<Point>();
        
        addMouseListener(this);
    }
    
    public void applyShips(ArrayList<Ship> ships) {
        this.userShips = ships;
    }
    
    public void startBattle() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        createTimerTask();
        
        timer.scheduleAtFixedRate(task, 1050, 1000);
        repaint();
        
    }
    
    private void createTimerTask() {
        task = new TimerTask(){
            public void run() {
                boolean aiWin = isUserWin = true;
                for(Ship ship : userShips) {
                    if(!ship.isBroken()) {
                        aiWin = false;
                        break;
                    }
                }
                
                for(Ship ship : myApp.getCurrentAI().getShips()) {
                    System.out.print(ship.isBroken());
                    if(!ship.isBroken()) {
                        isUserWin = false;
                        break;
                    }
                }
                
                System.out.println();
                
                if(isUserWin || aiWin) {
                    stopBattle();
                }
                
                if(!isUserStep) {
                    int[] attack = myApp.getCurrentAI().generateAttack();
                    Point p = new Point(attack[0], attack[1]);
                    
                    int r = 0;
                    for(Ship ship : userShips) {
                        r = ship.takeAttack(attack[0], attack[1]);
                        if(r == 0) {
                            isUserStep = true;
                        } else {
                            isUserStep = false;
                            break;
                        }
                    }
                    p.type = r;
                    
                    if(!aiAttacks.contains(p))
                        aiAttacks.add(p);
                }
                
                repaint();
            }
        };
    }
    
    public void pauseBattle() {
        if(startTime != -1) {
            task.cancel();
            pauseTime = System.currentTimeMillis();
        }
    }
    
    public void stopBattle() {
        stopTime = System.currentTimeMillis();
        isGameFinished = true;
        task.cancel();
        myApp.showResults(stopTime - startTime - lessTime, stepCount, isUserWin);
    }
    
    public void continueBattle() {
        if(startTime != -1) {
            createTimerTask();
            lessTime += System.currentTimeMillis() - pauseTime;
            pauseTime = -1;
            timer.scheduleAtFixedRate(task, 1050, 1000);
        }
    }
    
    public void calculate() {
        super.calculate();
        
        int width = getWidth(), height = getHeight();
        int hpadding = width / 25;
        int fieldSize = (width - hpadding * 3) / 2;
        fieldSize = Math.floorDiv(fieldSize, 10) * 10;
        int vpadding = (height - fieldSize) / 2; 
                
        aiField.width = aiField.height = userField.width = userField.height = fieldSize;
        aiField.x = hpadding;
        aiField.y = userField.y = vpadding;
        userField.x = width - fieldSize - hpadding;
        
        int buttonWidth = width / 2, buttonHeight = vpadding;
        exit.y = surrender.y = height - buttonHeight * 4 / 5;
        exit.width = surrender.width = buttonWidth * 6 / 10;
        exit.height = surrender.height = buttonHeight * 3 / 5;
        exit.x = buttonWidth / 10;
        surrender.x = width - surrender.width - buttonWidth / 10;
        
        start.x = width / 4;
        start.y = height * 3 / 7;
        start.width = width / 2;
        start.height = height / 7;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth(), height = getHeight();
        
        Graphics2D g2d = (Graphics2D)g;
        if(startTime != -1) {
            g2d.clearRect(0, 0, width, height);
        }
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setStroke(mainBorderStroke);
        g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
        g2d.setFont(Resources.applicationFont.deriveFont(width / 10.0f));
        FontMetrics fm = g2d.getFontMetrics();
        
        if(pauseTime != -1) {
            g2d.drawString("Пауза", (width - fm.stringWidth("Пауза")) / 2, (height - fm.getHeight()) / 2);
            return;
        }
        
        g2d.setFont(Resources.applicationFont.deriveFont(width / 30.0f));
        fm = g2d.getFontMetrics();
        g2d.setColor(isExitPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
        g2d.fillRoundRect(exit.x, exit.y, exit.width, exit.height, exit.height, exit.height);
        if(startTime == -1) {
            g2d.setColor(isStartPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
            g2d.fillRoundRect(start.x, start.y, start.width, start.height, start.height, start.height);
        } else {
            g2d.setColor(isSurrenderPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
            g2d.fillRoundRect(surrender.x, surrender.y, surrender.width, surrender.height, surrender.height, surrender.height);
        }
        
        g2d.setColor(isExitPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
        g2d.drawRoundRect(exit.x, exit.y, exit.width, exit.height, exit.height, exit.height);
        if(startTime == -1) {
            g2d.setColor(isExitPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
            g2d.drawRoundRect(start.x, start.y, start.width, start.height, start.height, start.height);
        } else {
            g2d.setColor(isExitPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
            g2d.drawRoundRect(surrender.x, surrender.y, surrender.width, surrender.height, surrender.height, surrender.height);
        }
        
        g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
        if(startTime == -1) {
            g2d.drawString("Назад", exit.x + (exit.width - fm.stringWidth("Назад")) / 2, exit.y + exit.height - fm.getHeight() / 2);
            g2d.setFont(Resources.applicationFont.deriveFont(width / 15.0f));
            fm = g2d.getFontMetrics();
            g2d.drawString("Начать бой!", start.x + (start.width - fm.stringWidth("Начать бой!")) / 2, start.y + start.height - fm.getHeight() / 3);
            return;
        }
        
        g2d.drawString("Выход", exit.x + (exit.width - fm.stringWidth("Выход")) / 2, exit.y + exit.height - fm.getHeight() / 2);
        g2d.drawString("Капитулировать", surrender.x + (surrender.width - fm.stringWidth("Капитулировать")) / 2, surrender.y + surrender.height - fm.getHeight() / 2);
        
        g2d.setFont(Resources.applicationFont.deriveFont(width / 20.0f));
        fm = g2d.getFontMetrics();
        String timerContent = countTimer();
        g2d.clearRect(0, 0, width, fm.getHeight() * 2);
        g2d.drawString(timerContent, (width - fm.stringWidth(timerContent)) / 2, fm.getHeight() * 7 / 6);
        
        g2d.setFont(Resources.applicationFont.deriveFont(width / 30.0f));
        fm = g2d.getFontMetrics();
        g2d.setColor(Theme.BACKGROUND_COLOR_DEFAULT);
        g2d.fillRect(aiField.x, aiField.y, aiField.width, aiField.height);
        g2d.fillRect(userField.x, userField.y, userField.width, userField.height);
        
        g2d.setStroke(cellBorderStroke);
        int cellSize = aiField.width / 10;
        String sym;
        for(int i = 1; i < 11; i++) {
            g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
            sym = String.valueOf(i);
            g2d.drawString(sym, aiField.x + cellSize * (i - .5f) - fm.stringWidth(sym) / 2, aiField.y - fm.getHeight() / 5);
            g2d.drawString(sym, userField.x + cellSize * (i - .5f) - fm.stringWidth(sym) / 2, userField.y - fm.getHeight() / 5);
            sym = new String(Character.toChars(64 + i));
            g2d.drawString(sym, aiField.x - fm.stringWidth(sym) * 3 / 2, aiField.y + cellSize * (i) - fm.getHeight() / 5);
            g2d.drawString(sym, userField.x - fm.stringWidth(sym) * 3 / 2, userField.y + cellSize * (i) - fm.getHeight() / 5);
            
            g2d.setColor(Theme.CELLS_BORDER_COLOR);
            g2d.drawLine(aiField.x + cellSize * i, aiField.y, aiField.x + cellSize * i, aiField.y + aiField.height);
            g2d.drawLine(aiField.x, aiField.y + cellSize * i, aiField.x + aiField.width, aiField.y + cellSize * i);
            g2d.drawLine(userField.x + cellSize * i, userField.y, userField.x + cellSize * i, userField.y + userField.height);
            g2d.drawLine(userField.x, userField.y + cellSize * i, userField.x + userField.width, userField.y + cellSize * i);
        }
        
        g2d.setStroke(mainBorderStroke);
        g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
        g2d.drawRect(aiField.x, aiField.y, aiField.width, aiField.height);
        g2d.drawRect(userField.x, userField.y, userField.width, userField.height);
        
        Image shipSprite;
        int[] data;
        g2d.translate(userField.x, userField.y);
        for(Ship ship : userShips) {
            if(ship.isBroken())
                shipSprite = Resources.brokenShips[ship.getSize() - 1];
            else
                shipSprite = Resources.wholeShips[ship.getSize() - 1];
            
            data = ship.toInternalFormat();
            
            if(data[2] == 0) {
                g2d.drawImage(shipSprite, data[0] * cellSize, data[1] * cellSize, data[3] * cellSize, cellSize, this);
            } else {
                g2d.rotate(Math.toRadians(90));
                g2d.drawImage(shipSprite, data[1] * cellSize, (-data[0] - 1) * cellSize, data[3] * cellSize, cellSize, this);
                g2d.rotate(Math.toRadians(-90));
            }
            
            Point[] brokenParts = ship.getBrokenPoints();
            if(brokenParts.length != 0) {
                for(Point point : brokenParts) {
                    g2d.drawImage(Resources.flame, point.x * cellSize, point.y * cellSize, cellSize, cellSize, this);
                }
            }
            
        }
        
        for(Point attack : aiAttacks) {
            if(attack.type >= 1 || attack.x < 0 || attack.y < 0 || attack.x > 9 || attack.y > 9) {
                continue;
            }
            
            g2d.drawImage(Resources.missSignal, attack.x * cellSize, attack.y * cellSize, cellSize, cellSize, this);
        }
        
        g2d.translate(-userField.x, -userField.y);
        
        g2d.translate(aiField.x, aiField.y);
        for(Ship ship : myApp.getCurrentAI().getShips()) {
            if(ship.isBroken()) {
                shipSprite = Resources.brokenShips[ship.getSize() - 1];
                data = ship.toInternalFormat();
                if(data[2] == 0) {
                    g2d.drawImage(shipSprite, data[0] * cellSize, data[1] * cellSize, data[3] * cellSize, cellSize, this);
                } else {
                    g2d.rotate(Math.toRadians(90));
                    g2d.drawImage(shipSprite, data[1] * cellSize, (-data[0] - 1) * cellSize, data[3] * cellSize, cellSize, this);
                    g2d.rotate(Math.toRadians(-90));
                }
            }
            
            Point[] brokenParts = ship.getBrokenPoints();
            if(brokenParts.length != 0) {
                for(Point point : brokenParts) {
                    g2d.drawImage(Resources.flame, point.x * cellSize, point.y * cellSize, cellSize, cellSize, this);
                }
            }
        }
        
        for(Point attack : userAttacks) {
            if(attack.type >= 1 || attack.x < 0 || attack.y < 0 || attack.x > 9 || attack.y > 9) {
                continue;
            }
            
            g2d.drawImage(Resources.missSignal, attack.x * cellSize, attack.y * cellSize, cellSize, cellSize, this);
        }
        
        g2d.translate(-aiField.x, -aiField.y);
        
        g2d.drawString(isUserStep ? "USER" : "AI", 20, 20);
        
    }
    
    private String countTimer() {
        long gameTime = (isGameFinished ? stopTime : System.currentTimeMillis()) - startTime - lessTime;
        return myApp.convertTimeToString(gameTime);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if(ignoreCallback || isGameFinished) {
            ignoreCallback = false;
            return;
        }
        
        int x = event.getX(), y = event.getY();
        if(isUserStep && x > aiField.x && y > aiField.y && x < aiField.x + aiField.width && y < aiField.y + aiField.height) {
            stepCount++;
            x -= aiField.x;
            y -= aiField.y;
            
            x = Math.floorDiv(x, aiField.width / 10);
            y = Math.floorDiv(y, aiField.width / 10);
            
            int r = myApp.getCurrentAI().takeAttack(x, y);
            
            Point p = new Point(x, y);
            if(!userAttacks.contains(p)) {
                p.type = r;
                userAttacks.add(p);
                
                if(r == 1) {
                    isUserStep = true;
                } else {
                    isUserStep = false;
                }
            }
            
            if(r == 2) {
                isUserStep = true;
                Ship ship = myApp.getCurrentAI().getLastSunkedShip();
                Rectangle zone = ship.getZone();
                
                Point point;
                for(int i = 0; i < zone.width; i++) {
                    point = new Point(zone.x + i, zone.y);
                    if(!userAttacks.contains(point))
                        userAttacks.add(point);
                    point = new Point(zone.x + i, zone.y + zone.height - 1);
                    if(!userAttacks.contains(point))
                        userAttacks.add(point);
                }
                
                for(int i = 0; i < zone.height; i++) {
                    point = new Point(zone.x, zone.y + i);
                    if(!userAttacks.contains(point))
                        userAttacks.add(point);
                    point = new Point(zone.x + zone.width - 1, zone.y + i);
                    if(!userAttacks.contains(point))
                        userAttacks.add(point);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(pauseTime == -1 || isGameFinished)
            return;
        
        continueBattle();
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) { 
        if(isGameFinished)
            return;
        
        pauseBattle();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if(x > exit.x && y > exit.y && x < exit.x + exit.width && y < exit.y + exit.height) {
            isExitPressed = true;
        } else if(startTime == -1 && x > start.x && y > start.y && x < start.x + start.width && y < start.y + start.height) {
            isStartPressed = true;
            ignoreCallback = true;
        } else if(x > surrender.x && y > surrender.y && x < surrender.x + surrender.width && y < surrender.y + surrender.height) {
            isSurrenderPressed = true;
        }
        
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if(x > exit.x && y > exit.y && x < exit.x + exit.width && y < exit.y + exit.height) {
            isExitPressed = false;
            if(startTime == -1)
                myApp.showFieldMappingScreen();
            else
                myApp.exit();
        } else if(startTime == -1 && x > start.x && y > start.y && x < start.x + start.width && y < start.y + start.height) {
            isStartPressed = false;
            startBattle();
        } else if(x > surrender.x && y > surrender.y && x < surrender.x + surrender.width && y < surrender.y + surrender.height) {
            isSurrenderPressed = false;
            isUserWin = false;
            stopBattle();
        }
        
        repaint();
    }
}

