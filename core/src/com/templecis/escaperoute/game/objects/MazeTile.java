package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.templecis.escaperoute.game.Assets;

/**
 * Created by Ziggy on 4/24/2018.
 */

public class MazeTile extends AbstractGameObject {

    private TextureRegion regMazeTile;
    private ShapeRenderer shapeRenderer;

    private Color wallColor;
    public int thickness; // Divides height and width by this number and border is 1 of them

    public Boolean topWall, rightWall, leftWall, bottomWall;

    static private boolean projectionMatrixSet;

    public MazeTile() {
        init();
    }

    private void init() {
        topWall = false;
        rightWall = false;
        leftWall = false;
        bottomWall = false;

        wallColor = Color.RED;
        thickness = 10;

        shapeRenderer = new ShapeRenderer();
        projectionMatrixSet = false;

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

        batch.end();

        if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }

        if(leftWall)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(position.x - dimension.x/2, position.y - dimension.y/2, dimension.x/thickness, dimension.y);
            shapeRenderer.end();
        }

        if(rightWall){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(position.x + dimension.x/2 - dimension.x/thickness, position.y - dimension.y/2, dimension.x/thickness, dimension.y);
            shapeRenderer.end();
        }

        if(bottomWall){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(position.x - dimension.x/2, position.y - dimension.y/2, dimension.x, dimension.y/thickness);
            shapeRenderer.end();
        }

        if(topWall) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(position.x - dimension.x / 2, position.y + dimension.y / 2 - dimension.y / thickness, dimension.x, dimension.y / thickness);
            shapeRenderer.end();
        }

        batch.begin();
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
