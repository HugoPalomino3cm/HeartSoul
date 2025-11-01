package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heartsoul.Main;
import com.heartsoul.SoundManager;

/** First screen of the application. Displayed after the application is created. */
public class IntroScreen extends BaseScreen {
    private float elapsedTime;
    private boolean isMuted = false;

    // Texturas
    private Texture title;
    private Texture muteOn, muteOff;

    // Posición y tamaño del botón de mute
    private float muteSize;
    private float muteX;
    private float muteY;

    // InputAdapter para el botón de mute
    private InputAdapter muteButtonListener;

    public IntroScreen(Main game) {
        super(game);
        this.elapsedTime = 0f;
    }

    @Override
    public void show() {
        this.batch.setColor(1f, 1f, 1f, 1f);
        setBackground(new Texture(Gdx.files.internal("ui/background.png")));
        title = new Texture(Gdx.files.internal("ui/title.png"));
        muteOn = new Texture(Gdx.files.internal("ui/music-on.png"));
        muteOff = new Texture(Gdx.files.internal("ui/music-off.png"));

        // Inicializar posición y tamaño del botón de mute
        muteSize = 64;
        muteX = getVirtualWidth() - muteSize - 32;
        muteY = getVirtualHeight() - muteSize - 32;

        // Cambiar a la música de fondo (respetando el estado de pausa)
        SoundManager.getInstance().playMusic("sounds/background_music.mp3", 100);

        // Sincronizar el estado de mute con el SoundManager DESPUÉS de cambiar la música
        this.isMuted = SoundManager.getInstance().isMusicPaused();

        // Crear el menú primero
        initializeStage();

        Table table = new Table();
        table.setFillParent(true);
        getStage().addActor(table);

        // Crear estilos para los elementos de UI
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.game.getLargeFont();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = this.game.getLargeFont();

        // Botón JUGAR
        TextButton playButton = createButton("JUGAR", buttonStyle, () -> {
            this.game.setScreen(new GameScreen(this.game, 1, 3, 0));
        });

        // Botón GUÍA
        TextButton guideButton = createButton("GUÍA", buttonStyle, () -> {
            this.game.setScreen(new GuideScreen(this.game));
        });

        // Botón SALIR
        TextButton exitButton = createButton("SALIR", buttonStyle, () -> {
            Gdx.app.exit();
        });

        // Añadir los botones
        table.center().padTop(380);
        table.row();
        table.add(playButton).padBottom(20);
        table.row();
        table.add(guideButton).padTop(10).padBottom(20);
        table.row();
        table.add(exitButton).padTop(10);

        // Crear y registrar el InputAdapter para el botón de silencio
        this.muteButtonListener = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Convertir coordenadas de pantalla a coordenadas del viewport usando unproject
                com.badlogic.gdx.math.Vector3 touchPos = new com.badlogic.gdx.math.Vector3(screenX, screenY, 0);
                viewport.unproject(touchPos);

                float mouseX = touchPos.x;
                float mouseY = touchPos.y;

                if (mouseX >= muteX && mouseX <= muteX + muteSize &&
                    mouseY >= muteY && mouseY <= muteY + muteSize) {
                    isMuted = !isMuted;
                    if (isMuted) {
                        SoundManager.getInstance().pauseMusic();
                    } else {
                        SoundManager.getInstance().resumeMusic();
                    }
                    return true; // Evento consumido
                }
                return false; // Dejar pasar el evento
            }
        };
        this.game.addInputProcessor(this.muteButtonListener);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.elapsedTime += delta;
        float amplitude = 20f; // altura máxima del movimiento
        float baseY = this.viewport.getWorldHeight() - this.title.getHeight();
        float offsetY = (float) Math.sin(this.elapsedTime * 2) * amplitude;
        float titleY = baseY + offsetY;
        float titleX = (this.viewport.getWorldWidth() - this.title.getWidth()) / 2f;

        // Fondo
        renderBackground();

        // Dibujar todo en un solo batch
        this.batch.begin();

        // Letras HeartSoul
        this.batch.draw(this.title, titleX, titleY);

        // Botón de mute
        Texture currentMuteTexture = isMuted ? muteOff : muteOn;
        this.batch.draw(currentMuteTexture, muteX, muteY, muteSize, muteSize);

        this.batch.end();

        // UI (botones del menú)
        getStage().act(delta);
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.muteButtonListener != null) {
            this.game.removeInputProcessor(this.muteButtonListener);
            this.muteButtonListener = null;
        }
        if (this.title != null) {
            this.title.dispose();
        }
        if (this.muteOn != null) {
            this.muteOn.dispose();
        }
        if (this.muteOff != null) {
            this.muteOff.dispose();
        }
    }
}

