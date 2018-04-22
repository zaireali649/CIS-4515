package com.templecis.escaperoute.Maze_Stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.templecis.escaperoute.screens.AbstractGameScreen;
import com.templecis.escaperoute.screens.DirectedGame;

public class maze_screen extends AbstractGameScreen {
    private Stage stage;
    private TiledMapRenderer mazeRenderer;
    private TiledMapRenderer mapRenderer;

    public maze_screen(DirectedGame game) {
        super(game);
    }


    public void maze_screen(){
        //super();
        Maze maze = (new Maze_Generator()).getMaze();

        Maze_Render mTileRenderer = new Maze_Render(maze, "images/Wall.png",2);

        mazeRenderer = mTileRenderer.getRenderer();


        TiledMap tiledMap = mTileRenderer.getMap();




    }

    private Color getRandomColor(Color notColor) {
        Color colors[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA};
        return colors[MathUtils.random(colors.length - 1)];
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();

        //Stage's InputAdapters was not called with keyboard events.
        //I think, it is logical, because keyboard events are global
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();

        OrthographicCamera cam = (OrthographicCamera) stage.getCamera();

        mazeRenderer.setView(cam);
        mazeRenderer.render();


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
