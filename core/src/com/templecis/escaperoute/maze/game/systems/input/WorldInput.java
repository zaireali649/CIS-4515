package com.templecis.escaperoute.maze.game.systems.input;

import com.templecis.escaperoute.maze.components.SystemInterface;
import com.templecis.escaperoute.maze.components.input.GameKeys;
import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.entities.Controllable;

import java.util.Iterator;

public class WorldInput implements SystemInterface {
    protected World world;

    public WorldInput(World world) {
        this.world = world;
    }

    public void update(float delta) {
        for(Controllable c : world.getControllables()) {
            if(c.isInputEnabled()) c.input();
        }
    }
}
