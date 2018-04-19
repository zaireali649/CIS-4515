package com.templecis.escaperoute;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.templecis.escaperoute.game.Assets;
import com.templecis.escaperoute.screens.DirectedGame;
import com.templecis.escaperoute.screens.MenuScreen;
import com.templecis.escaperoute.screens.transitions.ScreenTransition;
import com.templecis.escaperoute.screens.transitions.ScreenTransitionSlice;

public class EscapeRouteMain extends DirectedGame {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 800;

	public static final String title = "Escape Route";

	SpriteBatch batch;
	Texture img;

	@Override
	public void create() {

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		Assets.instance.init(new AssetManager());



		// Start game at menu screen
		ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		setScreen(new MenuScreen(this), transition);
	}
}
