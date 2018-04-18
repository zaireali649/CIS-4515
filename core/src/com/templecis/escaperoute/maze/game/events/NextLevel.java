package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.stats.Stats;

public class NextLevel extends TimerEvent {

    public NextLevel() {
        super(2);
    }

    @Override
    protected void before(World world) {
        D.o("Level ending");
        //TODO stop world timer
        //TODO save score
        //TODO play win sound
        world.getTimer().stop();
        Stats.time.put(Stats.level, (int) world.getTimer().getRemainingTime());
        D.o("Ending time: " + world.getTimer().getRemainingTime());
        world.getPlayer().setStop(true);
        world.getPlayer().setInputEnabled(false);
    }

    @Override
    protected void finished(World world) {
        Stats.level++;
        world.initialize();
        D.o("Going to next level");
    }

}
