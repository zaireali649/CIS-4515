package com.templecis.escaperoute.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.EscapeRouteMain;
import com.templecis.escaperoute.HUD.HealthBar;
import com.templecis.escaperoute.HUD.LoadingBarWithBorders;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.game.objects.Button;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.game.objects.ReverseCoin;
import com.templecis.escaperoute.game.objects.TrapDoor;
import com.templecis.escaperoute.screens.DirectedGame;
import com.templecis.escaperoute.screens.MenuScreen;
import com.templecis.escaperoute.screens.transitions.ScreenTransition;
import com.templecis.escaperoute.screens.transitions.ScreenTransitionSlice;
import com.templecis.escaperoute.util.ActionResolver;

import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest{

    private Stage stage;
    private HealthBar healthBar;
    private LoadingBarWithBorders loadingBarWithBorders;

    private Boolean Attacker;
    Texture img;

    private long lastUpdate = 0L;


    public Array<MazeTile> mazeTiles;
    public Array<ReverseCoin> reverseCoins;
    public Array<Monster> monsters;
    public Array<TrapDoor> trapDoors;
    public Array<Button> buttons;
    private Texture imgMonster;
SpriteBatch batch;

    AbstractGameObject obj;

    private void init(){

     //   Assets.instance.init(new AssetManager());

//        monsters = new Array<Monster>();
//
//
//
//        obj = new Monster();
//        obj.position.set(12, 5);
//
//        reverseCoins = new Array<ReverseCoin>();
//
//
//        obj = new ReverseCoin();
//        obj.position.set(100, 120);
//
//        trapDoors = new Array<TrapDoor>();
//
//
//        obj = new TrapDoor();
//        obj.position.set(15, 13);
//        mazeTiles = new Array<MazeTile>();
//
//
//        MazeTile mt =  new MazeTile();
//        mt.position.set(6, 2);
//
//        mt.topWall = false;
//        mt.rightWall = false;
//        mt.leftWall = true;
//        mt.bottomWall = true;
        //monsters.add((Monster) obj);
        // Assets.instance.init(new AssetManager());
        boolean truth = true;
        boolean falsely = false;

        //img = new Texture(Color.BLACK);


    }


    @Test
    public void setMonster(){
        init();
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);
//        monsters = new Array<Monster>();



        obj = new Monster();
        obj.position.set(12, 5);

        batch.draw(img, obj.position.x, obj.position.y,
                obj.origin.x, obj.origin.y, obj.dimension.x, obj.dimension.y, obj.scale.x, obj.scale.y,
                obj.rotation, 0, 0,
                imgMonster.getWidth(), imgMonster.getHeight(), false, false);
       // Assets.instance.init(new AssetManager());


        assertEquals(12, obj.position.x, 0.001);
        assertEquals(5, obj.position.y, 0.001);
    }

    @Test
    public void setReverseCoin(){

        init();
//        reverseCoins = new Array<ReverseCoin>();
//
//
//        obj = new ReverseCoin();
//        obj.position.set(100, 120);
        //monsters.add((Monster) obj);
       // Assets.instance.init(new AssetManager());
        assertEquals(100, obj.position.x, 0.001);
        assertEquals(120, obj.position.y, 0.001);
    }

    @Test
    public void setTrapDoor(){
        init();
//        trapDoors = new Array<TrapDoor>();
//
//
//        obj = new TrapDoor();
//        obj.position.set(15, 13);
        //monsters.add((Monster) obj);
       // Assets.instance.init(new AssetManager());

        assertEquals(15, obj.position.x, 0.001);
        assertEquals(13, obj.position.y, 0.001);

    }

    @Test
    public void setMazeTiles(){

//        mazeTiles = new Array<MazeTile>();
//
//
        MazeTile mt =  new MazeTile();
//        mt.position.set(6, 2);
//
//        mt.topWall = false;
//        mt.rightWall = false;
//        mt.leftWall = true;
//        mt.bottomWall = true;
//        //monsters.add((Monster) obj);
//       // Assets.instance.init(new AssetManager());
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


