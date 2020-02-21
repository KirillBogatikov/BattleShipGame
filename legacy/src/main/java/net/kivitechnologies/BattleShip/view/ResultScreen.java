package net.kivitechnologies.BattleShip.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import net.kivitechnologies.BattleShip.controls.Application;
import net.kivitechnologies.BattleShip.controls.Resources;
 
@SuppressWarnings("serial")
public class ResultScreen extends Screen {
    private Application myApp;
    private long gameTime;
    private int stepCount;
    private boolean isUserWin;
    
    public ResultScreen(Application app) {
        super();
        myApp = app;
    }
    
    public void applyInitialData(long gT, int sC, boolean iUW) {
        this.gameTime = gT;
        this.stepCount = sC;
        this.isUserWin = iUW;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2d.setFont(Resources.applicationFont.deriveFont(getWidth() / 5.0f));
        g2d.setColor(Theme.BORDER_COLOR_DEFAULT);
        
        String time = myApp.convertTimeToString(gameTime);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(time, (getWidth() - fm.stringWidth(time)) / 2, (getHeight() - fm.getHeight()) / 2);
        
        g2d.setFont(Resources.applicationFont.deriveFont(getWidth() / 15.0f));
        fm = g2d.getFontMetrics();
        String message = String.format("Вы %s за %d ходов!", isUserWin ? "победили" : "проиграли", stepCount);
        g2d.drawString(message, (getWidth() - fm.stringWidth(message)) / 2, getHeight() - fm.getHeight() * 3 / 2);
    }
}
