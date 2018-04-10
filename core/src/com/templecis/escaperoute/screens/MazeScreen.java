package com.templecis.escaperoute.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.templecis.escaperoute.util.actors.Bouncer;
import com.templecis.escaperoute.util.actors.Fly;
import com.templecis.escaperoute.util.camera.MapViewport;
import com.templecis.escaperoute.util.mazeScreenCode.BobActor;
import com.templecis.escaperoute.util.mazeScreenCode.CameraActor;
import com.templecis.escaperoute.util.mazeScreenCode.CameraInputAdapter;
import com.templecis.escaperoute.util.mazeScreenCode.Const;
import com.templecis.escaperoute.util.mazeScreenCode.Maze;
import com.templecis.escaperoute.util.mazeScreenCode.MazeCreator;
import com.templecis.escaperoute.util.mazeScreenCode.MazeMapRenderer;
import com.templecis.escaperoute.util.mazeScreenCode.MazeTileRenderer;



public class MazeScreen implements Screen {

    private Stage stage;
    private TiledMapRenderer mazeRenderer;
    private CameraInputAdapter camInput;
    private MapViewport mapViewport;
    private TiledMapRenderer mapRenderer;




    public MazeScreen(){
        super();
        Maze maze = (new MazeCreator()).getMaze();

        MazeTileRenderer mTileRenderer =
                new MazeTileRenderer(maze, "images/Wall.png");

        mazeRenderer = mTileRenderer.getRenderer();

        MazeMapRenderer mMapRenderer =
                new MazeMapRenderer(maze, "images/Wall.png", 2);

        mapRenderer = mMapRenderer.getRenderer();

        TiledMap tiledMap = mTileRenderer.getMap();

        camInput = new CameraInputAdapter();
        Actor bobActor = new BobActor(camInput, tiledMap);
        Actor cameraActor =
                new CameraActor(tiledMap, (BobActor) bobActor);
        ScreenViewport svp = new ScreenViewport();
        svp.update(Const.TILE_SIZE * Const.MAZE_MAGNIFY_TO_WORDL,
                Const.TILE_SIZE * Const.MAZE_MAGNIFY_TO_WORDL, true);

        mapViewport = new MapViewport(Const.TILE_SIZE * Const.MAZE_WIDTH,
                Const.TILE_SIZE * Const.MAZE_HEIGHT);

        stage = new Stage(svp);
        stage.addActor(cameraActor);
        Gdx.app.log("MazeScreen2d","bob is adding to stage");
        stage.addActor(bobActor);
        Gdx.app.log("MazeScreen2d","bob adding done");
        Actor bouncers[] = new Actor[10];
        for (int i = 0; i < bouncers.length; i+= 2) {
            bouncers[i] = new Bouncer(32, 32, getRandomColor(Color.BLACK), (float)(Math.random() * 0.6) + 0.1f, tiledMap);
            bouncers[i + 1] = new Fly(32, 32, getRandomColor(Color.BLACK), (float)(Math.random() * 0.6) + 0.1f, tiledMap);
            stage.addActor(bouncers[i]);
            stage.addActor(bouncers[i + 1]);
        }
    }

    private Color getRandomColor(Color notColor) {
        Color colors[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA};
        return colors[MathUtils.random(colors.length - 1)];
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(camInput);
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

        mapViewport.apply();
        mapRenderer.setView((OrthographicCamera)mapViewport.getCamera());
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        mapViewport.update(width, height, true);
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
}
