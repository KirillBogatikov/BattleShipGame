package org.battleshipgame.desktop;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.battleshipgame.render.Image;

public class SwingImage implements Image {
	private BufferedImage image;
	
	public SwingImage(byte[] imageBytes) throws IOException {
		this(new ByteArrayInputStream(imageBytes));
	}
	
	public SwingImage(InputStream imageSteam) throws IOException {
		image = ImageIO.read(imageSteam);
	}
	
	public SwingImage(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getContent() {
		return image;
	}
	
	@Override
	public int width() {
		return image.getWidth();
	}
	
	@Override
	public int height() {
		return image.getHeight();
	}
}
