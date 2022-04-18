package com.templecis.escaperoute.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.templecis.escaperoute.HUD.HealthBar;
import com.templecis.escaperoute.HUD.LoadingBarWithBorders;
import com.templecis.escaperoute.Maze_Stuff.MazeGenerator;
import com.templecis.escaperoute.game.objects.AbstractGameObject;
import com.templecis.escaperoute.game.objects.Player;
import com.templecis.escaperoute.game.objects.Button;
import com.templecis.escaperoute.game.objects.ButtonType;
import com.templecis.escaperoute.game.objects.Goal;
import com.templecis.escaperoute.game.objects.MazeTile;
import com.templecis.escaperoute.game.objects.Monster;
import com.templecis.escaperoute.game.objects.ReverseCoin;
import com.templecis.escaperoute.game.objects.TrapDoor;

import java.util.Random;

/**
 * Created by Ziggy on 4/19/2018.
 */

public class Level {
    public static final String TAG = Level.class.getName();
    public Player player;
    // objects
    // decoration
    public Array<MazeTile> mazeTiles;
    public Array<ReverseCoin> reverseCoins;
    public Array<Monster> monsters;
    public Array<TrapDoor> trapDoors;
    public Array<Button> buttons;
    public Goal goal;
    //Health bar vars
    private Stage stage;
    private HealthBar healthBar;
    private LoadingBarWithBorders loadingBarWithBorders;

    private Boolean Attacker;

    private long lastUpdate = 0L;


    //end health bar vars

    public Level() {
        Attacker = false;
        init();
    }

    public Level(Boolean Attacker) {
        this.Attacker = Attacker;
        init();
    }

    private void init() {
        // player character
        player = null;
        // objects
        mazeTiles = new Array<MazeTile>();
        reverseCoins = new Array<ReverseCoin>();
        trapDoors = new Array<TrapDoor>();
        monsters = new Array<Monster>();
        buttons = new Array<Button>();

        AbstractGameObject obj;

        // Spawn Maze Tile
        obj = new MazeTile();
        int w = (int) obj.dimension.x;
        int h = (int) obj.dimension.y;
        int row = 7;
        int column = 7;
        int W = row * w;
        int H = column * h;

        obj = new Goal();
        obj.position.set(8,32);
        goal = (Goal) obj;

        // Spawn Player
        obj = new Player();
        obj.position.set(w/2 + obj.dimension.x/2 + 2, h/2 + obj.dimension.y/2 + 2);
        player = (Player) obj;

        // Spawn Reverse Coin
        obj = new ReverseCoin();
        obj.position.set(w/2 + obj.dimension.x/2 + 4, h/2 + obj.dimension.y/2 + 4);
        reverseCoins.add((ReverseCoin) obj);

        // Spawn Trap Door
        obj = new TrapDoor();
        obj.position.set(w/2 + obj.dimension.x/2 + 6, h/2 + obj.dimension.y/2 + 6);
        trapDoors.add((TrapDoor) obj);

        // Spawn Monster
        obj = new Monster();
        obj.position.set(w/2 + obj.dimension.x/2 + 8, h/2 + obj.dimension.y/2 + 8);
        monsters.add((Monster) obj);

        // Spawn Monster
        obj = new Monster();
        obj.position.set(w/2 + obj.dimension.x/2 + 3, h/2 + obj.dimension.y/2 + 3);
        monsters.add((Monster) obj);

        // Spawn Monster
        obj = new Monster();
        obj.position.set(w/2 + obj.dimension.x/2 + 5, h/2 + obj.dimension.y/2 + 5);
        monsters.add((Monster) obj);

        // Spawn Monster
        obj = new Monster();
        obj.position.set(w/2 + obj.dimension.x/2 + 7, h/2 + obj.dimension.y/2 + 7);
        monsters.add((Monster) obj);

        if (Attacker) {

            // Spawn Trap Buttons
            obj = new Button(ButtonType.MONSTER);
            buttons.add((Button) obj);
            obj = new Button(ButtonType.TRAPDOOR);
            buttons.add((Button) obj);
            obj = new Button(ButtonType.REVERSECOIN);
            buttons.add((Button) obj);
        }
        generateMaze();
    }

    int maze_size;
    public void spawn_stuff(int i){
        AbstractGameObject obj;
        obj = new MazeTile();
        int w = (int) obj.dimension.x;
        int h = (int) obj.dimension.y;

        Random rng = new Random();
        int x_offset = (rng.nextInt(maze_size))+1;
        int y_offset = (rng.nextInt((maze_size*4)))+1;
        if(i == 1){
            obj = new ReverseCoin();
            obj.position.set(w/2 + obj.dimension.x/2 + x_offset*2, h/2 + obj.dimension.y/2 + y_offset*2);
            reverseCoins.add((ReverseCoin) obj);
        }
        else if(i == 2){
            obj = new TrapDoor();
            obj.position.set(w/2 + obj.dimension.x/2 + x_offset*2, h/2 + obj.dimension.y/2 + y_offset*2);
            trapDoors.add((TrapDoor) obj);
        }
        else if(i == 3){
            obj = new Monster();
            obj.position.set(w/2 + obj.dimension.x/2 + x_offset*2, h/2 + obj.dimension.y/2 + y_offset*2);
            monsters.add((Monster) obj);
        }
    }

    private Array<MazeTile> generateMaze() {
        //YourGraphicMaze yourGraphicMaze = new YourGraphicMaze();
    //Maze maze = new Maze();
        //mazeTiles = yourGraphicMaze.CreatePath(maze, 1, 1, 4, 4, path);

        MazeGenerator mg = new MazeGenerator(4);

        Gdx.app.debug(TAG, "North Size: " + mg.north.length);

        for (int x = 1; x < mg.north.length-1; x++) {
            for (int y = 1; y < mg.north[0].length-1; y++) {
                MazeTile mt =  new MazeTile();
                mt.position.set(mt.dimension.x + (x * 2), mt.dimension.y + (y * 2));

                mt.topWall = mg.north[x][y];
                mt.rightWall = mg.east[x][y];
                mt.bottomWall = mg.south[x][y];
                mt.leftWall = mg.west[x][y];
                mazeTiles.add(mt);
            }
        }
    mg = null;
        //mg.draw();
        //maze.solve();



        //return mazeTiles;
        return null;
    }

    public void render(SpriteBatch batch) {
        // Draw Goal

                // Draw Maze Tiles
        for (MazeTile mazeTile : mazeTiles)
            mazeTile.render(batch);

        for (ReverseCoin reverseCoin : reverseCoins)
            reverseCoin.render(batch);

        for (TrapDoor trapDoor: trapDoors)
            trapDoor.render(batch);

        for (Monster monster: monsters)
            monster.render(batch);

        for (Button button: buttons) {
            if (button.bt == ButtonType.MONSTER){
                button.position.x = player.position.x - 2;
                button.position.y = (float) (player.position.y - 1.5);
            }
            else if (button.bt == ButtonType.TRAPDOOR){
                button.position.x = player.position.x;
                button.position.y = (float) (player.position.y - 1.5);
            }
            else if (button.bt == ButtonType.REVERSECOIN){
                button.position.x = player.position.x + 2;
                button.position.y = (float) (player.position.y - 1.5);
            }
            button.render(batch);
        }

        // Draw Player Character
        player.render(batch);


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
        player.update(deltaTime);

    }


}
