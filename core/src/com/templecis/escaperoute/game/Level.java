package com.templecis.escaperoute.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.HUD.HealthBar;
import com.templecis.escaperoute.HUD.LoadingBarWithBorders;
import com.templecis.escaperoute.Maze_Stuff.Maze;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.game.objects.BunnyHead;
import com.templecis.escaperoute.game.objects.Carrot;
import com.templecis.escaperoute.game.objects.Clouds;
import com.templecis.escaperoute.game.objects.Feather;
import com.templecis.escaperoute.game.objects.Goal;
import com.templecis.escaperoute.game.objects.GoldCoin;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Mountains;
import com.templecis.escaperoute.game.objects.Rock;
import com.templecis.escaperoute.game.objects.WaterOverlay;

import java.util.concurrent.TimeUnit;


/**
 * Created by Ziggy on 4/19/2018.
 */

public class Level {
    public static final String TAG = Level.class.getName();
    public BunnyHead bunnyHead;



    // objects
    public Array<Rock> rocks;
    // decoration
    //public Clouds clouds;
    //public Mountains mountains;
    //public WaterOverlay waterOverlay;
    //public Array<Carrot> carrots;
    public Array<MazeTile> mazeTiles;
    public Goal goal;
    //Health bar vars
    private Stage stage;
    private HealthBar healthBar;
    private LoadingBarWithBorders loadingBarWithBorders;

    private long lastUpdate = 0L;

    //end health bar vars

    public Level(String filename) {
        init(filename);
    }

    private void init(String filename) {
        // player character
        bunnyHead = null;
        // objects
        rocks = new Array<Rock>();
        mazeTiles = new Array<MazeTile>();

        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));



        // scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj;
                float offsetHeight;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match
                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                }
                // rock
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                }
                // player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    obj = new BunnyHead();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bunnyHead = (BunnyHead) obj;


                }
                // maze tiles
                else if (BLOCK_TYPE.ITEM_MAZE_TILE.sameColor(currentPixel)) {
                    obj = new MazeTile();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    mazeTiles.add((MazeTile) obj);
                    //goldcoins.add((GoldCoin) obj);
                }
                // feather
                else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    //feathers.add((Feather) obj);
                }
                // gold coin
                else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    //goldcoins.add((GoldCoin) obj);
                }
                // goal
                else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) {
                    obj = new Goal();
                    offsetHeight = -7.0f;
                    obj.position.set(pixelX, baseHeight + offsetHeight);
                    goal = (Goal) obj;
                }
                // unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;


            }
        }

        // Spawn Maze Tile
        AbstractGameObject obj = new MazeTile();
        int w = (int) obj.dimension.x;
        int h = (int) obj.dimension.y;
        float offsetHeight;


        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY = pixelY + h) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX = pixelX + w) {
                obj = new MazeTile();
                offsetHeight = -1.5f;
                //obj.position.set(pixelX, pixelY * obj.dimension.y + offsetHeight);
                obj.position.set(pixelX, pixelY);
                mazeTiles.add((MazeTile) obj);
            }
        }







        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }

    public void render(SpriteBatch batch) {
        // Draw Goal
        goal.render(batch);
        // Draw Rocks
        for (Rock rock : rocks) {
            rock.render(batch);
        }
                // Draw Maze Tiles
        for (MazeTile mazeTile : mazeTiles)
            mazeTile.render(batch);
        // Draw Player Character
        bunnyHead.render(batch);


        //Health Bar RENDER START

//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        if (System.currentTimeMillis() - lastUpdate > TimeUnit.SECONDS.toMillis(5)) {
//            healthBar.setValue(healthBar.getValue() - 0.1f);
//            loadingBarWithBorders.setValue(loadingBarWithBorders.getValue() + 0.1f);
//            lastUpdate = System.currentTimeMillis();
//        }
//
//        stage.draw();
//        stage.act();

        //Health Bar RENDER END
    }

    public void update(float deltaTime) {
        bunnyHead.update(deltaTime);
        for (Rock rock : rocks)
            rock.update(deltaTime);
        for (MazeTile mazeTile : mazeTiles)
            mazeTile.update(deltaTime);
    }

    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), // white
        ITEM_FEATHER(255, 0, 255), // purple
        ITEM_MAZE_TILE(0, 0, 255), // blue
        GOAL(255, 0, 0), // red
        ITEM_GOLD_COIN(255, 255, 0); // yellow

        private int color;

        BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }
}
