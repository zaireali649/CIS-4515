package com.templecis.escaperoute.maze.menu.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ArrayMap;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.components.input.GameKeys;
import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.menu.data.Menu;
import com.templecis.escaperoute.maze.menu.view.MenuItem;

public class MenuControls {
    protected Menu menu;

    public MenuControls(Menu menu) {
        this.menu = menu;
    }


    public void update(MasterMaze game) {
        if(GameKeys.isPressed(GameKeys.UP)) {
            this.menu.moveUp();
        }
        if(GameKeys.isPressed(GameKeys.DOWN)) {
            this.menu.moveDown();
        }
        if(GameKeys.isPressed(GameKeys.ENTER)) {
            this.menu.select();
        }
    }



}
