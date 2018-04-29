package com.templecis.escaperoute.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.templecis.escaperoute.game.Assets;

public class Button extends AbstractGameObject {

    private Texture imgButton;
    public ButtonType bt;

    private ShapeRenderer shapeRenderer;
    static private boolean projectionMatrixSet;

    public Button(ButtonType bt) {
        this.bt = bt;
        init();
    }

    private void init() {
        dimension.set(1f, 1f);
        shapeRenderer = new ShapeRenderer();
        projectionMatrixSet = false;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);

        if (bt == ButtonType.MONSTER){
            imgButton = Assets.instance.monster.monster;
        }
        else if (bt == ButtonType.TRAPDOOR){
            imgButton = Assets.instance.trapDoor.trapDoor;
        } else if (bt == ButtonType.REVERSECOIN) {
            imgButton = Assets.instance.reverseCoin.reverseCoin;
        }
        else{
            imgButton = null;
        }
    }

    public void render(SpriteBatch batch) {

        batch.end();

        if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(position.x, position.y , dimension.x, dimension.y);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y , dimension.x, dimension.y);
        shapeRenderer.end();

        batch.begin();

        batch.draw(imgButton, position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, 0, 0,
                imgButton.getWidth(), imgButton.getHeight(), false, false);


    }

}
