package com.templecis.escaperoute.maze.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.components.input.GameInput;
import com.templecis.escaperoute.maze.menu.data.HighscoreMenu;
import com.templecis.escaperoute.maze.menu.data.Menu;
import com.templecis.escaperoute.maze.menu.view.MenuRenderer;

public class HighscoreScreen implements Screen, InputScreen {
    protected MasterMaze game;

    protected Menu menu;
    protected MenuRenderer renderer;

    public HighscoreScreen(MasterMaze game) {
        this.game = game;
        this.menu = new HighscoreMenu(this.game);
        this.renderer = new MenuRenderer(this.menu);
    }

    @Override
    public void render(float delta) {
        this.menu.getControls().update(this.game);
        this.renderer.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public InputMultiplexer getInputMultiplexer() {
        return new InputMultiplexer(renderer.getStage(), new GameInput());
    }
}
