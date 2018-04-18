package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.screens.MainMenuScreen;
import com.templecis.escaperoute.maze.utils.Timer;

public class TimeLimitReached extends TimerEvent {
    private MasterMaze game;

    public TimeLimitReached(MasterMaze game) {
        super(2);
    }

    @Override
    protected void before(World world) {
        //TODO play sound
        world.setStop(true);
        world.getPlayer().setInputEnabled(false);
    }

    @Override
    protected void finished(World world) {
        game.setScreen(new MainMenuScreen(game));
    }
}
