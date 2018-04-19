package com.templecis.escaperoute.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by petermontanez on 4/19/18.
 */

public class Trap {
    public static final int TRAP_WIDTH = 20;

    private Texture trap;
    private Vector2 posTrap;
    private Random rand;
    private Rectangle bounds;

    public Trap(float x){

        trap = new Texture("trap.png"); //figure out random traps?
        rand = new Random();

        posTrap = new Vector2(rand.nextInt(800), rand.nextInt(800));

        bounds = new Rectangle(posTrap.x, posTrap.y, trap.getWidth(), trap.getHeight());
    }

    public Texture getTrap() {
        return trap;
    }

    public Vector2 getPosTrap() {
        return posTrap;
    }

    public void reposition(float x){
        posTrap.set(rand.nextInt(800), rand.nextInt(800));
        bounds.setPosition(posTrap.x, posTrap.y);

    }

    public boolean collidies(Rectangle player){

    return player.overlaps(bounds);
    }
}
