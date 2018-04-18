package com.templecis.escaperoute.maze.game.map;

import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.game.entities.Entity;
import com.templecis.escaperoute.maze.game.events.Event;
import com.templecis.escaperoute.maze.game.systems.events.EventHandler;

public class EventTile extends Tile {
    private boolean fired;
    protected Event event;

    public EventTile(int x, int y, int width, int height, String type) {
        super(x, y, width, height, type);
    }

    public boolean isFired() {
        return this.fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean fireEvent(Entity entity) {
        if(!this.isFired()) {
            if(this.isInTile(entity.getCenterX(), entity.getCenterY())) {
                this.event.fire();
                this.setFired(true);
                return true;
            }
        }
        return false;
    }
}
