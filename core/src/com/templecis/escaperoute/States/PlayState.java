package com.templecis.escaperoute.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.templecis.escaperoute.EscapeRouteMain;
import com.templecis.escaperoute.Sprites.Player;
import com.templecis.escaperoute.Sprites.Trap;

/**
 * Created by petermontanez on 4/19/18.
 */

public class PlayState extends State {

    private Player player;
    private Trap trap;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        player = new Player(50,100);
        cam.setToOrtho(false, EscapeRouteMain.WIDTH/2, EscapeRouteMain.HEIGHT /2);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);

 //Just restarts. Need to be more specific. Starting point
        if(trap.collidies(player.getBounds())){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        sb.end();


    }

    @Override
    public void dispose() {

    }
}
