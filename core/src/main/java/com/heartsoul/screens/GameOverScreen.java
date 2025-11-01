package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.heartsoul.Main;
import com.heartsoul.SoundManager;

public class GameOverScreen extends BaseScreen {
    public GameOverScreen(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        this.camera.update();
        this.game.getBatch().setProjectionMatrix(this.camera.combined);

        this.game.getBatch().begin();
        this.game.getMediumFont().draw(this.game.getBatch(), "Game Over !!! ", 120, 400, 400, 1, true);
        this.game.getMediumFont().draw(this.game.getBatch(), "Apreta ESPACIO para reiniciar", 100, 300);
        this.game.getMediumFont().draw(this.game.getBatch(), "Apreta ESC para volver al menú", 100, 200);

        this.game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.game.setScreen(new GameScreen(this.game, 1, 3, 0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(new IntroScreen(this.game));
        }
    }

    @Override
    public void show() {
        SoundManager.getInstance().stopMusic();
        SoundManager.getInstance().playSound("sounds/gameover.mp3");
    }
}
