package com.templecis.escaperoute.maze.game.systems.input;

import com.badlogic.gdx.Game;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.components.input.GameKeys;
import com.templecis.escaperoute.maze.game.entities.Controllable;
import com.templecis.escaperoute.maze.game.map.Map;

public class GlobalGameInput implements Controllable {
    private MasterMaze game;

    public GlobalGameInput(MasterMaze game) {
        this.game = game;
    }

    @Override
    public void input() {
        if(GameKeys.isDown(GameKeys.ESCAPE)) {
            game.setScreen(game.getMainMenuScreen());
        }
    }

    @Override
    public boolean isInputEnabled() {
        return true;
    }
}
