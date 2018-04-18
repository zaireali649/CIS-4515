package com.templecis.escaperoute.maze.game.data.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.templecis.escaperoute.maze.assets.Assets;
import com.templecis.escaperoute.maze.game.entities.Player;
import com.templecis.escaperoute.maze.game.map.Map;

public class Arjan extends Player {

    public Arjan(float x, float y, Map map) {
        super(x, y, 32, 32, map);
        this.createSprite(Assets.getGameTexture("player"));

    }


}
