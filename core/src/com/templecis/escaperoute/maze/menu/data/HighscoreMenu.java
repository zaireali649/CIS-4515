package com.templecis.escaperoute.maze.menu.data;

import com.badlogic.gdx.utils.ArrayMap;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.menu.actions.MenuAction;
import com.templecis.escaperoute.maze.menu.actions.ScreenAction;
import com.templecis.escaperoute.maze.screens.MainMenuScreen;

public class HighscoreMenu extends Menu {

    public HighscoreMenu(MasterMaze game) {
        super(game);
        ArrayMap<String, MenuAction> items = new ArrayMap<String, MenuAction>();
        items.put("Back", new ScreenAction(game, "mainMenu"));
        this.setMenuItems(items);
    }
}
