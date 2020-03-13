package org.battleshipgame.ui;

import org.battleshipgame.render.Button;
import org.battleshipgame.render.ClickListener;
import org.battleshipgame.render.Renderer;
import org.battleshipgame.render.Size;
import org.battleshipgame.render.StylesResolver;
import org.battleshipgame.render.TextView;

public class DesktopStartScreen extends StartScreen {
	private StylesResolver styles;
	private Renderer renderer;
	private Button startGame, continueGame, settings, exit;
	
	public DesktopStartScreen(StylesResolver styles, Renderer renderer) {
		this.styles = styles;
		this.renderer = renderer;
		
		startGame = new Button(50, 120, 250, 60, 20, "НОВАЯ ИГРА", 24.0);
		continueGame = new Button(50, 200, 250, 60, 20, "ПРОДОЛЖИТЬ ИГРУ", 24.0);
		settings = new Button(50, 280, 250, 60, 20, "НАСТРОЙКИ", 24.0);
		exit = new Button(50, 360, 250, 60, 20, "ВЫХОД", 24.0);
	}
	
	public void setClickListeners(ClickListener startGame, ClickListener continueGame, ClickListener settings) {
		this.startGame.listener_$eq(startGame);
		this.continueGame.listener_$eq(continueGame);
		this.settings.listener_$eq(settings);
		this.exit.listener_$eq(() -> System.exit(0));
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
		return new Button[]{ startGame/*, continueGame, settings*/, exit };
	}
	
	@Override
	public TextView[] labels() {
		return new TextView[] { new TextView(860, 440, 100, 100, "v. 2.0", 24.0, styles.textColor()) };
	}

}
