package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.entities.Entity;
import com.templecis.escaperoute.maze.game.stats.Stats;
import com.templecis.escaperoute.maze.utils.Timer;

public class StartLevel extends TimerEvent {

    public StartLevel() {
        super(2);
    }

    @Override
    protected void before(World world) {
        //TODO play start sound
    }

    @Override
    protected void finished(World world) {
        world.start();
    }

}
