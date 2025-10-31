package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heartsoul.Main;

/** First screen of the application. Displayed after the application is created. */
public class IntroScreen extends BaseScreen {
    private Texture title;
    private float elapsedTime = 0f;

    public IntroScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        batch.setColor(1f, 1f, 1f, 1f);
        setBackground(new Texture(Gdx.files.internal("ui/background.png")));
        title = new Texture(Gdx.files.internal("ui/title.png"));

        // Crear el menú
        initializeStage();

        Table table = new Table();
        table.setFillParent(true);
        getStage().addActor(table);

        playMusic("sounds/background_music.mp3", true);

        // Crear estilos para los elementos de UI
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getLargeFont();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getLargeFont();

        // Botón JUGAR
        TextButton playButton = createButton("JUGAR", buttonStyle, () -> {
            stopMusic();
            game.setScreen(new GameScreen(game, 1, 3, 0));
        });

        // Botón GUÍA
        TextButton guideButton = createButton("GUÍA", buttonStyle, () -> {
            stopMusic();
            game.setScreen(new GuideScreen(game));
        });

        // Botón SALIR
        TextButton exitButton = createButton("SALIR", buttonStyle, () -> {
            stopMusic();
            Gdx.app.exit();
        });

        // Añadir los botones en dos filas y bajar su posición para que no se sobrepongan con el título
        // padTop mueve el contenido hacia abajo dentro de la pantalla virtual
        table.center().padTop(380);
        table.row();
        table.add(playButton).padBottom(20);
        table.row();
        table.add(guideButton).padTop(10).padBottom(20);
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
        renderBackground();

        // Letras HeartSoul
        batch.begin();
        batch.draw(title, titleX, titleY);
        batch.end();

        // UI
        getStage().act(delta);
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (title != null) {
            title.dispose();
        }
    }
}
