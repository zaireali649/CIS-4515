package com.templecis.escaperoute.maze.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A character is a visible and movable entity.
 */
public abstract class Character extends MoveableEntity implements Drawable {

    public Character(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if(sprite != null) batch.draw(this.getSprite(), this.getX(), this.getY());
    }




}
