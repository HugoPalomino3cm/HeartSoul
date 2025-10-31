package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public abstract class BaseScreen implements Screen {
    protected final Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;
    protected Texture background;
    protected Music bgMusic;
    protected Stage stage;
    protected InputAdapter escListener;

    protected static final int VIRTUAL_WIDTH = 1920;
    protected static final int VIRTUAL_HEIGHT = 1080;
    protected static final int BOTTOM_UI_HEIGHT = 40;
    protected static final int TOP_UI_HEIGHT = 100;

    public BaseScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();
        initializeCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    }

    protected void initializeCamera(float width, float height) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        viewport = new FitViewport(width, height, camera);
    }

    @Override
    public void show() {
        // Implementación por defecto vacía
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    protected void renderBackground() {
        if (background != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            batch.end();
        }
    }

    protected void playMusic(String path, boolean loop) {
        stopMusic();
        try {
            bgMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
            bgMusic.setLooping(loop);
            bgMusic.play();
        } catch (Exception e) {
            Gdx.app.error("BaseScreen", "Error en cargar la música: " + path);
        }
    }

    protected void stopMusic() {
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.dispose();
            bgMusic = null;
        }
    }

    protected void initializeStage() {
        stage = new Stage(viewport, batch);
        game.addInputProcessor(stage);
    }

    protected void disposeStage() {
        if (stage != null) {
            game.removeInputProcessor(stage);
            stage.dispose();
            stage = null;
        }
    }

    protected void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void registerESC() {
        escListener = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new IntroScreen(game));
                    return true;
                }
                return false;
            }
        };
        game.addInputProcessor(escListener);
    }

    protected void unregisterESC() {
        if (escListener != null) {
            game.removeInputProcessor(escListener);
            escListener = null;
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
                try {
                    Gdx.audio.newSound(Gdx.files.internal("sounds/hover_sound.mp3")).play();
                } catch (Exception e) {
                    // Ignorar si no existe el sonido
                }
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
        stopMusic();
        disposeStage();
        unregisterESC();
    }

    @Override
    public void dispose() {
        stopMusic();
        disposeStage();
        unregisterESC();
        if (background != null) {
            background.dispose();
            background = null;
        }
    }

    public Main getGame() {
        return game;
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
