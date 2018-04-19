package com.templecis.escaperoute.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by petermontanez on 4/19/18.
 */

public class Player {


    private Vector3 position;
    private Vector3 velocity;
    private Texture ball;
    private Rectangle bounds;

    public Player(int x, int y){
        position = new Vector3( x, y, 0);
        velocity = new Vector3(0, 0, 0);
        ball = new Texture("ball.png");
        bounds = new Rectangle(x, y, ball.getWidth(), ball.getHeight());
    }

    public void update (float dt){
        velocity.scl(dt);
        position.add(0, velocity.y, 0);
        //maybe not needed
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);

    }

    public Texture getTexture() {
        return ball;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds(){

        return bounds;
    }
}
