package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.heartsoul.Main;
import com.heartsoul.IntroScreen;

public class GuideScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Texture backgroundTexture;
    private BitmapFont titleFont;
    private BitmapFont contentFont;

    private static final float WORLD_WIDTH = 1200;
    private static final float WORLD_HEIGHT = 800;

    private Table mainMenu;
    private Table contentTable;
    private boolean showingSection = false;

    public GuideScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());

        backgroundTexture = new Texture(Gdx.files.internal("ui/gameGuideBackground.png"));

        titleFont = game.getLargeFont();
        contentFont = game.getMediumFont();

        createMainMenu();
        game.addInputProcessor(stage);
    }

    private void createMainMenu() {
        mainMenu = new Table();
        mainMenu.setFillParent(true);

        // Título
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);
        Label titleLabel = new Label("GUÍA DEL JUEGO", titleStyle);
        mainMenu.add(titleLabel).padBottom(50).row();

        // Estilo de botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = contentFont;
        buttonStyle.fontColor = Color.CYAN;
        buttonStyle.downFontColor = Color.YELLOW;
        buttonStyle.overFontColor = Color.YELLOW;

        // Botón HISTORIA
        TextButton gameplayButton = createButton("HISTORIA", buttonStyle, () -> showStorySection());
        mainMenu.add(gameplayButton).width(300).height(80).padBottom(30).row();

        // Botón MOVIMIENTOS
        TextButton enemiesButton = createButton("MOVIMIENTOS", buttonStyle, () -> showControlsSection());
        mainMenu.add(enemiesButton).width(300).height(80).padBottom(30).row();

        // Botón Volver
        TextButton backButton = createButton("VOLVER AL MENÚ", buttonStyle, () -> {
            game.removeInputProcessor(stage);
            game.setScreen(new IntroScreen(game));
        });
        mainMenu.add(backButton).width(300).height(80).padTop(50).row();

        stage.addActor(mainMenu);
    }

    private TextButton createButton(String text, TextButton.TextButtonStyle style, Runnable onClick) {
        TextButton button = new TextButton(text, style);
        button.setTransform(true);
        button.setOrigin(Align.center);

        // Efecto hover
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.clearActions();
                button.addAction(Actions.scaleTo(1.2f, 1.2f, 0.2f, Interpolation.fade));
                try {
                    Gdx.audio.newSound(Gdx.files.internal("sounds/hover_sound.mp3")).play();
                } catch (Exception e) {
                    // Ignorar si no existe el sonido
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.clearActions();
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f, Interpolation.fade));
            }
        });

        // Click
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });

        return button;
    }

    private void showStorySection() {
        showingSection = true;
        mainMenu.setVisible(false);

        contentTable = new Table();
        contentTable.setFillParent(true);
        contentTable.pad(80, 150, 80, 150);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);
        Label.LabelStyle contentStyle = new Label.LabelStyle(contentFont, Color.WHITE);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(contentFont, Color.CYAN);

        contentTable.add(new Label("HISTORIA", titleStyle)).padBottom(40).row();

        contentTable.add(new Label("El Corazón Valiente:", highlightStyle)).padBottom(20).row();

        Label storyText = new Label(
            "En un mundo dividido entre luz y oscuridad,\n" +
                "tu corazón define tu destino.\n" +
                "Elige tu camino sabiamente.",
            contentStyle
        );
        storyText.setWrap(true);
        storyText.setAlignment(Align.center);
        contentTable.add(storyText).width(600).padBottom(30).row();

        contentTable.add(new Label("Tu Misión:", highlightStyle)).padBottom(20).row();
        contentTable.add(new Label("Esquiva las balas y sobrevive", contentStyle)).padBottom(15).row();

        addBackButton(contentTable);
        stage.addActor(contentTable);
    }



    private void showControlsSection() {
        showingSection = true;
        mainMenu.setVisible(false);

        contentTable = new Table();
        contentTable.setFillParent(true);
        contentTable.pad(50, 100, 50, 100);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);
        Label.LabelStyle contentStyle = new Label.LabelStyle(contentFont, Color.WHITE);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(contentFont, Color.CYAN);

        contentTable.add(new Label("MOVIMIENTOS", titleStyle)).padBottom(40).row();
        contentTable.add(new Label("Controles de Movimiento:", highlightStyle)).padBottom(20).row();
        contentTable.add(new Label("- ↑/W: Mover arriba", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- ↓/S: Mover abajo", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- ←/A: Mover izquierda", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- →/D: Mover derecha", contentStyle)).padBottom(30).row();

        addBackButton(contentTable);
        stage.addActor(contentTable);
    }

    private void addBackButton(Table table) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = contentFont;
        buttonStyle.fontColor = Color.YELLOW;
        buttonStyle.overFontColor = Color.GOLD;

        TextButton backButton = createButton("VOLVER", buttonStyle, () -> {
            contentTable.remove();
            showingSection = false;
            mainMenu.setVisible(true);
        });

        table.add(backButton).width(200).height(60).row();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        game.removeInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
