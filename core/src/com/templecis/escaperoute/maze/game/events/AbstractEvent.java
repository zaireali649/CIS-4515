package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.systems.events.EventHandler;

public abstract class AbstractEvent implements Event {

    public abstract boolean action(World world);

    public void fire() {
        EventHandler.events.add(this);
    }

    @Override
    public boolean handle(World world) {
        return this.action(world);
    }
}
