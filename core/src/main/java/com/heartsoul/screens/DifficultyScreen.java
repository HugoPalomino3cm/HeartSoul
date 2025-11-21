package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.heartsoul.DifficultyManager;
import com.heartsoul.Main;
import com.heartsoul.SoundManager;

/**
 * Pantalla de selección de dificultad
 * Permite al jugador elegir entre Fácil, Normal o Difícil
 * antes de comenzar el juego
 */
public class DifficultyScreen extends BaseScreen {

    private Stage stage;
    private DifficultyManager.Difficulty selectedDifficulty;

    public DifficultyScreen(Main game) {
        super(game);
        this.selectedDifficulty = DifficultyManager.getInstance().getCurrentDifficulty();
    }

    @Override
    public void show() {
        setBackground(new Texture(Gdx.files.internal("ui/background.png")));

        this.stage = new Stage(this.viewport, this.batch);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Botones de dificultad
        TextButton easyButton = createDifficultyButton("FÁCIL", DifficultyManager.Difficulty.EASY);
        TextButton normalButton = createDifficultyButton("NORMAL", DifficultyManager.Difficulty.NORMAL);
        TextButton hardButton = createDifficultyButton("DIFÍCIL", DifficultyManager.Difficulty.HARD);

        // Botón para continuar
        TextButton continueButton = createButton("CONTINUAR", () -> {
            DifficultyManager.getInstance().setDifficulty(this.selectedDifficulty);
            this.game.removeInputProcessor(this.stage);
            this.game.setScreen(new GameScreen(this.game, 1, 3, 0));
        });

        // Botón para volver
        TextButton backButton = createButton("VOLVER", () -> {
            this.game.removeInputProcessor(this.stage);
            this.game.setScreen(new IntroScreen(this.game));
        });

        mainTable.add().height(100);
        mainTable.row();
        mainTable.add(easyButton).width(400).height(60).padBottom(20);
        mainTable.row();
        mainTable.add(normalButton).width(400).height(60).padBottom(20);
        mainTable.row();
        mainTable.add(hardButton).width(400).height(60).padBottom(40);
        mainTable.row();
        mainTable.add(continueButton).width(400).height(60).padBottom(20);
        mainTable.row();
        mainTable.add(backButton).width(400).height(60);

        this.stage.addActor(mainTable);
        this.game.addInputProcessor(this.stage);

        // Reproducir música de fondo
        SoundManager.getInstance().playMusic("sounds/background_music.mp3", 1.0f);
    }

    private TextButton createDifficultyButton(String text, DifficultyManager.Difficulty difficulty) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = this.game.getMediumFont();
        style.fontColor = Color.WHITE;
        style.overFontColor = Color.CYAN;
        style.downFontColor = Color.GOLD;

        TextButton button = new TextButton(text, style);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.getInstance().playSound("sounds/hover.mp3");
                selectedDifficulty = difficulty;
                updateButtonColors();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (pointer == -1) {
                    SoundManager.getInstance().playSound("sounds/hover.mp3");
                }
            }
        });

        return button;
    }

    private TextButton createButton(String text, Runnable action) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = this.game.getMediumFont();
        style.fontColor = Color.WHITE;
        style.overFontColor = Color.CYAN;
        style.downFontColor = Color.GOLD;

        TextButton button = new TextButton(text, style);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.getInstance().playSound("sounds/hover.mp3");
                action.run();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (pointer == -1) {
                    SoundManager.getInstance().playSound("sounds/hover.mp3");
                }
            }
        });

        return button;
    }

    private void updateButtonColors() {
        // Actualizar colores de los botones según selección
        // Este método puede expandirse para cambiar el color del botón seleccionado
    }

    @Override
    public void render(float delta) {
        clearScreen();
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);

        // Procesar ESC para volver
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.removeInputProcessor(this.stage);
            this.game.setScreen(new IntroScreen(this.game));
        }

        renderBackground();

        this.batch.begin();

        // Título
        this.game.getLargeFont().setColor(Color.GOLD);
        GlyphLayout titleLayout = new GlyphLayout(this.game.getLargeFont(), "SELECCIONA DIFICULTAD");
        this.game.getLargeFont().draw(
            this.batch,
            titleLayout,
            (getVirtualWidth() - titleLayout.width) / 2f,
            getVirtualHeight() - 80
        );

        // Descripción de la dificultad seleccionada
        this.game.getSmallFont().setColor(Color.WHITE);
        String description = this.selectedDifficulty.getDescription();
        GlyphLayout descLayout = new GlyphLayout(this.game.getSmallFont(), description);
        this.game.getSmallFont().draw(
            this.batch,
            descLayout,
            (getVirtualWidth() - descLayout.width) / 2f,
            200
        );

        // Indicador de dificultad actual
        this.game.getSmallFont().setColor(Color.CYAN);
        String currentText = "Actual: " + this.selectedDifficulty.getDisplayName();
        GlyphLayout currentLayout = new GlyphLayout(this.game.getSmallFont(), currentText);
        this.game.getSmallFont().draw(
            this.batch,
            currentLayout,
            (getVirtualWidth() - currentLayout.width) / 2f,
            250
        );

        this.batch.end();

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.stage != null) {
            this.stage.dispose();
        }
    }
}

