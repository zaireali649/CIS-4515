package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.game.Assets;

public class HP extends AbstractGameObject {
    private Array<TextureRegion> healthStatus;


    public HP() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);

        healthStatus = new Array<TextureRegion>();
        healthStatus.add(Assets.instance.health.health4);
        healthStatus.add(Assets.instance.health.health3);
        healthStatus.add(Assets.instance.health.health2);
        healthStatus.add(Assets.instance.health.health1);
        healthStatus.add(Assets.instance.health.health0);
    }

    public void render(SpriteBatch batch) {
        TextureRegion reg;
        reg = healthStatus.first();
        batch.draw(reg.getTexture(), position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    public int getScore() {
        return 250;
    }
}