package com.heartsoul;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private BitmapFont smallFont;
    private BitmapFont mediumFont;
    private BitmapFont largeFont;
    private String gameName = "HeartSoul";
    private int highScore;

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("ui/PixelifySans-VariableFont_wght.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.color = Color.WHITE;

        parameter.size = 32;
        smallFont = generator.generateFont(parameter);

        parameter.size = 48;
        mediumFont = generator.generateFont(parameter);

        parameter.size = 72;
        largeFont = generator.generateFont(parameter);

        generator.dispose();

        setScreen(new IntroScreen(this));
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }

    public BitmapFont getMediumFont() {
        return mediumFont;
    }

    public BitmapFont getLargeFont() {
        return largeFont;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public String getGameName() {
        return gameName;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public void dispose() {
        batch.dispose();
        smallFont.dispose();
        mediumFont.dispose();
        largeFont.dispose();
    }
}
