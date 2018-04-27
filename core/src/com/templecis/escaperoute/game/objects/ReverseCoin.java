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
    //private TextureRegion regReverseCoin;

    private Texture imgReverseCoin;

    private ShapeRenderer shapeRenderer;
    static private boolean projectionMatrixSet;

    public ReverseCoin() {
        init();
    }

    private void init() {
        dimension.set(1f, 1f);
        //regFeather = Assets.instance.feather.feather;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;

        imgReverseCoin = Assets.instance.reverseCoin.reverseCoin;

        shapeRenderer = new ShapeRenderer();
        projectionMatrixSet = false;
    }

    public void render(SpriteBatch batch) {
        if (collected) return;
        /*TextureRegion reg;
        reg = regFeather;*/
        //batch.begin();

        batch.draw(imgReverseCoin, position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, 0, 0,
                imgReverseCoin.getWidth(), imgReverseCoin.getHeight(), false, false);

        //batch.end();

        /*if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y, dimension.x, dimension.y);
        shapeRenderer.end();*/

    }

    public int getDuration() {
        return 5;
    }
}
