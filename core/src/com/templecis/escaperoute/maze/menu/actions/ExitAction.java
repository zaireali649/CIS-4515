package com.templecis.escaperoute.maze.menu.actions;

import com.badlogic.gdx.Gdx;

public class ExitAction implements MenuAction {

    @Override
    public void act() {
        Gdx.app.exit();
    }
}
