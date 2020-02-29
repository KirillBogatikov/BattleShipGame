package org.battleshipgame.ui;

import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.ImageView;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;

public class DesktopConnectionScreen extends ConnectionScreen {
	private StylesResolver styles;
	private Renderer renderer;
	private Button connect, create;
	private ImageView backView;
	private TextView backLabel;
	private TextView screenLabel;
	private TextView gameId;
	
	public DesktopConnectionScreen(Image backImage, StylesResolver styles, Renderer renderer) {
		this.styles = styles;
		this.renderer = renderer;
		
		connect = new Button(355, 280, 250, 60, 20, "ПОДКЛЮЧИТЬСЯ", 24.0);
		create = new Button(355, 360, 250, 60, 20, "СОЗДАТЬ ИГРУ", 24.0);
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, true);
		screenLabel = new TextView(280, 50, 400, 50, "СЕТЕВАЯ ИГРА", 24.0, true);
		gameId = new TextView(280, 200, 400, 60, 10,  "", 24.0, false);
	}
	
	public void setClickListeners(ClickListener connect, ClickListener create, ClickListener back) {
		this.connect.listener_$eq(connect);
		this.create.listener_$eq(create);
		backView.listener_$eq(back);
		backLabel.listener_$eq(back);
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
	public Size size() {
		return new Size(960, 540);
	}

	@Override
	public ImageView[] images() {
		return new ImageView[] { backView };
	}

	@Override
	public TextView[] labels() {
		return new TextView[] { backLabel, screenLabel };
	}

	@Override
	public TextView[] inputs() {
		return new TextView[] { gameId };
	}

	@Override
	public Button[] buttons() {
		return new Button[] { create, connect };
	}

}
