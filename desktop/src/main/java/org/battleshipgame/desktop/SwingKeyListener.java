package org.battleshipgame.desktop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.battleshipgame.render.Screen;

public class SwingKeyListener implements KeyListener {
	private Object mutex = new Object();
	private Screen screen;
	private JFrame frame;
	
	public SwingKeyListener(JFrame frame) {
		this.frame = frame;
		frame.addKeyListener(this);
	}
	
	public void setScreen(Screen screen) {
		synchronized(mutex) {
			this.screen = screen;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		synchronized(mutex) {
			if(screen.onKeyPress((int)e.getKeyChar())) {
				frame.repaint();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

}
