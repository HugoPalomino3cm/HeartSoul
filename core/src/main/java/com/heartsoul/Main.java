package com.heartsoul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.heartsoul.screens.IntroScreen;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static final String GAME_NAME = "HeartSoul";

    private SpriteBatch batch;
    private BitmapFont smallFont;
    private BitmapFont mediumFont;
    private BitmapFont largeFont;
    private int highScore;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        this.highScore = 0;
        this.batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/PixelifySans-VariableFont_wght.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.color = Color.WHITE;

        parameter.size = 32;
        this.smallFont = generator.generateFont(parameter);

        parameter.size = 48;
        this.mediumFont = generator.generateFont(parameter);

        parameter.size = 72;
        this.largeFont = generator.generateFont(parameter);

        generator.dispose();

        // Preparar InputMultiplexer para permitir múltiples processors (escenas + atajos globales)
        this.inputMultiplexer = new InputMultiplexer();
        // Añadir listener para F11 dentro del multiplexer
        this.inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.F11) {
                    if (Gdx.graphics.isFullscreen()) {
                        Gdx.graphics.setWindowedMode(1200, 800);
                    } else {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    }
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(this.inputMultiplexer);

        setScreen(new IntroScreen(this));
    }

    // Permite a pantallas/stages añadir su InputProcessor al multiplexer
    public void addInputProcessor(InputProcessor p) {
        if (this.inputMultiplexer != null) {
            this.inputMultiplexer.addProcessor(p);
        } else {
            Gdx.input.setInputProcessor(p);
        }
    }

    public void removeInputProcessor(InputProcessor p) {
        if (this.inputMultiplexer != null) {
            this.inputMultiplexer.removeProcessor(p);
        }
    }

    public BitmapFont getSmallFont() {
        return this.smallFont;
    }

    public BitmapFont getMediumFont() {
        return this.mediumFont;
    }

    public BitmapFont getLargeFont() {
        return this.largeFont;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public String getGameName() {
        return GAME_NAME;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.smallFont.dispose();
        this.mediumFont.dispose();
        this.largeFont.dispose();
        SoundManager.getInstance().dispose();
    }
}
