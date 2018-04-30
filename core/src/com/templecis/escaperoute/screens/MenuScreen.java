package com.templecis.escaperoute.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.templecis.escaperoute.EscapeRouteMain;
import com.templecis.escaperoute.screens.transitions.ScreenTransition;
import com.templecis.escaperoute.screens.transitions.ScreenTransitionFade;
import com.templecis.escaperoute.util.Constants;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Ziggy on 3/29/2018.
 */

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();
    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private Stage stage;
    private Skin skinCanyonBunny;
    private Skin skinLibgdx;
    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgBunny;
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    private boolean debugEnabled = false;
    private float debugRebuildStage;

    EscapeRouteMain main;

    public MenuScreen(DirectedGame game) {
        super(game);
        this.main = (EscapeRouteMain) game;
        if (!main.actionResolver.getSignedInGPGS()){
            main.actionResolver.loginGPGS();
        }
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.setDebugAll(false);
        stage.draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinCanyonBunny.dispose();
        skinLibgdx.dispose();
    }

    @Override
    public void pause() {
    }

    private void rebuildStage() {
        skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
    }




    private Table buildBackgroundLayer() {
        Table layer = new Table();
        // + Background
        imgBackground = new Image(skinCanyonBunny, "background");
        layer.add(imgBackground);
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setOrigin(imgCoins.getWidth() / 2,
                imgCoins.getHeight() / 2);
        imgCoins.addAction(sequence(
                moveTo(135, -20),
                scaleTo(0, 0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(0, 100, 0.5f, Interpolation.swingOut),
                        scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
                        alpha(1.0f, 0.5f))));
        // + Bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.addAction(sequence(moveTo(655, 510),
                delay(4.0f),
                moveBy(-70, -100, 0.5f, Interpolation.fade),
                moveBy(-100, -50, 0.5f, Interpolation.fade),
                moveBy(-150, -300, 1.0f, Interpolation.elasticIn)));
        return layer;
    }

    private Table buildLogosLayer() {
        Table layer = new Table();
        layer.left().top();
        // + Game Logo
        imgLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + Info Logos
        imgInfo = new Image(skinCanyonBunny, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right().bottom();
        // + Play Button
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled)
            layer.debug();
        return layer;
    }




    private void onPlayClicked() {
        if (!main.actionResolver.getSignedInGPGS()){
            main.actionResolver.loginGPGS();
        }
        else{
            ScreenTransition transition = ScreenTransitionFade.init(0.75f);
            game.setScreen(new EscaperGameScreen(game), transition);
        }
    }

    private void onOptionsClicked() {
        if (!main.actionResolver.getSignedInGPGS()){
            main.actionResolver.loginGPGS();
        }
        else {
            ScreenTransition transition = ScreenTransitionFade.init(0.75f);
            game.setScreen(new AttackerGameScreen(game), transition);
        }
    }


}