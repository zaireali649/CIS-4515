package com.templecis.escaperoute.util.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by petermontanez on 4/17/18.
 */

public class Player extends Sprite{

    public Player(String name, float x, float y){
        super(new Texture(name));
        setPosition(x , y);

    }
} //player


















