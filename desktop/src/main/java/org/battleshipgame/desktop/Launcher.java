package org.battleshipgame.desktop;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;

import org.battleshipgame.core.CoreListener;
import org.battleshipgame.core.GameEngine;
import org.battleshipgame.impl.ShipDockImpl;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.Screen;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.ui.DesktopConnectionScreen;
import org.battleshipgame.ui.DesktopGameModeScreen;
import org.battleshipgame.ui.DesktopGameScreen;
import org.battleshipgame.ui.DesktopMapScreen;
import org.battleshipgame.ui.DesktopStartScreen;
import org.battleshipgame.ui.Ship;
import org.cuba.log.Configurator;
import org.cuba.log.Log;

public class Launcher  {
	private Log log;
	
	private SwingFrame frame;
	private StylesResolver styles;
	private SwingRenderer renderer;
	private SwingMouseListener mouseListener;
	private SwingKeyListener keyListener;
	private Image backImage;
	
	private ShipDockImpl shipsDock;
	private GameEngine gameEngine = null;
	
	public Launcher() throws IOException {
		log = new Log(Configurator.system().build());
		
		URL fontPath = Launcher.class.getClassLoader().getResource("fonts/Roboto.ttf");
		Font applicationFont;
        try {
            applicationFont = Font.createFont(Font.TRUETYPE_FONT, fontPath.openStream());
        } catch (Exception e) {
            return;
        }
        
        styles = new DesktopStylesResolver(loadImage("background.png"), loadImage("flame.png"), loadImage("wreck.png"), loadImage("miss.png"), 
    		new SwingImage[] { loadImage("ships/warship.png"), loadImage("ships/cruiser.png"), loadImage("ships/destroyer.png"), loadImage("ships/torpedo.png") });
		renderer = new SwingRenderer(applicationFont);
		
		frame = new SwingFrame(renderer);
		frame.setBounds(300, 300, 960, 540);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mouseListener = new SwingMouseListener(frame);
		keyListener = new SwingKeyListener(frame);
		
		backImage = loadImage("arrow_back.png");
		
		shipsDock = new ShipDockImpl(frame::repaint);
	}
	
	private void showStartScreen() {
		DesktopStartScreen startScreen = new DesktopStartScreen(styles, renderer);
		startScreen.setClickListeners(this::showChooseModeScreen, System.out::println, System.out::println);	
		setScreen(startScreen);
	}

	private void showChooseModeScreen() {
		DesktopGameModeScreen modeScreen = new DesktopGameModeScreen(backImage, styles, renderer);
		modeScreen.setClickListeners(() -> {
			initGame();
			gameEngine.skynet();
			showMapScreen(false);
		}, this::showConnectionScreen, this::showStartScreen);
		setScreen(modeScreen);
	}
	
	private void showConnectionScreen() {
		DesktopConnectionScreen connScreen = new DesktopConnectionScreen(backImage, styles, renderer);
		connScreen.setClickListeners(() -> {
			initGame();
			gameEngine.connect(connScreen.gameId().text());
		}, () -> {
			initGame();
			connScreen.gameId().text_$eq(gameEngine.gameId());
			connScreen.setLoading(true);
		}, this::showChooseModeScreen);
		setScreen(connScreen);
	}
	
	private void showMapScreen(boolean online) {
		DesktopMapScreen mapScreen = new DesktopMapScreen(backImage, loadImage("rotate.png"), shipsDock, styles, renderer);
		mapScreen.setClickListeners(() -> {
			gameEngine.playerBay().ships_$eq(shipsDock.placed());
			showGameScreen();
		}, online ? this::showConnectionScreen : this::showChooseModeScreen, frame::repaint);
		setScreen(mapScreen);
	}
	
	private void initGame() {
		gameEngine = new GameEngine(new Ship[0], log, new CoreListener() {
			public void onConnected(boolean client) {
				showMapScreen(true);
			}
			
		    public void onGameEnd(boolean win) {
		    	
		    }
		});
	}
	
	private void showGameScreen() {
		DesktopGameScreen gameScreen = new DesktopGameScreen(backImage, gameEngine, styles, renderer);
		gameScreen.setClickListeners(() -> {
			System.out.println("YOU LOSE");
			System.exit(0);
		});
		setScreen(gameScreen);
		
		Thread t = new Thread(() -> {
			while(true) {
				try {
					frame.repaint();
					Thread.sleep(100L);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	
	private void setScreen(Screen screen) {
		frame.setScreen(screen);
		mouseListener.setScreen(screen);	
		keyListener.setScreen(screen);
		if(!frame.isVisible()) {
			frame.setVisible(true);
		}
	}
	
	private SwingImage loadImage(String path) {
		try(InputStream stream = Launcher.class.getClassLoader().getResourceAsStream(path)) {
			return new SwingImage(stream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Launcher launcher = new Launcher();
		launcher.showStartScreen();
	}
}
