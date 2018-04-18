package com.templecis.escaperoute.maze.game.systems.movement;

import com.templecis.escaperoute.maze.game.entities.Moveable;
import com.templecis.escaperoute.maze.game.entities.MoveableEntity;
import com.templecis.escaperoute.maze.components.SystemInterface;
import com.templecis.escaperoute.maze.game.data.World;

import java.util.Iterator;

public class WorldMovement implements SystemInterface {

    protected World world;
    protected Iterator<Moveable> movablesIterator;

    public WorldMovement(World world) {
        this.world = world;
    }

    public void update(float delta) {
        this.movablesIterator = world.getMovables().iterator();
        while(movablesIterator.hasNext()) {
            Moveable m = movablesIterator.next();
            if(!m.isStopped()) {
                m.move(delta, world.getMap());
            }
        }
    }


    public void copyCurrentPosition() {
        this.movablesIterator = world.getMovables().iterator();
        while(movablesIterator.hasNext()) {
            MoveableEntity entity = (MoveableEntity) movablesIterator.next();
            entity.positionPrevious.x = entity.getX();
            entity.positionPrevious.y = entity.getY();
        }
    }


    public void interpolateCurrentPosition(float alpha) {
        this.movablesIterator = world.getMovables().iterator();
        while(movablesIterator.hasNext()) {
            MoveableEntity entity = (MoveableEntity) movablesIterator.next();
            entity.positionPrevious.x = entity.getX();
            entity.positionPrevious.y = entity.getY();

            entity.setX(entity.getX() * alpha + entity.positionPrevious.x * (1.0f - alpha));
            entity.setY(entity.getY() * alpha + entity.positionPrevious.y * (1.0f - alpha));
        }
    }


}
