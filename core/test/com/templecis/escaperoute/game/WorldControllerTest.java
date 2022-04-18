package com.templecis.escaperoute.game;

import com.templecis.escaperoute.Sprites.Player;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.screens.DirectedGame;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by petermontanez on 4/29/18.
 */
public class WorldControllerTest {
    @Test
    public void isPlayerInWater() throws Exception {
        Player player;
        AbstractGameObject obj;


        obj = new com.templecis.escaperoute.game.objects.Player();



        obj.position.x = -5;
        obj.position.y = -5;

        WorldController worldController = new WorldController(null, false);

        boolean water = true;

        assertEquals(water, worldController.isPlayerInWater());

    }

    @Test
    public void sample(){
        /*WorldController worldController = new WorldController();


        assertEquals(worldController.isGameOver(), false);*/
    }

}


