package com.templecis.escaperoute.maze.game.entities;

import com.templecis.escaperoute.maze.game.map.Map;

public interface Moveable {

    public void move(float delta, Map map);

    public boolean isStopped();
}
