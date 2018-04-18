package com.templecis.escaperoute.maze.menu.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.templecis.escaperoute.maze.assets.Assets;
import com.templecis.escaperoute.maze.menu.input.MenuControls;

public class MenuItem {;
    private TextButton item;

    public MenuItem(String text) {
        TextButton.TextButtonStyle itemStyle = new TextButton.TextButtonStyle();
        itemStyle.font = Assets.getFont("visitor2", 42);;
        //itemStyle.downFontColor = Color.GREEN;
        //itemStyle.overFontColor = Color.GREEN;
        itemStyle.checkedFontColor = Color.GREEN;
        itemStyle.fontColor = Color.CYAN;
        this.item = new TextButton(text, itemStyle);
    }

    public TextButton getItem() {
        return this.item;
    }

    @Override
    public String toString() {
        return String.valueOf(this.item.getText());
    }
}
