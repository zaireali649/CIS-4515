package com.templecis.escaperoute.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Ziggy on 3/29/2018.
 */

public interface ScreenTransition {
    public float getDuration();

    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}