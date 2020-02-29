package org.battleshipgame;

import java.awt.image.BufferedImage;

public class SwingUtils {
	public static BufferedImage rotate(BufferedImage currentImage) {	    
	    int currentWidth = currentImage.getWidth();
	    int currentHeight = currentImage.getHeight();
	    
	    BufferedImage newImage = new BufferedImage(currentHeight, currentWidth, BufferedImage.TYPE_INT_ARGB);
	    
	    for(int i = 0; i < currentWidth; i++) {
	        for(int j = 0;j < currentHeight; j++) {
	            newImage.setRGB(currentHeight - 1 - j, i, currentImage.getRGB(i, j));
	        }
	    }
	    
	    return newImage;
	}
}
