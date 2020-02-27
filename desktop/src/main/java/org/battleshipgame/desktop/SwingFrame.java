package org.battleshipgame.desktop;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import org.battleshipgame.render.Screen;

public class SwingFrame extends JFrame {
	private static final long serialVersionUID = -2746195633645150601L;

	private SwingRenderer renderer;
	private Object mutex = new Object();
	private Screen screen;
	
	public SwingFrame(SwingRenderer renderer) {
		this.renderer = renderer;
	}
	
	public void setScreen(Screen screen) {
		synchronized(mutex) {
			this.screen = screen;
		}
	}
	
	public void paint(Graphics g) {
		renderer.use((Graphics2D)g);
		synchronized(mutex) {
			if(screen != null) {
				screen.render();
			}
		}
	}
}
