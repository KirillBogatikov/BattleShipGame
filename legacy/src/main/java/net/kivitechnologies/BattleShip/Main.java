package net.kivitechnologies.BattleShip;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import net.kivitechnologies.BattleShip.controls.Application;
import net.kivitechnologies.BattleShip.view.Theme;

public class Main {

    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize(); 
        JFrame frame = new JFrame("BattleShip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(screen.width / 4, screen.height / 4, screen.width / 2, screen.height / 2);
        frame.setBackground(Theme.SCREEN_BACKGROUND_COLOR);
        Application app = new Application(frame);
        app.showMainMenuScreen();
    }

}
