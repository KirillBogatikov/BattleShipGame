package org.battleshipgame.desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import org.battleshipgame.render.Screen;

public class SwingMouseListener implements MouseListener, MouseMotionListener {
	private Object mutex = new Object();
	private Screen screen;
	private JFrame frame;
	
	public SwingMouseListener(JFrame frame) {
		this.frame = frame;
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
	}
	
	public void setScreen(Screen screen) {
		synchronized(mutex) {
			this.screen = screen;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		synchronized(mutex) {
			if(screen.onMouseMove(e.getX(), e.getY())) {
				frame.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized(mutex) {
			if(screen.onMouseMove(e.getX(), e.getY())) {
				frame.repaint();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		synchronized(mutex) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(screen.onClick(e.getX(), e.getY())) {
					frame.repaint();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized(mutex) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(screen.onMouseDown(e.getX(), e.getY())) {
					frame.repaint();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		synchronized(mutex) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(screen.onMouseUp(e.getX(), e.getY())) {
					frame.repaint();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
}
