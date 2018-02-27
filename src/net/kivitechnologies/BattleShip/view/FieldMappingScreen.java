package net.kivitechnologies.BattleShip.view;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import net.kivitechnologies.BattleShip.controls.Application;
import net.kivitechnologies.BattleShip.controls.Resources;
import net.kivitechnologies.BattleShip.controls.Ship;
 
@SuppressWarnings("serial")
public class FieldMappingScreen extends Screen implements MouseListener, MouseMotionListener  {
    private Rectangle container, field, currentCell, backward, forward, reset; 
    private ArrayList<Rectangle> ships = new ArrayList<Rectangle>(10);
    private Image[] shipz;
    private Rectangle[] shipsShapes; 
    private int m = 0, size = 0;
    private boolean backwardPressed = false, forwardPressed = false, resetPressed = false;
    private int cellSize;
    private int[] mappedShipsCount;
    private Application myApp;
    
    public FieldMappingScreen(Application application) {
        super();
        myApp = application;
        
        currentCell = new Rectangle();
        container = new Rectangle();
        field = new Rectangle();
        backward = new Rectangle();
        forward = new Rectangle();
        reset = new Rectangle();
        shipz = Resources.wholeShips;
        
        addMouseListener(this);
        addMouseMotionListener(this);
        shipsShapes = new Rectangle[4];
        
        for(int i = 0; i < 4; i++) {
            shipsShapes[i] = new Rectangle();
        }
        
        mappedShipsCount = new int[]{ 0, 0, 0, 0 };
    }
    
    public void reset() {
        for(int i = 0; i < 4; i++) {
            shipsShapes[i] = new Rectangle();
        }
        
        mappedShipsCount = new int[]{ 0, 0, 0, 0 };
        ships.clear();
        calculate();
        repaint();
    }
    
    public void calculate() {
        super.calculate();
        int width = getWidth(), height = getHeight();
        
        int padding = width / 50;
        int size = (int)((width / 2 - padding * 2) / 10) * 10;
        container.width = container.height = size;
        container.x = padding;
        container.y = (height - size) / 2;
        
        field.width = field.height = size;
        field.x = width / 2 + padding;
        field.y = container.y;
        
        cellSize = field.width / 10;
        
        for(int i = 0; i < 4; i++) {
            Rectangle shape = shipsShapes[i];
            shape.x = container.width / 10;
            shape.y = container.height / 10 * (i + 1) + (container.width * 2 / 15 * i);
            shape.width = container.width * 2 / 15 * (4 - i);
            shape.height = container.width * 2 / 15;
        }
        
        int btnwdth = width / 2;
        int btnhght = (height - size) / 2; 
        backward.x = btnwdth / 10;
        backward.y = forward.y = reset.y = height - btnhght * 4 / 5; 
        backward.width = forward.width = reset.width = btnwdth * 4 / 9;
        backward.height = forward.height = reset.height = btnhght * 3 / 5;
        
        forward.x = width - forward.width - btnwdth / 10;
        
        reset.x = (width - reset.width) / 2;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
        int width = getWidth();
        g2d.clearRect(0, 0, width, getHeight());
        g2d.setFont(Resources.applicationFont.deriveFont(width / 30.0f));
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Theme.BACKGROUND_COLOR_DEFAULT);
        g2d.fillRect(container.x, container.y, container.width, container.height);
        g2d.fillRect(field.x, field.y, field.width, field.height);
        
        if(m == 1 && currentCell.x + size > 10) {
            currentCell.x = 10 - size;
        }
        
        if(m == 0 && currentCell.y + size > 10) {
            currentCell.y = 10 - size;
        }
           
        g2d.setColor(Theme.CELLS_BACKGROUND_COLOR);
        g2d.fillRect(field.x + currentCell.x * (field.width / 10), field.y + currentCell.y * (field.height / 10), field.width / 10, field.height / 10);
        
        g2d.setStroke(new BasicStroke(0.75f));
        FontMetrics fm = g2d.getFontMetrics();
        
        for(int i = 1; i <= 10; i++) {
            g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
            g2d.drawString(i+"", field.x + cellSize * (i - .5f) - fm.stringWidth(i+"") / 2, field.y - fm.getHeight() / 5);
            String sym = new String(Character.toChars(64 + i));
            g2d.drawString(sym, field.x - fm.stringWidth(sym) * 3 / 2, field.y + cellSize * (i) - fm.getHeight() / 5);
            if(i == 10) break;
            
            g2d.setColor(Theme.CELLS_BORDER_COLOR);
            g2d.drawLine(field.x + cellSize * i, field.y, field.x + cellSize * i, field.y + field.height);
            g2d.drawLine(field.x, field.y + cellSize * i, field.x + field.width, field.y + cellSize * i);
        }
        
        g2d.translate(field.x, field.y);
        for(Rectangle rect : ships) {
            if(rect.width < rect.height) {
                g2d.rotate(Math.toRadians(90));
                g2d.drawImage(shipz[Math.max(rect.width, rect.height) - 1], rect.y * cellSize, (-rect.x - 1) * cellSize, Math.max(rect.width, rect.height) * cellSize, Math.min(rect.width, rect.height) * cellSize, this);
                g2d.rotate(Math.toRadians(-90));
            } else 
                g2d.drawImage(shipz[Math.max(rect.width, rect.height) - 1], rect.x * cellSize, rect.y * cellSize, Math.max(rect.width, rect.height) * cellSize, Math.min(rect.width, rect.height) * cellSize, this);
            
        }
        
