package org.battleshipgame.desktop;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;

import org.battleshipgame.core.GameEngine;
import org.battleshipgame.core.RemotePlayerListener;
import org.battleshipgame.impl.ShipDockImpl;
import org.battleshipgame.network.GameId;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.Screen;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.ui.DesktopConnectionScreen;
import org.battleshipgame.ui.DesktopGameModeScreen;
import org.battleshipgame.ui.DesktopGameScreen;
import org.battleshipgame.ui.DesktopMapScreen;
import org.battleshipgame.ui.DesktopResultScreen;
import org.battleshipgame.ui.DesktopStartScreen;
import org.cuba.log.Configurator;
import org.cuba.log.Log;

public class Launcher implements RemotePlayerListener {
	private Log log;
	
	private SwingFrame frame;
	private StylesResolver styles;
	private SwingRenderer renderer;
	private SwingMouseListener mouseListener;
	private SwingKeyListener keyListener;
	private Image backImage, ringImage;
	
	private ShipDockImpl shipsDock = null;
	private GameEngine gameEngine = null;
	private DesktopGameScreen gameScreen = null;
	
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
		ringImage = loadImage("ring.png");
		
		shipsDock = new ShipDockImpl(frame::repaint);
		gameEngine = new GameEngine(log);
	}
	
	private void showStartScreen() {
		DesktopStartScreen startScreen = new DesktopStartScreen(styles, renderer);
		startScreen.setClickListeners(this::showChooseModeScreen, () -> {}, () -> {});	
		setScreen(startScreen);
	}

	private void showChooseModeScreen() {
		DesktopGameModeScreen modeScreen = new DesktopGameModeScreen(backImage, styles, renderer);
		modeScreen.setClickListeners(() -> {
			gameEngine.startAI();
			showMapScreen(false);
		}, this::showConnectionScreen, this::showStartScreen);
		setScreen(modeScreen);
	}
	
	private void showConnectionScreen() {
		DesktopConnectionScreen connScreen = new DesktopConnectionScreen(backImage, styles, renderer);
		connScreen.setClickListeners(() -> {
			gameEngine.startMultiPlayer(this);
			gameEngine.connectToFriend(connScreen.inputs()[0].text(), connScreen.inputs()[1].text());
		}, () -> {
			gameEngine.startMultiPlayer(this);
			
			GameId gameId = gameEngine.gameId();
			connScreen.inputs()[0].text_$eq(gameId.hash());
			connScreen.inputs()[1].text_$eq(gameId.connection());
			
			connScreen.setLoading(true);
		}, this::showChooseModeScreen);
		setScreen(connScreen);
	}
	
	private void showMapScreen(boolean online) {
		DesktopMapScreen mapScreen = new DesktopMapScreen(backImage, loadImage("rotate.png"), shipsDock, styles, renderer);
		mapScreen.setClickListeners(() -> {
			gameEngine.user().bay().ships_$eq(shipsDock.placed());
			showGameScreen(online);
		}, online ? this::showConnectionScreen : this::showChooseModeScreen, frame::repaint);
		setScreen(mapScreen);
	}
	
	private void showGameScreen(boolean online) {
		gameScreen = new DesktopGameScreen(backImage, ringImage, gameEngine, styles, renderer);
		gameScreen.setClickListeners(this::onGameLose);
		setScreen(gameScreen);
		
		Thread t = new Thread(() -> {
			while(!gameScreen.disposed()) {
				try {
					frame.repaint();
					Thread.sleep(100L);

					if (!online && !gameEngine.friend().hasWholeShips()) {
						onGameWin();
					} else if(!gameEngine.user().hasWholeShips()) {
						onGameLose();
					}
					
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
	
	private void showResultScreen(boolean win) {
		gameScreen.disposed(true);
		DesktopResultScreen screen = new DesktopResultScreen(win, gameScreen.minutes(), gameScreen.seconds(), styles, renderer, () -> {
			showStartScreen();
			shipsDock = new ShipDockImpl(frame::repaint);
			gameEngine = new GameEngine(log);
			gameScreen = null;
		});
		setScreen(screen);
	}

	@Override
	public void onGameWin() {
		log.d("Desktop", "You win!");
		showResultScreen(true);
	}

	@Override
	public void onGameLose() {
		log.d("Desktop", "You lose!");
		showResultScreen(false);
	}

	@Override
	public void onFriendConnected() {
		log.d("Desktop", "Friend connected!");
		showMapScreen(true);
	}

	@Override
	public void onConnectedToFriend() {
		log.d("Desktop", "Connected to friend!");
		showMapScreen(true);
	}
}
