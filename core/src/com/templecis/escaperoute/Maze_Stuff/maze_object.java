package com.templecis.escaperoute.Maze_Stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.screens.AbstractGameScreen;
import com.templecis.escaperoute.screens.DirectedGame;

public class maze_object extends AbstractGameObject {
    private Stage stage;
    private TiledMapRenderer mazeRenderer;
    private TiledMapRenderer mapRenderer;


    public TiledMap maze_object(Maze maze){
        //super();
        //Maze maze = (new Maze_Generator()).getMaze();

        Maze_Render mTileRenderer = new Maze_Render(maze, "images/Wall.png",2);

        mazeRenderer = mTileRenderer.getRenderer();


        TiledMap tiledMap = mTileRenderer.getMap();



        return tiledMap;
    }

    private Color getRandomColor(Color notColor) {
        Color colors[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA};
        return colors[MathUtils.random(colors.length - 1)];
    }


    public void show() {

    }


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


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }


    public void pause() {

    }


    public void resume() {

    }


    public void hide() {

    }


    public void dispose() {

    }



    @Override
    public void render(SpriteBatch batch) {

    }
}
