package org.battleshipgame.desktop;

import org.battleshipgame.render.Image;
import org.battleshipgame.render.StylesResolver;

public class DesktopStylesResolver implements StylesResolver {
	private Image background;
	
	public DesktopStylesResolver(Image background) {
		this.background = background;
	}
	
	@Override
	public Image background() {
		return background;
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
	public long linesColor() {
		return buttonDefault();
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
	public long inputBackground() {
		return buttonPressed();
	}

	@Override
	public long highlightBackground() {
		return 0xFFFF0000;
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
