package net.kivitechnologies.BattleShip.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import net.kivitechnologies.BattleShip.controls.Application;
import net.kivitechnologies.BattleShip.controls.Resources;
 
@SuppressWarnings("serial")
public class MainMenuScreen extends Screen implements MouseListener {
    private Button[] buttons;
    private Application application;
    private int clickedButtonIndex = -1;
    private Rectangle logoBounds;
    
    public MainMenuScreen(Application app) {
        super();
        this.application = app;
        
        buttons = new Button[]{
                new Button("Новая игра"),
                new Button("Выход")
        };
        logoBounds = new Rectangle();
        
        addMouseListener(this);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
       
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(Resources.applicationFont.deriveFont(getWidth() / 25.0f));
        
        for(Button button : buttons) {
            button.paint(g2d);
        }
        
        g2d.drawImage(Resources.startScreenLogo, logoBounds.x, logoBounds.y, logoBounds.width, logoBounds.height, this);
    }
    
    public void calculate() {
        super.calculate();
        int padding = lastSize.width / 30;
        int button_width = lastSize.width / 2 - padding * 2;
        int button_height = lastSize.height / 8;
        int def_x = lastSize.width / 2 + padding;
        int def_y = (lastSize.height - (button_height + padding) * 3) / 2 + padding / 2;
        
        for(int i = 0; i < buttons.length; i++) {
            Rectangle currentBounds = buttons[i].getBounds();
            currentBounds.x = def_x;
            currentBounds.y = def_y + i * (button_height + padding); 
            currentBounds.width = button_width;
            currentBounds.height = button_height;
            buttons[i].setBounds(currentBounds);
        }
        
        logoBounds.x = padding;
        logoBounds.width = getWidth() / 2 - padding * 2;
        logoBounds.height = (Resources.startScreenLogo.getHeight(this) * logoBounds.width) / Resources.startScreenLogo.getWidth(this); 
        logoBounds.y = (lastSize.height - logoBounds.height) / 2;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) { }

    @Override
    public void mouseEntered(MouseEvent arg0) { }

    @Override
    public void mouseExited(MouseEvent arg0) { }

    @Override
    public void mousePressed(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        Rectangle bounds;
        for(int i = 0; i < buttons.length; i++) {
            bounds = buttons[i].getBounds();
            if(x >= bounds.x && x <= bounds.x + bounds.width) {
                if(y >= bounds.y && y <= bounds.y + bounds.height) {
                    buttons[i].setClicked(true);
                    clickedButtonIndex = i;
                    repaint();
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if(clickedButtonIndex == -1)
            return;
        
        if(clickedButtonIndex == 0) {
            application.startNewGame();
        } else if(clickedButtonIndex == 1) {
            application.exit();
        } else
            return;
        
        buttons[clickedButtonIndex].setClicked(false);
        repaint();
    }
}
