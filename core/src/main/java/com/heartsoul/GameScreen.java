package com.heartsoul;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameScreen implements Screen {
    private Main game;
    private int round;
    private int lives;
    private int score;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Heart heart;

    public GameScreen(Main game, int round, int lives, int score) {
        this.game = game;
        this.round = round;
        this.lives = lives;
        this.score = score;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);

        heart = new Heart(Gdx.graphics.getWidth() / 2 - 22, Gdx.graphics.getHeight() / 2 - 22,
                new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("ui/heart.png")));

        heart.setLives(lives);
    }

    public void header() {
        game.getSmallFont().getData().setScale(1f);
        game.getSmallFont().draw(batch, "Vidas: "+heart.getLives(), 10, 30);
        game.getSmallFont().draw(batch, "Ronda: "+round, Gdx.graphics.getWidth()-500, 30);
        game.getSmallFont().draw(batch, "Score: "+this.score, Gdx.graphics.getWidth()-350, 30);
        //game.getSmallFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        header();
        heart.draw(batch, this);
        // Aquí irá la lógica del juego
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
