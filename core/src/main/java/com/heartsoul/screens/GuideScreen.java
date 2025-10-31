package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heartsoul.Main;

public class GuideScreen extends BaseScreen {
    private BitmapFont titleFont;
    private BitmapFont contentFont;

    private Table table;
    private Table contentTable;
    private boolean showingSection = false;

    public GuideScreen(Main game) {
        super(game);
        titleFont = game.getLargeFont();
        contentFont = game.getMediumFont();
    }

    @Override
    public void show() {
        registerESC();
        initializeStage();
        background = new Texture(Gdx.files.internal("ui/gameGuideBackground.png"));
        guideMenu();
    }

    private void guideMenu() {
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Título
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);
        Label titleLabel = new Label("GUÍA DEL JUEGO", titleStyle);

        // Estilo de botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = contentFont;
        buttonStyle.fontColor = Color.CYAN;
        buttonStyle.downFontColor = Color.YELLOW;
        buttonStyle.overFontColor = Color.YELLOW;

        TextButton.TextButtonStyle buttonStyleBack = new TextButton.TextButtonStyle();
        buttonStyleBack.font = contentFont;
        buttonStyleBack.fontColor = Color.RED;
        buttonStyleBack.downFontColor = Color.YELLOW;
        buttonStyleBack.overFontColor = Color.YELLOW;

        // Botón HISTORIA
        TextButton storyButton = createButton("HISTORIA", buttonStyle, () -> showStorySection());

        // Botón MOVIMIENTOS
        TextButton movementButton = createButton("MOVIMIENTOS", buttonStyle, () -> showControlsSection());

        // Botón VOLVER
        TextButton exitButton = createButton("VOLVER", buttonStyleBack, () -> {
            game.removeInputProcessor(stage);
            game.setScreen(new IntroScreen(game));
        });

        table.center().padTop(100);
        table.row();
        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(storyButton).padBottom(20);
        table.row();
        table.add(movementButton).padTop(10).padBottom(20);
        table.row();
        table.add(exitButton).padTop(10);
    }

    private void showStorySection() {
        showingSection = true;
        table.setVisible(false);

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
        table.setVisible(false);

        contentTable = new Table();
        contentTable.setFillParent(true);
        contentTable.pad(50, 100, 50, 100);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);
        Label.LabelStyle contentStyle = new Label.LabelStyle(contentFont, Color.WHITE);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(contentFont, Color.CYAN);

        contentTable.add(new Label("MOVIMIENTOS", titleStyle)).padBottom(40).row();
        contentTable.add(new Label("Controles de Movimiento:", highlightStyle)).padBottom(20).row();
        contentTable.add(new Label("- W: Mover arriba", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- S: Mover abajo", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- A: Mover izquierda", contentStyle)).padBottom(10).row();
        contentTable.add(new Label("- D: Mover derecha", contentStyle)).padBottom(30).row();

        addBackButton(contentTable);
        stage.addActor(contentTable);
    }

    private void addBackButton(Table table) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = contentFont;
        buttonStyle.fontColor = Color.RED;
        buttonStyle.overFontColor = Color.GOLD;

        TextButton backButton = createButton("VOLVER", buttonStyle, () -> {
            contentTable.remove();
            showingSection = false;
            this.table.setVisible(true);
        });

        table.add(backButton).width(200).height(60).row();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        renderBackground();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        disposeStage();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
