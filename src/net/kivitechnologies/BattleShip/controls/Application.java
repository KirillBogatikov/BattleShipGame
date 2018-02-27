package net.kivitechnologies.BattleShip.controls;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.kivitechnologies.BattleShip.view.BattleScreen;
import net.kivitechnologies.BattleShip.view.FieldMappingScreen;
import net.kivitechnologies.BattleShip.view.MainMenuScreen;
import net.kivitechnologies.BattleShip.view.ResultScreen;
import net.kivitechnologies.BattleShip.view.Theme;

public class Application { 
    private JFrame frame;
    private JPanel mainMenuScreen, fieldMappingScreen, battleScreen, resultsScreen;
    private File savingsFile;
    private AI artifactIntel;
    
    public Application(JFrame frame) {
        this.frame = frame;
        
        Resources.initiate();
        savingsFile = new File(System.getProperty("user.dir", "saves.json"));
        artifactIntel = new AI();   
    }
    
    public AI getCurrentAI() {
        return artifactIntel;
    }
    
    public void showMainMenuScreen() {
        if(mainMenuScreen == null)
           mainMenuScreen = new MainMenuScreen(this);
        
        showScreen(mainMenuScreen);
    }
    
    public void showFieldMappingScreen() {
        if(fieldMappingScreen == null)
            fieldMappingScreen = new FieldMappingScreen(this);
         
        showScreen(fieldMappingScreen);
    }
    
    public void showBattleScreen(ArrayList<Ship> ships) {
        if(battleScreen == null)
            battleScreen = new BattleScreen(this);
        
        ((BattleScreen)battleScreen).applyShips(ships);
        showScreen(battleScreen);
    }
    
    public void showResults(long gameTime, int stepCount, boolean isUserWin) {
        if(resultsScreen == null)
            resultsScreen = new ResultScreen(this);
        
        ((ResultScreen)resultsScreen).applyInitialData(gameTime, stepCount, isUserWin);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize(); 
        JFrame frame = new JFrame("BattleShip - Result");
        frame.setBounds(screen.width / 3, screen.height / 3, screen.width / 3, screen.height / 3);
        frame.setBackground(Theme.SCREEN_BACKGROUND_COLOR);
        frame.setContentPane(resultsScreen);
        frame.setVisible(true);
    }
    
    public String convertTimeToString(long gameTime) {
        long h = gameTime / 60 / 60 / 1000;
        gameTime -= h * 60 * 60 * 1000;
        long m = gameTime / 60 / 1000;
        gameTime -= m * 60 * 1000;
        long s = (gameTime - m * 60 / 1000) / 1000;
        
        StringBuilder formatter = new StringBuilder();
        if(h < 10)
            formatter.append("0");
        formatter.append("%d:");
        if(m < 10)
            formatter.append("0");
        formatter.append("%d:");
        if(s < 10)
            formatter.append("0");
        formatter.append("%d");
        return String.format(formatter.toString(), h, m, s);
    }
    
    private void showScreen(JPanel screen) {
        dismissWindow();
        this.frame.setContentPane(screen);
        showWindow();
    }
    
    public void showWindow() {
        if(frame.isVisible())
            return;
        
        frame.setVisible(true);
    }
    
    public void dismissWindow() {
        if(!frame.isVisible())
            return;
        
        frame.setVisible(false);
    }
    
    public void exit() {
        System.exit(0);
    }
    
    public void startNewGame() {
        showFieldMappingScreen();
    }
    
    public void startLastGame() {
        
    }
    
    public boolean hasLastGame() {
        return !savingsFile.exists();
    }
    
    public void saveGame(ArrayList<Ship> ai, ArrayList<Ship> user) {
        
    }
    
    public void surrenderBattle(long gameTime, int stepCount) {
        showResults(gameTime, stepCount, false);
    }
}