        if(size < 5 && size > 0) {
            if(m == 0) {
                g2d.rotate(Math.toRadians(90));
                g2d.drawImage(shipz[size - 1], currentCell.y * cellSize, (-currentCell.x - 1) * cellSize, size * cellSize, cellSize, this);
                g2d.rotate(Math.toRadians(-90));
            } else 
                g2d.drawImage(shipz[size - 1], currentCell.x * cellSize, currentCell.y * cellSize, size * cellSize, cellSize, this);
        }
        
        g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(0, 0,  field.width, field.height);
        g2d.translate(-field.x,  -field.y);
        
        g2d.drawRect(container.x,  container.y,  container.width, container.height);
        
        String count;
        for(int i = 0; i < 4; i++) {
            if(mappedShipsCount[i] > i) continue;
            g2d.drawImage(shipz[3 - i], container.x + shipsShapes[i].x, container.y + shipsShapes[i].y, shipsShapes[i].width, shipsShapes[i].height, this);
            count = "x" + (i + 1 - mappedShipsCount[i]);
            g2d.drawString(count, container.x + container.width - fm.stringWidth(count) * 3 / 2, container.y + shipsShapes[i].y + fm.getHeight());
        }
        
        g2d.setColor(backwardPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
        g2d.fillRoundRect(backward.x, backward.y, backward.width, backward.height, backward.height, backward.height);
        
        if(ships.size() == 10) {
            g2d.setColor(forwardPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
            g2d.fillRoundRect(forward.x, forward.y, forward.width, forward.height, forward.height, forward.height);
        }
        
        g2d.setColor(resetPressed ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
        g2d.fillRoundRect(reset.x, reset.y, reset.width, reset.height, reset.height, reset.height);
        
        g2d.setColor(backwardPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
        g2d.drawRoundRect(backward.x, backward.y, backward.width, backward.height, backward.height, backward.height);
        
        if(ships.size() == 10) {
            g2d.setColor(forwardPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
            g2d.drawRoundRect(forward.x, forward.y, forward.width, forward.height, forward.height, forward.height);
        }
        
        g2d.setColor(resetPressed ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
        g2d.drawRoundRect(reset.x, reset.y, reset.width, reset.height, reset.height, reset.height);
        
        g2d.drawString("Назад", backward.x + (backward.width - fm.stringWidth("Назад")) / 2, backward.y + backward.height - fm.getHeight() / 2);
        
        if(ships.size() == 10) {
            g2d.drawString("Далее", forward.x + (forward.width - fm.stringWidth("Далее")) / 2, forward.y + forward.height - fm.getHeight() / 2);
        }
        
        g2d.drawString("Сброс", reset.x + (reset.width - fm.stringWidth("Сброс")) / 2, reset.y + reset.height - fm.getHeight() / 2);
    }
    
    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if(size != 0 && x > field.x && x < field.x + field.width && y > field.y && y < field.y + field.height) {
            x -= field.x;
            y -= field.y;
            int btn = event.getButton();
            if(btn == MouseEvent.BUTTON1) {
                for(Rectangle exists : ships) {
                    if(exists.intersects(currentCell.x - 1, currentCell.y - 1, (m == 0 ? 1 : size) + 2, (m == 0 ? size : 1) + 2))
                        return;
                }
                ships.add(new Rectangle(currentCell.x, currentCell.y, m == 1 ? size : 1, m == 1 ? 1 : size));
                mappedShipsCount[4 - size]++;
                if(mappedShipsCount[4 - size] > 4 - size) {
                    size = 0;
                }
            } else {
                m = m == 0 ? 1 : 0; 
            }
        
            repaint();
         } else if(x > container.x && x < container.x + container.width && y > container.y && y < container.y + container.height) {
             x -= container.x;
             y -= container.y;
             for(int i = 0; i < 4; i++) {
                 Rectangle shipShape = shipsShapes[i];
                 
                 if(x > shipShape.x && x < shipShape.x + shipShape.width && y > shipShape.y && y < shipShape.y + shipShape.height) {
                     size = 4 - i;
                     break;
                 }
             }
             
             repaint();
         }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if(x > backward.x && y > backward.y && x < backward.x + backward.width && y < backward.y + backward.height) {
            backwardPressed = true;
        } else if(ships.size() == 10 && x > forward.x && y > forward.y && x < forward.x + forward.width && y < forward.y + forward.height) {
            forwardPressed = true;
        } else if(x > reset.x && y > reset.y && x < reset.x + reset.width && y < reset.y + reset.height) {
            resetPressed = true;
        } else return;
        
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if(x > backward.x && y > backward.y && x < backward.x + backward.width && y < backward.y + backward.height) {
            backwardPressed = false;
            myApp.showMainMenuScreen();
        } else if(ships.size() == 10 && x > forward.x && y > forward.y && x < forward.x + forward.width && y < forward.y + forward.height) {
            forwardPressed = false;
            ArrayList<Ship> shipsPrepared = new ArrayList<Ship>(10);
            for(Rectangle rect : ships) {
                shipsPrepared.add(new Ship(rect.x, rect.y, rect.width > rect.height ? 0 : 1, Math.max(rect.width, rect.height)));
            }
            myApp.showBattleScreen(shipsPrepared);
        } else if(x > reset.x && y > reset.y && x < reset.x + reset.width && y < reset.y + reset.height) {
            resetPressed = false;
            reset();
        } else return;
        
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {}

    @Override
    public void mouseMoved(MouseEvent event) {
        int x = event.getX() - field.x, y = event.getY() - field.y;
        if(x > 0 && x < field.width && y > 0 && y < field.height) {
            currentCell.x = (x / cellSize);
            currentCell.y = (y / cellSize);
                 
            repaint();
        }
    }
}
