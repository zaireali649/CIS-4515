package com.templecis.escaperoute.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ArrayMap;
import com.templecis.escaperoute.maze.assets.Assets;
import com.templecis.escaperoute.maze.components.input.GameInput;
import com.templecis.escaperoute.maze.components.input.GameKeys;
import com.templecis.escaperoute.maze.screens.GameScreen;
import com.templecis.escaperoute.maze.screens.HighscoreScreen;
import com.templecis.escaperoute.maze.screens.InputScreen;
import com.templecis.escaperoute.maze.screens.MainMenuScreen;

public class MasterMaze extends Game {
    public static final boolean DEBUG = true;

    public GameScreen gameScreen;
    public MainMenuScreen mainMenuScreen;
    public HighscoreScreen highscoreScreen;

    public static ArrayMap<String, Screen> screens = new ArrayMap<String, Screen>();

    public void create() {
        //TODO create GameState system

        gameScreen = new GameScreen(this);
        highscoreScreen = new HighscoreScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        screens.put("game", gameScreen);
        screens.put("highscore", highscoreScreen);
        screens.put("mainMenu", mainMenuScreen);

        Gdx.input.setInputProcessor(new GameInput());
        //this.setScreen(new GameScreen(this));
        this.setScreen(mainMenuScreen);
//        MazeGenerator m = new MazeGenerator();
//        m.generate(8, 8, 1, 1, 32, 32);
    }

    public HighscoreScreen getHighscoreScreen() {
        return highscoreScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void setScreen(Screen screen) {
        if(screen instanceof InputScreen) {
            Gdx.input.setInputProcessor(((InputScreen) screen).getInputMultiplexer());
        }
        super.setScreen(screen);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        GameKeys.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        if(this.getScreen() != null) this.getScreen().dispose();
        Assets.manager.dispose();
    }
}
