package org.battleshipgame.ui;

import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.Image;
import org.battleshipgame.render.ImageView;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;

public class DesktopGameModeScreen extends GameModeScreen {
	private StylesResolver styles;
	private Renderer renderer;
	private Button singleplay, multiplayer;
	private ImageView backView;
	private TextView backLabel;
	private TextView screenLabel;
	
	public DesktopGameModeScreen(Image backImage, StylesResolver styles, Renderer renderer) {
		this.styles = styles;
		this.renderer = renderer;
		
		singleplay = new Button(224, 170, 200, 200, 20, "ОДИНОЧНАЯ\nИГРА", 24.0);
		multiplayer = new Button(526, 170, 200, 200, 20, "СЕТЕВАЯ\nИГРА", 24.0);
		backView = new ImageView(25, 50, 50, 50, backImage);
		backLabel = new TextView(75, 50, 100, 50, "НАЗАД", 24.0, true);
		screenLabel = new TextView(280, 50, 400, 50, "ВЫБЕРИТЕ РЕЖИМ ИГРЫ", 24.0, true);
	}
	
	public void setClickListeners(ClickListener singleplay, ClickListener multiplayer, ClickListener back) {
		this.singleplay.listener_$eq(singleplay);
		this.multiplayer.listener_$eq(multiplayer);
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
	public Button[] buttons() {
		return new Button[]{ singleplay, multiplayer };
	}

	@Override
	public ImageView[] images() {
		return new ImageView[]{ backView };
	}
	
	@Override
	public TextView[] labels() {
		return new TextView[] { backLabel, screenLabel };
	}

}
