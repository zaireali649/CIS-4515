package com.templecis.escaperoute.screens.transitions;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.templecis.escaperoute.States.GameStateManager;
import com.templecis.escaperoute.States.State;
import com.templecis.escaperoute.util.MazeGenerator;


public class Maze_Screen extends State {
    MazeGenerator maze_gen = new MazeGenerator();



    public Maze_Screen(GameStateManager gsm){
        super(gsm);

        maze_gen.generate(10,10,3,3,1,1);


    }

    @Override
    public void handleInput(){

    }

    @Override
    public void update(float dt) {

    }


    @Override
    public void render(SpriteBatch sb){

    }





}
