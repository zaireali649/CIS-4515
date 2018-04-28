package com.templecis.escaperoute.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.templecis.escaperoute.game.objects.BunnyHead;
import com.templecis.escaperoute.game.objects.GoldCoin;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.game.objects.ReverseCoin;
import com.templecis.escaperoute.game.objects.Rock;
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
    private float timeLeftGameOverDelay;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private Rectangle r3 = new Rectangle();
    private Rectangle r4 = new Rectangle();
    private Rectangle r5 = new Rectangle();
    private Rectangle r6 = new Rectangle();
    private Rectangle r7 = new Rectangle();

    public long reverseCoinTime = 0;
    public int reverseCoinDuration;

    SpriteBatch batch;


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

            level = new Level(true);
            cameraHelper.setTarget(level.bunnyHead);
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
        // Rocks
        Vector2 origin = new Vector2();
        for (Rock rock : level.rocks) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            Body body = b2world.createBody(bodyDef);
            rock.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }



    private void spawnCarrots(Vector2 pos, int numCarrots, float radius) {
        /*float carrotShapeScale = 0.5f;
        // create carrots with box2d body and fixture
        for (int i = 0; i < numCarrots; i++) {
            Carrot carrot = new Carrot();
            // calculate random spawn position, rotation, and scale
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
            float carrotScale = MathUtils.random(0.5f, 1.5f);
            carrot.scale.set(carrotScale, carrotScale);
            // create box2d body for carrot with start position and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;
            Body body = b2world.createBody(bodyDef);
            body.setType(BodyDef.BodyType.DynamicBody);
            carrot.body = body;
            // create rectangular shape for carrot to allow interactions (collisions) with other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
            float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);
            // set physics attributes
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            // finally, add new carrot to list for updating/rendering
            level.carrots.add(carrot);
        }*/
    }


    float x = -15;
    float y = -15;
    float offsetX = 50;
    float offsetY = 50;

    public void update(float deltaTime) {
        this.delta_time = deltaTime;
        handleDebugInput(deltaTime);

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
        return level.bunnyHead.position.y < -5;
    }

    private void onCollisionBunnyWithGoal() {
        goalReached = true;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
        Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
        centerPosBunnyHead.x += level.bunnyHead.bounds.width;
        spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX, Constants.CARROTS_SPAWN_RADIUS);
    }

    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;
        if (!cameraHelper.hasTarget(level.bunnyHead)) {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
            // Camera Controls (zoom)
            float camZoomSpeed = 1 * deltaTime;
            float camZoomSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camZoomSpeed *= camZoomSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
        }
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    private void handleInputGame(float deltaTime) {
        if (cameraHelper.hasTarget(level.bunnyHead)) {

            accelX = Gdx.input.getAccelerometerX();
            accelY = Gdx.input.getAccelerometerY();






            // Player Movement
            level.bunnyHead.velocity.y = -accelX * 1000;
            level.bunnyHead.velocity.x = accelY * 1000;

            // Keep moving for testing
            // level.bunnyHead.velocity.y = -1 * 1000;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                level.bunnyHead.velocity.x = 1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                level.bunnyHead.velocity.x = -1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)){
                level.bunnyHead.velocity.y = 1 * 1000;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                level.bunnyHead.velocity.y = -1 * 1000;
            }


            // Reverse Coin Trap Implementation
            if(System.currentTimeMillis() - reverseCoinTime < reverseCoinDuration * 1000){
                level.bunnyHead.velocity.x = level.bunnyHead.velocity.x * -1;
                level.bunnyHead.velocity.y = level.bunnyHead.velocity.y * -1;
                //Gdx.app.debug(TAG, "TIME: " + System.currentTimeMillis());
            }



         /*   if ( accelX < 0) {
                level.bunnyHead.velocity.x = -accelX * 100;
            } else if (accelX > 0) {
                level.bunnyHead.velocity.x = accelX * 100;
            } else if ( accelY < 0){
                level.bunnyHead.velocity.y = -accelY * 100;

            } else if (accelY > 0) {
                level.bunnyHead.velocity.y = accelY * 100;

            }else {
                // Execute auto-forward movement on non-desktop platform

            }*/

            // Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.bunnyHead.setJumping(true);
            } else {
                level.bunnyHead.setJumping(false);
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
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to  Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }



    private void onCollisionBunnyHeadWithRock(Rock rock) {
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            }
            return;
        }
        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
        goldcoin.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
        score += goldcoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
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
                    r6.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width / 100, level.bunnyHead.bounds.height);
                    // Right edge
                    r7.set(level.bunnyHead.position.x + level.bunnyHead.bounds.width - level.bunnyHead.bounds.width / 100, level.bunnyHead.position.y, level.bunnyHead.bounds.width / 100, level.bunnyHead.bounds.height);

                    // Hit wall from right
                    if(r6.overlaps(r2)) {
                        level.bunnyHead.position.x = mazeTile.position.x + mazeTile.dimension.x / 2;
                    }

                    // Hit wall from left
                    if(r7.overlaps(r2)) {
                        level.bunnyHead.position.x = mazeTile.position.x + mazeTile.dimension.x / 2 - level.bunnyHead.bounds.width - mazeTile.dimension.x/mazeTile.thickness;
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
                    r6.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width / 100, level.bunnyHead.bounds.height);
                    // Right edge
                    r7.set(level.bunnyHead.position.x + level.bunnyHead.bounds.width - level.bunnyHead.bounds.width / 100, level.bunnyHead.position.y, level.bunnyHead.bounds.width / 100, level.bunnyHead.bounds.height);

                    // Hit wall from right
                    if(r6.overlaps(r3)) {
                        level.bunnyHead.position.x = mazeTile.position.x - mazeTile.dimension.x / 2 + mazeTile.dimension.x/mazeTile.thickness;
                    }

                    // Hit wall from left
                    if(r7.overlaps(r3)) {
                        level.bunnyHead.position.x = mazeTile.position.x - mazeTile.dimension.x / 2 - level.bunnyHead.bounds.width;
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
                    r6.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width , level.bunnyHead.bounds.height / 100);
                    // Top edge
                    r7.set(level.bunnyHead.position.x, level.bunnyHead.position.y + level.bunnyHead.bounds.height - level.bunnyHead.bounds.height / 100, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height / 100);

                    // Hit wall from top
                    if(r6.overlaps(r4)) {
                        level.bunnyHead.position.y = mazeTile.position.y + mazeTile.dimension.y / 2;
                    }

                    // Hit wall from bottom
                    if(r7.overlaps(r4)) {
                        level.bunnyHead.position.y = mazeTile.position.y + mazeTile.dimension.y / 2 - level.bunnyHead.bounds.height - mazeTile.dimension.y / mazeTile.thickness;
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
                    r6.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width , level.bunnyHead.bounds.height / 100);
                    // Top edge
                    r7.set(level.bunnyHead.position.x, level.bunnyHead.position.y + level.bunnyHead.bounds.height - level.bunnyHead.bounds.height / 100, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height / 100);

                    // Hit wall from top
                    if(r6.overlaps(r5)) {
                        level.bunnyHead.position.y = mazeTile.position.y - mazeTile.dimension.y / 2 + mazeTile.dimension.y/mazeTile.thickness;
                    }

                    // Hit wall from bottom
                    if(r7.overlaps(r5)) {
                        level.bunnyHead.position.y = mazeTile.position.y - mazeTile.dimension.y / 2 - level.bunnyHead.bounds.height ;
                    }
                }
            }
        }
    }


    private void testCollisions() {



        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

        // Test collision: Bunny Head <-> Rocks
        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithRock(rock);
            // IMPORTANT: must do all collisions for valid edge testing on rocks.
        }

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
            level.bunnyHead.position.x = w/2 + level.bunnyHead.dimension.x/2 + 2;
            level.bunnyHead.position.y = h/2 + level.bunnyHead.dimension.y/2 + 2;
        }

        for (Monster monster: level.monsters) {
            r2.set(monster.position.x, monster.position.y, monster.bounds.width, monster.bounds.height);
            if (monster.collected) continue;
            if (!r1.overlaps(r2)) continue;
            Gdx.app.debug(TAG, "Intersect Monster");
            monster.collected = true;
            // Reduce Health
            level.bunnyHead.health = level.bunnyHead.health - monster.getStrength();

            //return level.bunnyHead.health;


        }


        //testMazeTileCollisions();



        /*
        // Test collision: Bunny Head <-> Gold Coins
        for (GoldCoin goldcoin : level.goldcoins) {
            if (goldcoin.collected) continue;
            r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithGoldCoin(goldcoin);
            break;
        }
        // Test collision: Bunny Head <-> Feathers
        for (Feather feather : level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithFeather(feather);
            break;
        }
        */
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

        return level.bunnyHead.health;
    }
    float delta_time;
    public float get_delta_time(){
        return delta_time;
    }
}