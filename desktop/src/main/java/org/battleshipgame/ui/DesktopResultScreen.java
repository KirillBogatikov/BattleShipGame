package org.battleshipgame.ui;

import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;

public class DesktopResultScreen extends ResultScreen {
	private StylesResolver styles;
	private Renderer renderer;
	private Button mainMenu;
	private Button exit;
	private TextView mainLabel;
	private TextView timeLabel;
	
	public DesktopResultScreen(boolean win, int minutes, int seconds, StylesResolver styles, Renderer renderer, ClickListener listener) {
		this.styles = styles;
		this.renderer = renderer;
		
		mainMenu = new Button(355, 290, 250, 60, 20, "В МЕНЮ", 24.0);
		mainMenu.listener_$eq(listener);
		exit = new Button(355, 380, 250, 60, 20, "ВЫХОД", 24.0);
		exit.listener_$eq(() -> { System.exit(0); });
		
		String ms = "";
		if (minutes < 10) ms = "0";
		ms += minutes;
				
		String ss = "";
		if (seconds < 10) ss = "0";
		ss += seconds;
		
		mainLabel = new TextView(180, 100, 600, 60, win ? "ПОБЕДА!" : "ВЫ ПРОИГРАЛИ БИТВУ...", 60.0, styles.darkTextColor());
		timeLabel = new TextView(180, 200, 600, 60, "ВРЕМЯ ИГРЫ: " + ms + ":" + ss, 30.0, styles.darkTextColor());
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
		return new Button[] { mainMenu, exit };
	}

	@Override
	public TextView[] labels() {
		return new TextView[] { mainLabel, timeLabel };
	}

}
