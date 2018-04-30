package com.templecis.escaperoute.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.templecis.escaperoute.game.objects.Player;
import com.templecis.escaperoute.game.objects.Button;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.game.objects.ReverseCoin;
import com.templecis.escaperoute.game.objects.TrapDoor;
import com.templecis.escaperoute.screens.DirectedGame;
import com.templecis.escaperoute.screens.MenuScreen;
import com.templecis.escaperoute.screens.transitions.ScreenTransition;
import com.templecis.escaperoute.screens.transitions.ScreenTransitionSlide;
import com.templecis.escaperoute.util.AudioManager;
import com.templecis.escaperoute.util.CameraHelper;
import com.templecis.escaperoute.util.Constants;


/**
 * Created by Ziggy on 4/19/2018.
 */

/*



 */

public class WorldController extends InputAdapter implements Disposable {
    private static final String TAG = WorldController.class.getName();
    public CameraHelper cameraHelper;
    public Level level;
    public int lives;
    public int score;
    public int w;
    public int h;
    public float livesVisual;
    public float scoreVisual;
    public World b2world;
    public int mana = 8;
    public int frames;

    private float timeLeftGameOverDelay;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private Rectangle r3 = new Rectangle();
    private Rectangle r4 = new Rectangle();
    private Rectangle r5 = new Rectangle();
    private Rectangle r6 = new Rectangle();
    private Rectangle r7 = new Rectangle();

    long startTime = System.currentTimeMillis();
    int manatimer;


    public long reverseCoinTime = 0;
    public int reverseCoinDuration;

    private DirectedGame game;
    private boolean goalReached;
    float gyroX, gyroY, gyroZ;
    float accelX, accelY;
    private Boolean attacker;

    public WorldController(DirectedGame game, Boolean attacker) {
        this.game = game;
        this.attacker = attacker;
        init();
    }

    public WorldController(DirectedGame game) {
        this.game = game;
        this.attacker = false;
        init();
    }

    private void init() {
        cameraHelper = new CameraHelper();

        boolean gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        boolean accelAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

        if(gyroscopeAvail){
            gyroX = Gdx.input.getGyroscopeX();
            gyroY = Gdx.input.getGyroscopeY();
            gyroZ = Gdx.input.getGyroscopeZ();
        }

        if (accelAvail){

        }
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel() {
   /*     score = 0;
        scoreVisual = score;
        goalReached = false;
        reverseCoinDuration = new ReverseCoin().getDuration();

        w = (int) new MazeTile().dimension.x;
        h = (int) new MazeTile().dimension.y;

        level = new Level(true);
        cameraHelper.setTarget(level.bunnyHead);
        initPhysics(); */

       // if(!attacker){
            score = 0;
            scoreVisual = score;
            goalReached = false;
            reverseCoinDuration = new ReverseCoin().getDuration();

            w = (int) new MazeTile().dimension.x;
            h = (int) new MazeTile().dimension.y;

            level = new Level(attacker);
            cameraHelper.setTarget(level.player);
            initPhysics();
      //  }
      //  else if(attacker){
            //init attacker stuff
       // }
    }

    @Override
    public void dispose() {
        if (b2world != null) b2world.dispose();
    }

    private void initPhysics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
    }



    public void update(float deltaTime) {
        this.delta_time = deltaTime;

        // Game Over Conditions
        if (isGameOver() || goalReached) {
            timeLeftGameOverDelay -= deltaTime;
//            if (timeLeftGameOverDelay < 0) backToMenu();
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        //can get int lives from here
        testCollisions();


        b2world.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);

        // Lose Life From Water
        if (!isGameOver() && isPlayerInWater()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }

        //level.mountains.updateScrollPosition(cameraHelper.getPosition());
        if (livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if (scoreVisual < score)
            scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
    }

    public boolean isGameOver() {
        return (get_number_of_lives() <= 0);
    }

    public void backToMenu() {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        Gdx.app.log("ABOUT TO","DO STUFF");
        game.setScreen(new MenuScreen(game), transition);
        Gdx.app.log("STUFF","DONE STUFF");
    }

    public boolean isPlayerInWater() {
        return level.player.position.y < -5;
    }

    private void onCollisionBunnyWithGoal() {
        goalReached = true;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
        Vector2 centerPosBunnyHead = new Vector2(level.player.position);
        centerPosBunnyHead.x += level.player.bounds.width;
    }


    private void handleInputGame(float deltaTime) {
        if (cameraHelper.hasTarget(level.player)) {

            accelX = Gdx.input.getAccelerometerX();
            accelY = Gdx.input.getAccelerometerY();

            // Player Movement
            level.player.velocity.y = -accelX * 1000;
            level.player.velocity.x = accelY * 1000;

            // Keep moving for testing
            // level.bunnyHead.velocity.y = -1 * 1000;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                level.player.velocity.x = 1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                level.player.velocity.x = -1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)){
                level.player.velocity.y = 1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                level.player.velocity.y = -1 * 1000;
            }


            // Reverse Coin Trap Implementation
            if(System.currentTimeMillis() - reverseCoinTime < reverseCoinDuration * 1000){
                level.player.velocity.x = level.player.velocity.x * -1;
                level.player.velocity.y = level.player.velocity.y * -1;
            }
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.player);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to  Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }

