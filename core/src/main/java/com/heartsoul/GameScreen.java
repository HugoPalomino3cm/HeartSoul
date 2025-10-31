package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen extends BaseScreen {
    private final int round;
    private final int lives;
    private final int score;
    private final Heart heart;
    private final ShapeRenderer shapeRenderer;

    public GameScreen(Main game, int round, int lives, int score) {
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        this.round = round;
        this.lives = lives;
        this.score = score;

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
    }

    @Override
    public void show() {
        registerESC();
    }

    // Lineas de contorno que limitan el movimiento del jugar
    private void drawMovementArea(SpriteBatch batch) {
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(255, 255, 255, 1); // Verde

        float x = 1; // Posición X inicial (1 pixel desde el borde izquierdo)
        float y = getBottomBarHeight(); // Posición Y inicial (altura de la barra inferior)
        float width = getVirtualWidth() - 2; // Ancho del rectángulo
        float height = getVirtualHeight() - getTopBarHeight() - getBottomBarHeight(); // Alto del rectángulo

        // Línea superior
        shapeRenderer.line(x, y + height, x + width, y + height);
        // Línea derecha
        shapeRenderer.line(x + width, y, x + width, y + height);
        // Línea inferior
        shapeRenderer.line(x, y, x + width, y);
        // Línea izquierda
        shapeRenderer.line(x, y, x, y + height);

        shapeRenderer.end();

        batch.begin();
    }

    @Override
    public void render(float delta) {
        clearScreen();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        header();
        heart.draw(batch, this);

        // aca añado la funcion que añadi yo
        drawMovementArea(batch); // Mostrar área de movimiento
        // Aquí irá la lógica del juego
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
