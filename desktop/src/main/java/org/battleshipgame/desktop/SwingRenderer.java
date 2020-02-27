package org.battleshipgame.desktop;

import static org.battleshipgame.render.ColorUtils.alpha;
import static org.battleshipgame.render.ColorUtils.blue;
import static org.battleshipgame.render.ColorUtils.green;
import static org.battleshipgame.render.ColorUtils.red;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;

import org.battleshipgame.render.Image;
import org.battleshipgame.render.Point;
import org.battleshipgame.render.Rectangle;
import org.battleshipgame.render.Renderer;

public class SwingRenderer implements Renderer {
	private static class PaintOps {
		public Color strokeColor;
		public boolean stroke;
		public int strokeSize;

		public Color fillColor;
		public boolean fill;

		public Color textColor;
	}

	public Graphics2D g2d;
	private PaintOps origin;
	private PaintOps current;
	private Font font;

	public SwingRenderer(Font font) {
		this.font = font;
	}

	public void use(Graphics2D g2d) {
		this.g2d = g2d;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	@Override
	public void begin() {
		origin = current;
		current = new PaintOps();
	}

	@Override
	public void end() {
		current = origin;
		origin = null;
	}

	@Override
	public void stroke(boolean paint) {
		current.stroke = paint;
	}

	@Override
	public void fill(boolean paint) {
		current.fill = paint;
	}

	private Color swingColor(long color) {
		return new Color(red(color), green(color), blue(color), alpha(color));
	}

	@Override
	public void stroke(long color) {
		current.strokeColor = swingColor(color);
	}

	@Override
	public void fill(long color) {
		current.fillColor = swingColor(color);
	}

	@Override
	public void text(long color) {
		current.textColor = swingColor(color);
	}

	@Override
	public void stroke(int size) {
		current.strokeSize = size;
	}

	private void stroke() {
		g2d.setColor(current.strokeColor);
		g2d.setStroke(new BasicStroke(current.strokeSize));
	}

	private void fill() {
		g2d.setColor(current.fillColor);
	}

	@Override
	public void rectangle(Rectangle rect) {
		int radius = (int) rect.cornerRadius();
		if (current.fill) {
			fill();
			g2d.fillRoundRect(rect.x(), rect.y(), rect.width(), rect.height(), radius, radius);
		}
		if (current.stroke) {
			stroke();
			g2d.drawRoundRect(rect.x(), rect.y(), rect.width(), rect.height(), radius, radius);
		}
	}

	@Override
	public void line(Point start, Point end) {
		line(start.x(), start.y(), end.x(), end.y());
	}

	@Override
	public void line(int sx, int sy, int ex, int ey) {
		if (current.stroke) {
			stroke();
			g2d.drawLine(sx, sy, ex, ey);
		}
	}

	@Override
	public void image(Rectangle rectangle, Image image) {
		SwingImage swingImage = (SwingImage) image;
		g2d.drawImage(swingImage.getContent(), rectangle.x(), rectangle.y(),  
			rectangle.x() + rectangle.width(), rectangle.y() + rectangle.height(), 
			0, 0, swingImage.getWidth(), swingImage.getHeight(), null);
	}

	@Override
	public void text(Rectangle rectangle, String text, double size) {
		g2d.setColor(current.textColor);
		g2d.setFont(font.deriveFont((float) size));
		FontMetrics metrics = g2d.getFontMetrics();
		
		String[] lines = text.split("\n");
		
		int lineHeight = metrics.getAscent() - metrics.getDescent();
		int centerY = rectangle.y() + rectangle.height() / 2 + lineHeight / 2;
	    		
		if(lines.length == 1) {
			int x = (rectangle.width() - metrics.stringWidth(text)) / 2;
			g2d.drawString(text, rectangle.x() + x, centerY);
		} else {
			centerY -= lineHeight * (lines.length - 1);
			for(int i = 0; i < lines.length; i++) {
				text = lines[i];
				int x = (rectangle.width() - metrics.stringWidth(text)) / 2;
		    	g2d.drawString(text, rectangle.x() + x, centerY);
				centerY += lineHeight += 25;
			}
		}
	}

	@Override
	public void shadow(Rectangle rect) {
		Paint p = g2d.getPaint();
		g2d.translate(rect.x(), rect.y());
		
		Color[] colors = new Color[] { new Color(0, 0, 0, 255), new Color(0, 0, 0, 0) };

		g2d.setPaint(new RadialGradientPaint(rect.width() - 5, 10, 10, new float[] { 0f, 1f }, colors));
		g2d.fillRect(rect.width() - 5, 0, 10, 10);

		g2d.setPaint(new RadialGradientPaint(rect.width() - 5, rect.height() - 5, 10, new float[] { 0f, 1f }, colors));
		g2d.fillRect(rect.width() - 5, rect.height() - 5, 10, 10);

		g2d.setPaint(new RadialGradientPaint(10, rect.height() - 5, 10, new float[] { 0f, 1f }, colors));
		g2d.fillRect(0, rect.height() - 5, 10, 10);
		
		g2d.setPaint(new GradientPaint(5, rect.height() - 5, colors[0], 5, rect.height() + 5, colors[1]));
		g2d.fillRect(10, rect.height() - 5, rect.width() - 15, rect.height() + 5);

		g2d.setPaint(new GradientPaint(rect.width() - 5, 10, colors[0], rect.width() + 5, 10, colors[1]));
		g2d.fillRect(rect.width() - 5, 10, rect.width() + 5, rect.height() - 15);

		g2d.setPaint(p);
		g2d.translate(-rect.x(), -rect.y());
	}

}
