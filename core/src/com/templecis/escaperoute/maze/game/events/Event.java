package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.entities.Entity;

public interface Event {

    /**
     * Returns true if event is handled, otherwise false.
     * @param world
     * @return
     */
    public boolean handle(World world);

    public void fire();
}