    private void testMazeTileCollisions() {
        for (MazeTile mazeTile: level.mazeTiles) {
            if (mazeTile.rightWall) {
                r2.set(mazeTile.position.x + mazeTile.dimension.x/2 - mazeTile.dimension.x/mazeTile.thickness, mazeTile.position.y - mazeTile.dimension.y/2, mazeTile.dimension.x/mazeTile.thickness, mazeTile.dimension.y);
                if (r1.overlaps(r2)) // Right Wall
                {
                    Gdx.app.debug(TAG, "Intersect Right Wall");
                    // Left edge
                    //(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
                    r6.set(level.player.position.x, level.player.position.y, level.player.bounds.width / 100, level.player.bounds.height);
                    // Right edge
                    r7.set(level.player.position.x + level.player.bounds.width - level.player.bounds.width / 100, level.player.position.y, level.player.bounds.width / 100, level.player.bounds.height);

                    // Hit wall from right
                    if(r6.overlaps(r2)) {
                        level.player.position.x = mazeTile.position.x + mazeTile.dimension.x / 2;
                    }

                    // Hit wall from left
                    if(r7.overlaps(r2)) {
                        level.player.position.x = mazeTile.position.x + mazeTile.dimension.x / 2 - level.player.bounds.width - mazeTile.dimension.x/mazeTile.thickness;
                    }
                }
            }

            if (mazeTile.leftWall) {
                r3.set(mazeTile.position.x - mazeTile.dimension.x/2, mazeTile.position.y - mazeTile.dimension.y/2, mazeTile.dimension.x/mazeTile.thickness, mazeTile.dimension.y);
                if (r1.overlaps(r3)) // Left Wall
                {
                    Gdx.app.debug(TAG, "Intersect Left Wall");

                    // Left edge
                    //(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
                    r6.set(level.player.position.x, level.player.position.y, level.player.bounds.width / 100, level.player.bounds.height);
                    // Right edge
                    r7.set(level.player.position.x + level.player.bounds.width - level.player.bounds.width / 100, level.player.position.y, level.player.bounds.width / 100, level.player.bounds.height);

                    // Hit wall from right
                    if(r6.overlaps(r3)) {
                        level.player.position.x = mazeTile.position.x - mazeTile.dimension.x / 2 + mazeTile.dimension.x/mazeTile.thickness;
                    }

                    // Hit wall from left
                    if(r7.overlaps(r3)) {
                        level.player.position.x = mazeTile.position.x - mazeTile.dimension.x / 2 - level.player.bounds.width;
                    }
                }
            }

            if (mazeTile.topWall) {
                r4.set(mazeTile.position.x - mazeTile.dimension.x / 2, mazeTile.position.y + mazeTile.dimension.y / 2 - mazeTile.dimension.y / mazeTile.thickness, mazeTile.dimension.x, mazeTile.dimension.y / mazeTile.thickness);
                if (r1.overlaps(r4)) // Top Wall
                {
                    Gdx.app.debug(TAG, "Intersect Top Wall");

                    // Bottom edge
                    //(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
                    r6.set(level.player.position.x, level.player.position.y, level.player.bounds.width , level.player.bounds.height / 100);
                    // Top edge
                    r7.set(level.player.position.x, level.player.position.y + level.player.bounds.height - level.player.bounds.height / 100, level.player.bounds.width, level.player.bounds.height / 100);

                    // Hit wall from top
                    if(r6.overlaps(r4)) {
                        level.player.position.y = mazeTile.position.y + mazeTile.dimension.y / 2;
                    }

                    // Hit wall from bottom
                    if(r7.overlaps(r4)) {
                        level.player.position.y = mazeTile.position.y + mazeTile.dimension.y / 2 - level.player.bounds.height - mazeTile.dimension.y / mazeTile.thickness;
                    }
                }
            }

            if (mazeTile.bottomWall) {
                r5.set(mazeTile.position.x - mazeTile.dimension.x/2, mazeTile.position.y - mazeTile.dimension.y/2, mazeTile.dimension.x, mazeTile.dimension.y/mazeTile.thickness);
                if (r1.overlaps(r5)) // Bottom Wall
                {
                    Gdx.app.debug(TAG, "Intersect Bottom Wall");

                    // Bottom edge
                    //(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
                    r6.set(level.player.position.x, level.player.position.y, level.player.bounds.width , level.player.bounds.height / 100);
                    // Top edge
                    r7.set(level.player.position.x, level.player.position.y + level.player.bounds.height - level.player.bounds.height / 100, level.player.bounds.width, level.player.bounds.height / 100);

                    // Hit wall from top
                    if(r6.overlaps(r5)) {
                        level.player.position.y = mazeTile.position.y - mazeTile.dimension.y / 2 + mazeTile.dimension.y/mazeTile.thickness;
                    }

                    // Hit wall from bottom
                    if(r7.overlaps(r5)) {
                        level.player.position.y = mazeTile.position.y - mazeTile.dimension.y / 2 - level.player.bounds.height ;
                    }
                }
            }
        }
    }


