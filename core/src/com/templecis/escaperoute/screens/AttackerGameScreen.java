package com.templecis.escaperoute.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.templecis.escaperoute.game.WorldController;
import com.templecis.escaperoute.game.WorldRenderer;
import com.templecis.escaperoute.util.GamePreferences;

/**
 * Created by Ziggy on 4/27/2018.
 */

public class AttackerGameScreen extends AbstractGameScreen {
    private static final String TAG = AttackerGameScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean paused;

    public AttackerGameScreen (DirectedGame game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed since last rendered frame.
            worldController.update(deltaTime);
        }
        // Sets the clear screen color to: Cornflower Blue
        //Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClearColor(0x00 / 255.0f, 0x00 / 255.0f, 0x00 / 255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.renderAttack();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        GamePreferences.instance.load();
        worldController = new WorldController(game, true);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void hide() {
        worldController.dispose();
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
    }
}