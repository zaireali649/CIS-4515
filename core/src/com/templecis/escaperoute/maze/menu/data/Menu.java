package com.templecis.escaperoute.maze.menu.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ArrayMap;
import com.templecis.escaperoute.maze.Config;
import com.templecis.escaperoute.maze.MasterMaze;
import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.menu.actions.MenuAction;
import com.templecis.escaperoute.maze.menu.input.MenuControls;
import com.templecis.escaperoute.maze.screens.GameScreen;
import com.templecis.escaperoute.maze.screens.HighscoreScreen;

public abstract class Menu {
    private int currentItem;
    private ArrayMap<String, MenuAction> menuItems = null;
    private String title;
    private MasterMaze game;
    private MenuControls controls;

    public Menu(MasterMaze game) {
        this.controls = new MenuControls(this);
        this.game = game;
        this.title = Config.TITLE;
    }

    public void setMenuItems(ArrayMap<String, MenuAction> menuItems) {
        this.menuItems = menuItems;
    }

    public void moveUp() {
        if (this.currentItem > 0) this.currentItem--;
    }

    public void moveDown() {
        if (this.currentItem < menuItems.size) this.currentItem++;
    }

    public void select() {
        menuItems.getValueAt(currentItem).act();
    }

    public void setCurrentItem(int item) {
        this.currentItem = item;
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public String getTitle() {
        return this.title;
    }

    public ArrayMap<String, MenuAction> getMenuItems() {
        return this.menuItems;
    }

    public String[] getMenuItemKeys() {
        if(menuItems == null) return new String[0];
        String[] items = new String[menuItems.size];
        for(int i = 0; i < menuItems.size; i++) {
            items[i] = menuItems.getKeyAt(i);
        }
        return items;
    }

    public MenuControls getControls() {
        return controls;
    }
}
