package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.templecis.escaperoute.game.Assets;

/**
 * Created by Ziggy on 4/26/2018.
 */

public class Monster extends AbstractGameObject {
    public boolean collected;

    private Texture imgMonster;

    public Monster() {
        init();
    }

    private void init() {
        dimension.set(1f, 1f);
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;

//        imgMonster = Assets.instance.monster.monster;

    }

    public void render(SpriteBatch batch) {
        if (collected) return;

        batch.draw(imgMonster, position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, 0, 0,
                imgMonster.getWidth(), imgMonster.getHeight(), false, false);
    }

    public int getStrength() {
        return 1;
    }
}