    private void testCollisions() {


        r1.set(level.player.position.x, level.player.position.y, level.player.bounds.width, level.player.bounds.height);



        for (ReverseCoin reverseCoin: level.reverseCoins) {
            r2.set(reverseCoin.position.x, reverseCoin.position.y, reverseCoin.bounds.width, reverseCoin.bounds.height);
            if (reverseCoin.collected) continue;
            if (!r1.overlaps(r2)) continue;
            Gdx.app.debug(TAG, "Intersect Reverse Coin");
            reverseCoin.collected = true;
            reverseCoinTime = System.currentTimeMillis();
        }

        for (TrapDoor trapDoor: level.trapDoors) {
            r2.set(trapDoor.position.x, trapDoor.position.y, trapDoor.bounds.width, trapDoor.bounds.height);
            if (trapDoor.collected) continue;
            if (!r1.overlaps(r2)) continue;
            Gdx.app.debug(TAG, "Intersect Trap Door");
            trapDoor.collected = true;
            // Send Back to Start
            level.player.position.x = w/2 + level.player.dimension.x/2 + 2;
            level.player.position.y = h/2 + level.player.dimension.y/2 + 2;
        }

        for (Monster monster: level.monsters) {
            r2.set(monster.position.x, monster.position.y, monster.bounds.width, monster.bounds.height);
            if (monster.collected) continue;
            if (!r1.overlaps(r2)) continue;
            Gdx.app.debug(TAG, "Intersect Monster");
            monster.collected = true;
            // Reduce Health
            level.player.health = level.player.health - monster.getStrength();

            //return level.bunnyHead.health;


        }
        //testMazeTileCollisions();


        // Test collision: Bunny Head <-> Goal
        if (!goalReached) {
            r2.set(level.goal.bounds);
            r2.x += level.goal.position.x;
            r2.y += level.goal.position.y;
            if (r1.overlaps(r2)) onCollisionBunnyWithGoal();
        }
        //return 0;
    }

    public int get_number_of_lives(){

        return level.player.health;
    }
    float delta_time;
    public float get_delta_time(){
        return delta_time;
    }


    public void trapClick(OrthographicCamera camera) {
        for (Button button: level.buttons) {
            r2.set(button.position.x, button.position.y, button.bounds.width, button.bounds.height);
            Vector3 vec = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(vec);

            if (Gdx.input.justTouched()){
                if (r2.contains(vec.x, vec.y)){
                    Gdx.app.debug(TAG, "Clicked " + button.bt + " Trap Button");
                    if(button.bt == button.get_coin_type() && mana > 0){
                        mana -= 1;
                        //level.spawn_stuff(1);
                    }
                    else if(button.bt == button.get_monster_type() && mana > 1){
                        mana -= 2;
                        //level.spawn_stuff(3);
                    }
                    else if(button.bt == button.get_trap_type() && mana > 4){
                        mana -= 5;
                        //level.spawn_stuff(2);
                    }
                }
            }
        }
    }

    public int return_mana(){
        manatimer = (int) (((System.currentTimeMillis() - startTime) / 1000));

        frames ++;

        if(frames % 250 == 0 && mana < 8){
            mana++;
        }


        Gdx.app.log("MANA TIMER", "" + manatimer);


        return mana;
    }


}