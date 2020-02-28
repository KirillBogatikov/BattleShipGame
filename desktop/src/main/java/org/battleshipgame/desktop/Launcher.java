package org.battleshipgame.desktop;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;

import org.battleshipgame.render.Image;
import org.battleshipgame.render.Screen;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.ui.DesktopConnectionScreen;
import org.battleshipgame.ui.DesktopGameModeScreen;
import org.battleshipgame.ui.DesktopStartScreen;

public class Launcher  {
	private SwingFrame frame;
	private StylesResolver styles;
	private SwingRenderer renderer;
	private SwingMouseListener mouseListener;
	private SwingKeyListener keyListener;
	private Image backImage;
	
	public Launcher() throws IOException {		
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
	}
	
	private void showStartScreen() {
		DesktopStartScreen startScreen = new DesktopStartScreen(styles, renderer);
		startScreen.setClickListeners(this::showChooseModeScreen, System.out::println, System.out::println);	
		setScreen(startScreen);
	}

	private void showChooseModeScreen() {
		DesktopGameModeScreen modeScreen = new DesktopGameModeScreen(backImage, styles, renderer);
		modeScreen.setClickListeners(this::showSinglePlayScreen, this::showConnectionScreen, this::showStartScreen);
		setScreen(modeScreen);
	}
	
	private void showConnectionScreen() {
		DesktopConnectionScreen connScreen = new DesktopConnectionScreen(backImage, styles, renderer);
		connScreen.setClickListeners(() -> {
			//TODO connect
		}, () -> {
			//TODO create
		}, this::showChooseModeScreen);
		setScreen(connScreen);
	}
	
	private void showSinglePlayScreen() {
		
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
