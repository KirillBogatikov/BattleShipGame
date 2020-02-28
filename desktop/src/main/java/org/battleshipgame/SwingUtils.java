package org.battleshipgame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SwingUtils {
	public static BufferedImage rotate(BufferedImage currentImage, double angle) {
	    double sin = Math.abs(Math.sin(angle));
	    double cos = Math.abs(Math.cos(angle));
	    
	    int currentWidth = currentImage.getWidth();
	    int currentHeight = currentImage.getHeight();
	    
	    int newWidth = (int)Math.floor(currentWidth * cos + currentHeight * sin);
	    int newHeight = (int) Math.floor(currentHeight * cos + currentWidth * sin);
	    
	    BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = newImage.createGraphics();
	    g.translate((newWidth - currentWidth) / 2, (newHeight - currentHeight) / 2);
	    g.rotate(angle, currentWidth / 2, currentHeight / 2);
	    g.drawRenderedImage(currentImage, null);
	    g.dispose();
	    
	    return newImage;
	}
}
