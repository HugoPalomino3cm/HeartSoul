package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.heartsoul.Main;

public class GameOverScreen extends BaseScreen {
    public GameOverScreen(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getMediumFont().draw(game.getBatch(), "Game Over !!! ", 120, 400, 400, 1, true);
        game.getMediumFont().draw(game.getBatch(), "Apreta ESPACIO para reiniciar", 100, 300);
        game.getMediumFont().draw(game.getBatch(), "Apreta ESC para volver al menú", 100, 200);

        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            stopMusic();
            game.removeInputProcessor(stage);
            game.setScreen(new GameScreen(game, 1, 3, 0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            stopMusic();
            game.removeInputProcessor(stage);
            game.setScreen(new IntroScreen(game));
        }
    }
    @Override
    public void show() {
        playMusic("sounds/game_over.mp3", false);
    }
}
