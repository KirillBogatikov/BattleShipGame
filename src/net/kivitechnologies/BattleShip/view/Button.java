package net.kivitechnologies.BattleShip.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
 
public class Button {
    private String label;
    private boolean clicked;
    private Rectangle bounds;
    private Stroke myStroke;
    
    public Button(String label) {
        this.label = label;
        this.bounds = new Rectangle();
        this.myStroke = new BasicStroke(2.75f);
    }
    
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public void paint(Graphics2D g) {
        Color lastColor = g.getColor();
        Stroke lastStroke = g.getStroke();
        
        g.setColor(clicked ? Theme.BACKGROUND_COLOR_PRESSED : Theme.BACKGROUND_COLOR_DEFAULT);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, bounds.width / 6, bounds.width / 6);
        g.setColor(clicked ? Theme.BORDER_COLOR_PRESSED : Theme.BORDER_COLOR_DEFAULT);
        g.setStroke(myStroke);
        g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, bounds.width / 6, bounds.width / 6);
        
        FontMetrics metrics = g.getFontMetrics();
        int x = (bounds.width - metrics.stringWidth(label)) / 2;
        int y = bounds.height - (metrics.getAscent() + metrics.getDescent()) / 2;
        g.drawString(label, bounds.x + x, bounds.y + y);
        
        g.setColor(lastColor);
        g.setStroke(lastStroke);
    }
    
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
