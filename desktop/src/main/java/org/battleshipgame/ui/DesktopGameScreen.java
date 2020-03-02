package org.battleshipgame.ui;

import org.battleshipgame.core.GameEngine;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.GridView;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.ImageView;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;

public class DesktopGameScreen extends GameScreen {
	private StylesResolver styles;
	private Renderer renderer;
	private ImageView backView;
	private TextView backLabel;
	private TextView timerLabel;
	private GameEngine engine;
	private GridView playerView, friendView;
	
	public DesktopGameScreen(Image backImage, GameEngine engine, StylesResolver styles, Renderer renderer) {
		this.engine = engine;
		this.styles = styles;
		this.renderer = renderer;
		
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, styles.darkTextColor());
		timerLabel = new TextView(280, 50, 400, 50, "00:00", 24.0, styles.darkTextColor());
		playerView = new GridView(50, 90, 400, 400);
		friendView = new GridView(513, 90, 400, 400);
	}
	
	public void setClickListeners(ClickListener back) {
		this.backView.listener_$eq(back);
		this.backLabel.listener_$eq(back);
	}

	@Override
	public Size size() {
		return new Size(960, 540);
	}

	@Override
	public Renderer renderer() {
		return renderer;
	}

	@Override
	public StylesResolver styles() {
		return styles;
	}

	@Override
	public ImageView[] images() {
		return new ImageView[]{ backView };
	}
	
	@Override
	public TextView[] labels() {
		return new TextView[] { backLabel, timerLabel };
	}
	
	@Override
	public Bay userBay() {
		return engine.player();
	}

	@Override
	public Bay opponentBay() {
		return engine.friend();
	}

	@Override
	public GridView userView() {
		return playerView;
	}

	@Override
	public GridView opponentView() {
		return friendView;
	}

}
