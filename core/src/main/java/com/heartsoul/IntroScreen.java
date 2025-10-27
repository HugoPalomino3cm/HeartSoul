package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    private float elapsedTime = 0f;

    public IntroScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("ui/background.png"));
        title = new Texture(Gdx.files.internal("ui/title.png"));

        // Crear el menú
        stage = new Stage(new FitViewport(1200, 800));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // musica de fondo
        Gdx.audio.newSound(Gdx.files.internal("sounds/background_music.mp3")).play();

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
                game.setScreen(new GameScreen(game, 1, 3, 0));
            }
        });

        table.row();
        table.add(playButton).padBottom(playButton.getHeight() - 200);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        elapsedTime += delta;
        float amplitude = 20f; // altura máxima del movimiento
        float baseY = stage.getHeight() - title.getHeight();
        float offsetY = (float) Math.sin(elapsedTime * 2) * amplitude;
        float titleY = baseY + offsetY;
        float titleX = (stage.getWidth() - title.getWidth()) / 2f;

        // Fondo
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().draw(title, titleX, titleY);
        stage.getBatch().end();

        // Solo actualizar y dibujar
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
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
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
