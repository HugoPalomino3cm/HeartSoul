package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Interpolation;

/** First screen of the application. Displayed after the application is created. */
public class IntroScreen implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Texture background;
    private Texture title;
    private Stage stage;
    private FitViewport viewport;
    private float elapsedTime = 0f;

    // Sonidos
    private Music bgMusic;

    // Virtual resolution to keep the game's original aesthetic
    private static final int VIRTUAL_WIDTH = 1200;
    private static final int VIRTUAL_HEIGHT = 800;

    public IntroScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("ui/background.png"));
        title = new Texture(Gdx.files.internal("ui/title.png"));

        // Crear el menú
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        stage = new Stage(viewport);
        // Registrar el stage en el InputMultiplexer del Main para que coexistan múltiples processors
        game.addInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // musica de fondo: usar Music para streaming y poder pararla/reanudar correctamente
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));
        bgMusic.setLooping(true);
        bgMusic.play();

        // Crear estilos para los elementos de UI
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getLargeFont();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getLargeFont();

        // Crear botones y elementos del menú
        TextButton playButton = new TextButton("JUGAR", buttonStyle);

        // Establecer la transformación y el origen en el centro
        playButton.setTransform(true);
        playButton.setOrigin(Align.center);

        // Agregar efecto hover al botón
        playButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Agrandar el botón cuando el mouse entra
                playButton.clearActions();
                playButton.addAction(Actions.scaleTo(1.5f, 1.5f, 0.2f, Interpolation.fade));
                Gdx.audio.newSound(Gdx.files.internal("sounds/hover_sound.mp3")).play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Volver al tamaño normal cuando el mouse sale
                playButton.clearActions();
                playButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f, Interpolation.fade));
            }
        });

        // Agregar listener de click para iniciar el juego
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Antes de cambiar de pantalla, quitar el processor y parar la música
                if (bgMusic != null) {
                    bgMusic.stop();
                    bgMusic.dispose();
                    bgMusic = null;
                }
                game.removeInputProcessor(stage);
                game.setScreen(new GameScreen(game, 1, 3, 0));
            }
        });

        // Botón SALIR debajo de JUGAR
        TextButton exitButton = new TextButton("SALIR", buttonStyle);
        exitButton.setTransform(true);
        exitButton.setOrigin(Align.center);
        // Efecto hover (más sutil)
        exitButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.clearActions();
                exitButton.addAction(Actions.scaleTo(1.2f, 1.2f, 0.15f, Interpolation.fade));
                Gdx.audio.newSound(Gdx.files.internal("sounds/hover_sound.mp3")).play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.clearActions();
                exitButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.15f, Interpolation.fade));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Salir de la aplicación: parar y liberar música
                if (bgMusic != null) {
                    bgMusic.stop();
                    bgMusic.dispose();
                    bgMusic = null;
                }
                Gdx.app.exit();
            }
        });

        // Añadir los botones en dos filas y bajar su posición para que no se sobrepongan con el título
        // padTop mueve el contenido hacia abajo dentro de la pantalla virtual
        table.center().padTop(220);
        table.row();
        table.add(playButton).padBottom(20);
        table.row();
        table.add(exitButton).padTop(10);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        elapsedTime += delta;
        float amplitude = 20f; // altura máxima del movimiento
        float baseY = viewport.getWorldHeight() - title.getHeight();
        float offsetY = (float) Math.sin(elapsedTime * 2) * amplitude;
        float titleY = baseY + offsetY;
        float titleX = (viewport.getWorldWidth() - title.getWidth()) / 2f;

        // Fondo
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.getBatch().draw(title, titleX, titleY);
        stage.getBatch().end();

        // Solo actualizar y dibujar
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Actualizar viewport para mantener la relación de aspecto y escalar la UI
        if (viewport != null) viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        // Asegurarnos de quitar el processor si el stage estaba registrado
        if (stage != null) game.removeInputProcessor(stage);
        // detener y liberar música por si acaso
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.dispose();
            bgMusic = null;
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            game.removeInputProcessor(stage);
            stage.dispose();
        }
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.dispose();
            bgMusic = null;
        }
    }
}
