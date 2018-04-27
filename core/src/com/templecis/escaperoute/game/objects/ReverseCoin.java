package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.templecis.escaperoute.game.Assets;

/**
 * Created by Ziggy on 4/26/2018.
 */

public class ReverseCoin extends AbstractGameObject {
    public boolean collected;

    private Texture imgReverseCoin;

    public ReverseCoin() {
        init();
    }

    private void init() {
        dimension.set(1f, 1f);
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;

        imgReverseCoin = Assets.instance.reverseCoin.reverseCoin;

    }

    public void render(SpriteBatch batch) {
        if (collected) return;

        batch.draw(imgReverseCoin, position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, 0, 0,
                imgReverseCoin.getWidth(), imgReverseCoin.getHeight(), false, false);
    }

    public int getDuration() {
        return 5;
    }
}
