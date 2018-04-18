package com.templecis.escaperoute.maze.menu.data;

import com.badlogic.gdx.utils.ArrayMap;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.menu.actions.ExitAction;
import com.templecis.escaperoute.maze.menu.actions.MenuAction;
import com.templecis.escaperoute.maze.menu.actions.ScreenAction;

public class MainMenu extends Menu {

    public MainMenu(MasterMaze game) {
        super(game);
        ArrayMap<String, MenuAction> items = new ArrayMap<String, MenuAction>();
        items.put("Start", new ScreenAction(game, "game"));
        items.put("Highscores", new ScreenAction(game, "highscore"));
        items.put("Exit", new ExitAction());
        this.setMenuItems(items);
    }


}
