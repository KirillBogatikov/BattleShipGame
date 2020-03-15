package org.battleshipgame.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import org.battleshipgame.core.GameEngine;
import org.battleshipgame.core.ShotResult;
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
	
	private AtomicBoolean locked = new AtomicBoolean(false);
	private TextView lockerText;
	private ImageView lockerImage;
	
	private GridView playerView, friendView;
	private boolean disposed = false;
	
	public DesktopGameScreen(Image backImage, Image lockerImage, GameEngine engine, StylesResolver styles, Renderer renderer) {
		this.engine = engine;
		this.styles = styles;
		this.renderer = renderer;
		
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, styles.darkTextColor());
		timerLabel = new TextView(280, 50, 400, 50, "00:00", 24.0, styles.darkTextColor());
		playerView = new GridView(50, 90, 400, 400);
		friendView = new GridView(513, 90, 400, 400);
		
		lockerText = new TextView(230, 440, 500, 50, "ОЖИДАНИЕ ХОДА ВТОРОГО ИГРОКА...", 24.0, styles.darkTextColor());
		this.lockerImage = new ImageView(330, 120, 300, 300, lockerImage);
		
		engine.friend().listener_$eq((x, y, r, f, a) -> {
			if(r == ShotResult.MISS) {
				locked.set(false);
			}
		});
		engine.user().listener_$eq((x, y, r, f, a) -> {
		    if(r != ShotResult.MISS) {
		    	locked.set(false);
		    }
		});
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
		return new TextView[] { backLabel };
	}
	
	@Override
	public Bay userBay() {
		return engine.user().bay();
	}

	@Override
	public Bay opponentBay() {
		return engine.friend().bay();
	}

	@Override
	public GridView userView() {
		return playerView;
	}

	@Override
	public GridView opponentView() {
		return friendView;
	}
	
	@Override
	public TextView timer() {
		return timerLabel;
	}

	@Override
	public boolean disposed() {
		return disposed;
	}
	
	public void disposed(boolean disposed) {
		this.disposed = disposed;
	}
	
	@Override
	public ShotListener shotListener() {
		return (x, y) -> {
			locked.set(true);
			engine.user().shotFriend(x, y);
		};
	}

	@Override
	public ImageView lockerImage() {
		return locked.get() ? lockerImage : null;
	}

	@Override
	public TextView lockerText() {
		return lockerText;
	}

}
