package org.battleshipgame.ui;

import org.battleshipgame.impl.ShipDockImpl;
import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.GridView;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.ImageView;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;
import org.battleshipgame.render.View;

public class DesktopMapScreen extends MapScreen {
	private ShipDockImpl dock;
	private StylesResolver styles;
	private Renderer renderer;
	private ImageView backView, rotateView;
	private TextView backLabel, rotateLabel;
	private GridView userGrid;
	private ImageView[][] horizontalShipViews;
	private ImageView[][] verticalShipViews;
	private Button start;

	public DesktopMapScreen(Image backImage, Image rotateImage, ShipDockImpl dock, StylesResolver styles, Renderer renderer) {
		this.dock = dock;
		this.styles = styles;
		this.renderer = renderer;
		
		start = new Button(610, 380, 250, 60, 20, "НАЧАТЬ ИГРУ", 24.0);
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, styles.darkTextColor());
		rotateView = new ImageView(885, 50, 50, 50, rotateImage);
		rotateLabel = new TextView(720, 50, 150, 50, "ПОВЕРНУТЬ", 24.0, styles.darkTextColor());
		
		userGrid = new GridView(50, 120, 400, 400);
		
		horizontalShipViews = new ImageView[][] {
			{
				new ImageView(835, 315, 40, 40, null),
				new ImageView(755, 315, 40, 40, null),
				new ImageView(675, 315, 40, 40, null),
				new ImageView(595, 315, 40, 40, null),
			},
			{
				new ImageView(815, 240, 80, 40, null),
				new ImageView(695, 240, 80, 40, null),
				new ImageView(575, 240, 80, 40, null),
			},
			{
				new ImageView(735, 165, 122, 40, null),
				new ImageView(612, 165, 122, 40, null),
			},
			{
				new ImageView(655, 90, 160, 40, null),
			}
		};
		
		verticalShipViews = new ImageView[][] {
			{
				new ImageView(827, 100, 40, 40, null),
				new ImageView(827, 175, 40, 40, null),
				new ImageView(827, 235, 40, 40, null),
				new ImageView(827, 310, 40, 40, null)
			},
			{
				new ImageView(752, 75, 40, 80, null),
				new ImageView(752, 175, 40, 80, null),
				new ImageView(752, 275, 40, 80, null)
			},
			{
				new ImageView(677, 93, 40, 122, null),
				new ImageView(677, 216, 40, 122, null)
			},
			{
				new ImageView(602, 135, 40, 160, null)
			}
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
	public GridView grid() {
		return userGrid;
	}

	@Override
	public View ship(ShipSize size, ShipOrientation orientation, int id) {
		switch(orientation) {
			case HORIZONTAL: return horizontalShipViews[size.toInt() - 1][id];
			case VERTICAL: return verticalShipViews[size.toInt() - 1][id];
		}
		return null;
	}

}
