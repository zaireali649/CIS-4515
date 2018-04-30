
package com.templecis.escaperoute.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.templecis.escaperoute.EscapeRouteMain;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.screens.DirectedGame;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import java.awt.HeadlessException;


public class GameTest {

    SpriteBatch batch = new SpriteBatch();
    AbstractGameObject obj;


    private static Application application;

    // Before running any tests, initialize the application with the headless backend
    @BeforeClass
    public static void init() {

        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {


                SpriteBatch batch = new SpriteBatch();
                // Note that we don't need to implement any of the listener's methods
                final SpriteBatch finalBatch = batch;
            }
            @Override public void resize(int width, int height) {}
            @Override public void render() {

//                obj = new Monster();
//                obj.position.x = 5;
//                obj.position.y = 5;
//
//                Texture img = new Texture("badlogic.jpg");
//
//                batch.draw(img, obj.position.x, obj.position.y,
////                obj.origin.x, obj.origin.y, obj.dimension.x, obj.dimension.y, obj.scale.x, obj.scale.y,
////                obj.rotation, 0, 0,
////                imgMonster.getWidth(), imgMonster.getHeight(), false, false);
            }
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    // After we are done, clean up the application
    @AfterClass
    public static void cleanUp() {
        // Exit the application first
        application.exit();
        application = null;
    }
}



