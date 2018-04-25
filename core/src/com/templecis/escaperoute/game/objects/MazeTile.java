package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.templecis.escaperoute.game.Assets;

/**
 * Created by Ziggy on 4/24/2018.
 */

public class MazeTile extends AbstractGameObject {

    private TextureRegion regMazeTile;

    public MazeTile() {
        init();
    }

    private void init() {
        dimension.set(2f, 2f);
        regMazeTile = Assets.instance.mazeTile.mazeTile;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }


    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;
        reg = regMazeTile;
        batch.draw(reg.getTexture(), position.x - origin.x,
                position.y - origin.y, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }

    @Override
    protected void updateMotionX(float deltaTime) {
        //super.updateMotionX(deltaTime);
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        //super.updateMotionY(deltaTime);
    }


}
