package com.templecis.escaperoute.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.templecis.escaperoute.util.Constants;

/**
 * Created by Ziggy on 4/19/2018.
 */

/*
This file handles rendering HUD like information such as score, time, health, ect...
 */





public class WorldRenderer implements Disposable {
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private OrthographicCamera cameraGUI;
    private Box2DDebugRenderer b2debugRenderer;

    boolean attacker = false;

    //Health bar vars
    long startTime = System.currentTimeMillis();


    Texture texture, texture2;
    int timer;
    int timeleft;

    float x = -15;
    float y = -15;
    float offsetX = 50;
    float offsetY = 50;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true);
        // flip y-axis
        cameraGUI.update();
        b2debugRenderer = new Box2DDebugRenderer();

    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);




    }


    public boolean renderAttack(){
        renderWorld(batch);
        renderGui(batch);

        attacker = true;
        return attacker;
    }

    private void renderWorld(SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
        if (DEBUG_DRAW_BOX2D_WORLD) {
            b2debugRenderer.render(worldController.b2world, camera.combined);
        }
    }

    private void renderGui(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        // draw collected gold coins icon + text
        // (anchored to top left edge)
        //renderGuiScore(batch);
        // draw collected feather icon (anchored to top left edge)
        //renderGuiFeatherPowerup(batch);
        // draw extra lives icon + text (anchored to top right edge)
        //renderGuiExtraLive(batch);
        // draw FPS text (anchored to bottom right edge)
        Countdown(batch);
        renderHealthBar(batch);

        if (attacker){
            renderGuiGameOverMessage(batch);
        }

        // draw game over text
        //renderGuiGameOverMessage(batch);


        batch.end();
    }

    private void renderGuiScore(SpriteBatch batch) {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if (worldController.scoreVisual < worldController.score) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }
        batch.draw(Assets.instance.goldCoin.goldCoin, x, y, offsetX, offsetY, 100, 100, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch, "" + (int) worldController.scoreVisual, x + 75, y + 37);
    }

    boolean dead = false;
    private void renderHealthBar(SpriteBatch batch){
        int lives = get_lives();

        if(lives == 4){
            batch.draw(Assets.instance.health.health4, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);

        }
        else if(lives == 3){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 3");
            batch.draw(Assets.instance.health.health3, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
        }
        else if(lives == 2){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 2");
            batch.draw(Assets.instance.health.health2, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
        }
        else if(lives == 1){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 1");
            batch.draw(Assets.instance.health.health1, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
        }
        else if(lives == 0 && !dead){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 0");
            dead = true;
            batch.draw(Assets.instance.health.health0, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
            Gdx.app.log("WorldRender 146","INSIDE LAST ELSE IF");
            renderGuiGameOverMessage(batch);

            // worldController.backToMenu();
        }





  /*  int i = 0;
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.draw(texture2,100,100,300,20);
        batch.draw(texture,100,100,i,20);
        if(i>300)
        {
            i=0;
        }
        i++;*/

       /*
       healthBar.setPosition(10, Gdx.graphics.getHeight() - 20);
        stage.addActor(healthBar);


        loadingBarWithBorders = new LoadingBarWithBorders(170, 20);
        loadingBarWithBorders.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(loadingBarWithBorders);
        */
    }

    private void initTestObjects() {

        int width =1 ;
        int height = 1;
        Pixmap pixmap = createProceduralPixmap(width, height,0,1,0);
        Pixmap pixmap2 = createProceduralPixmap(width, height,1,0,0);

        texture = new Texture(pixmap);
        texture2 = new Texture(pixmap2);



    }

    private Pixmap createProceduralPixmap (int width, int height, int r, int g, int b) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(r, g, b, 1);
        pixmap.fill();

        return pixmap;
    }


    private void renderGuiGameOverMessage(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        //Gdx.app.log("WorldRender 217","GAMEOVER");
       // if (worldController.isGameOver() || timeleft == 0) {
            Gdx.app.log("WorldRender 221","GAMEOVER");
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, true);
            fontGameOver.setColor(1, 1, 1, 1);


           // dispose();
            //worldController.backToMenu();
        //}
       //Gdx.app.log("WorldRender 226","GAMEOVER");

    }

    private void renderGuiFeatherPowerup(SpriteBatch batch) {
        float x = -15;
        float y = 30;
        float timeLeftFeatherPowerup = worldController.level.bunnyHead.timeLeftFeatherPowerup;
        if (timeLeftFeatherPowerup > 0) {
            // Start icon fade in/out if the left power-up time
            // is less than 4 seconds. The fade interval is set
            // to 5 changes per second.
            if (timeLeftFeatherPowerup < 4) {
                if (((int) (timeLeftFeatherPowerup * 5) % 2) != 0) {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
            Assets.instance.fonts.defaultSmall.draw(batch, "" + (int) timeLeftFeatherPowerup, x + 60, y + 57);
        }
    }

    private void renderGuiExtraLive(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (worldController.lives <= i) {
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            }
            batch.draw(Assets.instance.bunny.head, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
        if (worldController.lives >= 0 && worldController.livesVisual > worldController.lives) {
            int i = worldController.lives;
            float alphaColor = Math.max(0, worldController.livesVisual - worldController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + worldController.lives - worldController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(Assets.instance.bunny.head, x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1, 1, 1, 1);
        }
    }

    boolean time_done = false;
    private void Countdown(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 500;
        float y = cameraGUI.viewportHeight - 450;


        timer = (int) (60 - ((System.currentTimeMillis() - startTime) / 1000));
        timeleft = timer;
        BitmapFont fpsFont = Assets.instance.fonts.defaultBig;


        fpsFont.draw(batch, "Time Remaining: " + timer, x, y);
        fpsFont.setColor(1, 0, 0, 1); // white

        if(timer == 0 && !time_done){
            time_done = true;
            renderGuiGameOverMessage(batch);
        }

    }


    public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }


    @Override
    public void dispose() {
        batch.dispose();
    }

    public int get_lives(){
        return worldController.get_number_of_lives();
    }

    public int get_time(){
        return timer;
    }

    public void change_health(int lives){

        if(lives == 3){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 3");
            batch.begin();
            batch.draw(Assets.instance.health.health3, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
            batch.end();
        }
        else if(lives == 2){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 2");
            batch.begin();
            batch.draw(Assets.instance.health.health2, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
            batch.end();
        }
        else if(lives == 1){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 1");
            batch.begin();
            batch.draw(Assets.instance.health.health1, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
            batch.end();
        }
        else if(lives == 0){
            //Gdx.app.log("LOOOOOOOOOOOOOOOOOOOK","Health at 0");
            batch.begin();
            batch.draw(Assets.instance.health.health0, x, y, offsetX, offsetY, 262, 53, 0.35f, -0.35f, 0);
            batch.end();
            //renderGuiGameOverMessage(batch);
           // worldController.backToMenu();
        }
    }

}