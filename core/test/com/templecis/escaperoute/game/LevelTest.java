package com.templecis.escaperoute.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.EscapeRouteMain;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.game.objects.Button;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.game.objects.ReverseCoin;
import com.templecis.escaperoute.game.objects.TrapDoor;
import com.templecis.escaperoute.util.ActionResolver;

import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest extends GameTest{
    public Array<MazeTile> mazeTiles;
    public Array<ReverseCoin> reverseCoins;
    public Array<Monster> monsters;
    public Array<TrapDoor> trapDoors;
    public Array<Button> buttons;
    private Texture imgMonster;


    AbstractGameObject obj;



    @Test
    public void setMonster(){
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);
        monsters = new Array<Monster>();



        obj = new Monster();
        obj.position.set(12, 5);
       // Assets.instance.init(new AssetManager());


        assertEquals(12, obj.position.x, 0.001);
        assertEquals(5, obj.position.y, 0.001);
    }

    @Test
    public void setReverseCoin(){


        reverseCoins = new Array<ReverseCoin>();


        obj = new ReverseCoin();
        obj.position.set(100, 120);
        //monsters.add((Monster) obj);
       // Assets.instance.init(new AssetManager());
        assertEquals(100, obj.position.x, 0.001);
        assertEquals(120, obj.position.y, 0.001);
    }

    @Test
    public void setTrapDoor(){

        trapDoors = new Array<TrapDoor>();


        obj = new TrapDoor();
        obj.position.set(15, 13);
        //monsters.add((Monster) obj);
       // Assets.instance.init(new AssetManager());

        assertEquals(15, obj.position.x, 0.001);
        assertEquals(13, obj.position.y, 0.001);

    }

    @Test
    public void setMazeTiles(){

        mazeTiles = new Array<MazeTile>();


        MazeTile mt =  new MazeTile();
        mt.position.set(6, 2);

        mt.topWall = false;
        mt.rightWall = false;
        mt.leftWall = true;
        mt.bottomWall = true;
        //monsters.add((Monster) obj);
       // Assets.instance.init(new AssetManager());
        boolean truth = true;
        boolean falsely = false;

        assertEquals(6, mt.position.x, 0.001);
        assertEquals(2, mt.position.y, 0.001);

        assertEquals(truth, mt.leftWall);
        assertEquals(truth, mt.bottomWall);
        assertEquals(falsely, mt.topWall);
        assertEquals(falsely, mt.rightWall);

    }

}


