package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.heartsoul.Main;
import com.heartsoul.SoundManager;

public abstract class BaseScreen implements Screen {
    private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;
    private static final int BOTTOM_UI_HEIGHT = 40;
    private static final int TOP_UI_HEIGHT = 100;

    protected final Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;

    private Texture background;
    private Stage stage;
    private InputAdapter escListener;


    public BaseScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();
        initializeCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    }

    protected void initializeCamera(float width, float height) {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);
        this.viewport = new FitViewport(width, height, this.camera);
    }

    @Override
    public void show() {
        // Implementación por defecto vacía
    }

    @Override
    public void resize(int width, int height) {
        if (this.viewport != null) {
            this.viewport.update(width, height, true);
        }
    }

    protected void renderBackground() {
        if (this.background != null) {
            this.batch.setProjectionMatrix(this.camera.combined);
            this.batch.begin();
            this.batch.draw(this.background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            this.batch.end();
        }
    }

    protected void initializeStage() {
        this.stage = new Stage(this.viewport, this.batch);
        this.game.addInputProcessor(this.stage);
    }

    protected void disposeStage() {
        if (this.stage != null) {
            this.game.removeInputProcessor(this.stage);
            this.stage.dispose();
            this.stage = null;
        }
    }

    protected void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void registerESC() {
        this.escListener = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new IntroScreen(game));
                    return true;
                }
                return false;
            }
        };
        this.game.addInputProcessor(this.escListener);
    }

    protected void unregisterESC() {
        if (this.escListener != null) {
            this.game.removeInputProcessor(this.escListener);
            this.escListener = null;
        }
    }

    protected TextButton createButton(String text, TextButton.TextButtonStyle style, Runnable onClick) {
        TextButton button = new TextButton(text, style);
        button.setTransform(true);
        button.setOrigin(Align.center);

        // Efecto hover
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.clearActions();
                button.addAction(Actions.scaleTo(1.2f, 1.2f, 0.2f, Interpolation.fade));
                SoundManager.getInstance().playSound("sounds/hover.mp3");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.clearActions();
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f, Interpolation.fade));
            }
        });

        // Click
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });

        return button;
    }

    @Override
    public void pause() {
        // Implementación por defecto vacía
    }

    @Override
    public void resume() {
        // Implementación por defecto vacía
    }

    @Override
    public void hide() {
        disposeStage();
        unregisterESC();
    }

    @Override
    public void dispose() {
        disposeStage();
        unregisterESC();
        if (this.background != null) {
            this.background.dispose();
            this.background = null;
        }
    }

    protected void setBackground(Texture background) {
        this.background = background;
    }

    protected Stage getStage() {
        return this.stage;
    }

    public Main getGame() {
        return this.game;
    }

    public int getVirtualWidth() {
        return VIRTUAL_WIDTH;
    }

    public int getVirtualHeight() {
        return VIRTUAL_HEIGHT;
    }

    public int getBottomBarHeight() {
        return BOTTOM_UI_HEIGHT;
    }

    public int getTopBarHeight() {
        return TOP_UI_HEIGHT;
    }

    public int getPlayableHeight() {
        return VIRTUAL_HEIGHT - BOTTOM_UI_HEIGHT - TOP_UI_HEIGHT;
    }
}
