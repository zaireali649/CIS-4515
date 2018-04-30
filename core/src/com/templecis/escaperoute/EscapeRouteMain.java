package com.templecis.escaperoute;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.templecis.escaperoute.game.Assets;
import com.templecis.escaperoute.screens.DirectedGame;
import com.templecis.escaperoute.screens.MenuScreen;
import com.templecis.escaperoute.screens.transitions.ScreenTransition;
import com.templecis.escaperoute.screens.transitions.ScreenTransitionSlice;
import com.templecis.escaperoute.util.ActionResolver;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.Collections;

public class EscapeRouteMain extends DirectedGame {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public Boolean connected = false;

	public ActionResolver actionResolver;

	public MenuScreen ms;


	public static final String title = "Escape Route";
    //private GameStateManager gsm;
	private SpriteBatch batch;
	Texture img;

	public EscapeRouteMain(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}


	@Override
	public void create() {

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        Gdx.gl.glClearColor(1, 0 , 0,1);

        // Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		Assets.instance.init(new AssetManager());

		// Start game at menu screen
		ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		ms = new MenuScreen(this);
		setScreen(ms, transition);
	}

    @Override
    public void render() {
        super.render();
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

	public void onRoomConnected(ArrayList<String> participantIds, String mMyId, long creationTimestamp) {
		connected = true;
		Collections.sort(participantIds);
		for (int i = 0; i < participantIds.size(); i++){
			Gdx.app.debug("ERM", participantIds.get(i));
		}

	}
}