package org.battleshipgame.ui;

import org.battleshipgame.core.RenderListener;
import org.battleshipgame.core.ShipDockImpl;
import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.ImageView;
import org.battleshipgame.render.MapGridView;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;
import org.battleshipgame.render.View;

public class DesktopMapScreen extends SetupMapScreen {
	private ShipDockImpl dock;
	private StylesResolver styles;
	private Renderer renderer;
	private ImageView backView, rotateView;
	private TextView backLabel, rotateLabel;
	private MapGridView userGrid;
	private ImageView[] horizontalShipViews;
	private ImageView[] verticalShipViews;
	private Button start;

	public DesktopMapScreen(Image backImage, Image rotateImage, RenderListener listener, StylesResolver styles, Renderer renderer) {
		dock = new ShipDockImpl(listener);
		this.styles = styles;
		this.renderer = renderer;
		
		start = new Button(610, 380, 250, 60, 20, "НАЧАТЬ ИГРУ", 24.0);
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, true);
		rotateView = new ImageView(885, 50, 50, 50, rotateImage);
		rotateLabel = new TextView(720, 50, 150, 50, "ПОВЕРНУТЬ", 24.0, true);
		
		userGrid = new MapGridView(50, 90, 400, 400, styles);
		
		horizontalShipViews = new ImageView[] {
			new ImageView(654, 120, 160, 40, null),
			new ImageView(654, 180, 122, 40, null),
			new ImageView(654, 240, 80, 40, null),
			new ImageView(654, 300, 40, 40, null)
		};
		
		verticalShipViews = new ImageView[] {
			new ImageView(640, 140, 40, 160, null),
			new ImageView(700, 177, 40, 122, null),
			new ImageView(760, 220, 40, 80, null),
			new ImageView(820, 260, 40, 40, null)
		};
	}
	
	public void setClickListeners(ClickListener start, ClickListener back, ClickListener rotate) {
		this.start.listener_$eq(start);
		this.backView.listener_$eq(back);
		this.backLabel.listener_$eq(back);
		this.rotateView.listener_$eq(() -> {
			dock.switchOrientation();
			rotate.onClick();
		});
		this.rotateLabel.listener_$eq(() -> {
			dock.switchOrientation();
			rotate.onClick();
		});
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
		return new ImageView[] { backView, rotateView };
	}

	@Override
	public TextView[] labels() {
		return new TextView[] { backLabel, rotateLabel };
	}

	@Override
	public ShipsDock dock() {
		return dock;
	}

	@Override
	public Button start() {
		return start;
	}

	@Override
	public MapGridView grid() {
		return userGrid;
	}

	@Override
	public View ship(ShipSize size, ShipOrientation orientation) {
		switch(orientation) {
			case HORIZONTAL: return horizontalShipViews[4 - size.toInt()];
			case VERTICAL: return verticalShipViews[4 - size.toInt()];
		}
		return null;
	}

}
