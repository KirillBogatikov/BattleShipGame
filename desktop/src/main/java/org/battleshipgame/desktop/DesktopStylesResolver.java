package org.battleshipgame.desktop;

import java.awt.image.BufferedImage;

import org.battleshipgame.SwingUtils;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.ui.ShipOrientation;
import org.battleshipgame.ui.ShipSize;

public class DesktopStylesResolver implements StylesResolver {
	private Image background;
	private Image flame;
	private Image wreck;
	private Image miss;
	private Image[] horizontalShips;
	private Image[] verticalShips;
	
	public DesktopStylesResolver(SwingImage background, SwingImage flame, SwingImage wreck, SwingImage miss, SwingImage[] horizontalShips) {
		this.background = background;
		this.flame = flame;
		this.wreck = wreck;
		this.miss = miss;
		this.horizontalShips = horizontalShips;
		this.verticalShips = new Image[horizontalShips.length];
		
		for(int i = 0; i < horizontalShips.length; i++) {
			BufferedImage bufferedImage = SwingUtils.rotate(horizontalShips[i].getContent());
			verticalShips[i] = new SwingImage(bufferedImage);
		}
	}
	
	@Override
	public Image background() {
		return background;
	}

	@Override
	public Image flame() {
		return flame;
	}

	@Override
	public Image wreck() {
		return wreck;
	}

	@Override
	public Image miss() {
		return miss;
	}

	@Override
	public Image ship(ShipSize size, ShipOrientation orientation) {
		switch(orientation) {
			case VERTICAL: return verticalShips[4 - size.toInt()];
			case HORIZONTAL: return horizontalShips[4 -size.toInt()];
		}
		return null;
	}

	@Override
	public long buttonDefault() {
		return 0xFF1060BD;
	}

	@Override
	public long buttonPressed() {
		return 0xFF0D4E99;
	}

	@Override
	public long buttonHovered() {
		return 0xFF1683FF;
	}

	@Override
	public long linesColorDefault() {
		return 0xFF1060BD;
	}

	@Override
	public long linesColorFocused() {
		return 0xFF1683FF;
	}
	
	@Override
	public long textColor() {
		return 0xFFFFFFFF;
	}

	@Override
	public long darkTextColor() {
		return buttonDefault();
	}

	@Override
	public long inputBackgroundDefault() {
		return 0x400D4E99;
	}

	@Override
	public long inputBackgroundFocused() {
		return 0x401683FF;
	}

	@Override
	public long lockerColor() {
		return 0xB0FFFFFF;
	}
	
	@Override
	public long highlightBackground() {
		return 0x407F0000;
	}

	@Override
	public long highlightStroke() {
		return 0xFFFF0000;
	}

	@Override
	public int strokeSize() {
		return 5;
	}
}
