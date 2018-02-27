package net.kivitechnologies.BattleShip.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel {
    protected Dimension lastSize;
    
    public Screen() {
        super();
        lastSize = new Dimension(0, 0);
        setBackground(Theme.TRANSPARENT);
    }
    
    public void paint(Graphics g) {
        if(lastSize.width != getWidth() || lastSize.height != getHeight())
            calculate();
    }
    
    public void calculate() {
        lastSize.width = getWidth();
        lastSize.height = getHeight();
    }
}
