package com.templecis.escaperoute.util.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Object{
    //Creates the Texture or image of our object
    private Texture myTexture = new Texture("object.png");
    //Gets X location of the courser upon spawning, corrects x loc to be in middle of cursor
    private int xLoc = Gdx.input.getX() - 10;
    //Gets Y location of the courser upon spawning, corrects y loc to be in middle of cursor
    private int yLoc = Gdx.input.getY() + 10;
    //Defines variables to store the Velocity
    private int xSpeed;
    private int ySpeed;

    //This relieves the random velocity given to the object upon creation
    public Object(int xspeed, int yspeed){
        //Sets the velocity sent to it, to the variables defined above
        this.xSpeed = xspeed;
        this.ySpeed = yspeed;
    }
    //This updates the location of our object by adding the speed of each direction to the current location
    public void Update(){
        this.xLoc += xSpeed;
        this.yLoc += ySpeed;
    }
}