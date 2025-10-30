package com.heartsoul;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class GameScreen implements Screen {
    private Main game;
    private int round;
    private int lives;
    private int score;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Heart heart;

    // Virtual resolution - mantener la misma estetica que antes
    private static final int VIRTUAL_WIDTH = 800;
    private static final int VIRTUAL_HEIGHT = 640;

    // Alturas de las barras UI (zona ocupada por HUD) para evitar que el corazón pase por encima
    private static final int BOTTOM_UI_HEIGHT = 40; // zona inferior donde está "Vidas: Ronda: Score:"
    private static final int TOP_UI_HEIGHT = 40; // zona superior, si quieres una barra similar arriba

    // Input processor para escuchar ESC
    private InputAdapter escListener;

    public GameScreen(Main game, int round, int lives, int score) {
        this.game = game;
        this.round = round;
        this.lives = lives;
        this.score = score;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Crear el heart en coordenadas del mundo virtual (centrado)
        heart = new Heart(VIRTUAL_WIDTH / 2 - 22, VIRTUAL_HEIGHT / 2 - 22,
                new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("ui/heart.png")));

        heart.setLives(lives);
    }

    public void header() {
        game.getSmallFont().getData().setScale(1f);
        // Dibujar el HUD dentro de la zona inferior reservada
        game.getSmallFont().draw(batch, "Vidas: "+heart.getLives(), 10, 30);
        game.getSmallFont().draw(batch, "Ronda: "+round, VIRTUAL_WIDTH-500, 30);
        game.getSmallFont().draw(batch, "Score: "+this.score, VIRTUAL_WIDTH-350, 30);
        //game.getSmallFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
    }

    @Override
    public void show() {
        // Registrar listener para ESC que vuelve al menú
        escListener = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    // Retornar al menú inicial
                    game.setScreen(new IntroScreen(game));
                    return true;
                }
                return false;
            }
        };
        game.addInputProcessor(escListener);
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
        if (viewport != null) viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // Quitar el input listener de ESC
        if (escListener != null) {
            game.removeInputProcessor(escListener);
            escListener = null;
        }
    }

    @Override
    public void dispose() {
        // Asegurarnos de quitar el listener si se libera la pantalla
        if (escListener != null) game.removeInputProcessor(escListener);
    }

    // Exponer dimensiones virtuales para que entidades usen el mundo virtual
    public int getVirtualWidth() { return VIRTUAL_WIDTH; }
    public int getVirtualHeight() { return VIRTUAL_HEIGHT; }

    // Getters para las alturas de las zonas UI (barreras invisibles)
    public int getBottomBarHeight() { return BOTTOM_UI_HEIGHT; }
    public int getTopBarHeight() { return TOP_UI_HEIGHT; }
}
