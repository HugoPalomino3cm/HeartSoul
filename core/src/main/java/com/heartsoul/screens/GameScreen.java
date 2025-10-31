package com.heartsoul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.*;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {
    private final int round;
    private final int lives;
    private final int score;
    private final Heart heart;
    private final ShapeRenderer shapeRenderer;
    private final List<Projectile> projectiles = new ArrayList<>();
    private final Texture bulletTx = new Texture(Gdx.files.internal("ui/bullet.png"));
    private Texture lifeIconTexture;

    private String powerUp = "NINGUNO";
    private int timerSeconds = 45;
    private float timerAccumulator = 0f;
    private float spawnTimer = 0f;

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
        // Preparar fuente
        game.getSmallFont().getData().setScale(1f);

        GlyphLayout gl = new GlyphLayout();

        // Dibujar fondo de la barra superior con ShapeRenderer (finaliza batch temporalmente)
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.5f));
        shapeRenderer.rect(0, VIRTUAL_HEIGHT - getTopBarHeight() - 40, VIRTUAL_WIDTH, getTopBarHeight() + 40);
        shapeRenderer.end();
        batch.begin();

        // Posiciones de texto (dos líneas dentro de la barra superior)
        float topY = VIRTUAL_HEIGHT - 10f;
        float secondLineY = VIRTUAL_HEIGHT - 50f;

        // Estilos para los íconos de vida
        float iconWidth = 50f; // ancho
        float iconHeight = 40f; // alto
        float iconPadding = 12f; // espacio entre íconos

        // Textos de la primera línea
        String leftText = "VIDAS: ";
        String centerText = "PUNTAJE: " + this.score;
        String rightText = "RONDA: " + this.round;

        // Dibujar Vidas (Izquierda)
        game.getSmallFont().setColor(Color.RED);
        game.getSmallFont().draw(batch, leftText, 10, topY);

        // Dibujar íconos de vida junto al texto

        // Medir el texto "VIDAS: " para calcular dónde empezar los íconos
        gl.setText(game.getSmallFont(), leftText);

        // Posición X inicial para el primer ícono
        float startX = 10 + gl.width + iconPadding;
        // Posición Y (alinea arriba)
        float iconY = topY + 5 - iconHeight;

        for (int i = 0; i < heart.getLives(); i++) {
            // Usamos el método draw() que escala la imagen
            batch.draw(
                lifeIconTexture,
                startX + i * (iconWidth + iconPadding), // Usamos iconPadding
                iconY,
                iconWidth,
                iconHeight
            );
        }

        // Dibujar Puntaje (Centro)
        gl.setText(game.getSmallFont(), centerText); // Reutilizamos 'gl'
        game.getSmallFont().setColor(Color.WHITE);
        game.getSmallFont().draw(batch, centerText, (VIRTUAL_WIDTH - gl.width) / 2f, topY);

        // Dibujar Ronda (Derecha)
        gl.setText(game.getSmallFont(), rightText); // Reutilizamos 'gl'
        game.getSmallFont().setColor(Color.GOLD);
        game.getSmallFont().draw(batch, rightText, VIRTUAL_WIDTH - gl.width - 10f, topY);

        // Textos de la segunda línea
        String powerText = "Power-Up: [" + powerUp + "]";
        String timeText = String.format("Tiempo Rund: %02d:%02d", timerSeconds / 60, timerSeconds % 60);

        // Power-Up a la izquierda (segunda línea)
        game.getSmallFont().setColor(Color.CYAN);
        game.getSmallFont().draw(batch, powerText, 10, secondLineY);

        // Tiempo a la derecha (segunda línea)
        gl.setText(game.getSmallFont(), timeText);
        game.getSmallFont().setColor(Color.LIGHT_GRAY);
        game.getSmallFont().draw(batch, timeText, VIRTUAL_WIDTH - gl.width - 10f, secondLineY);

        // Restaurar color por si acaso
        game.getSmallFont().setColor(Color.WHITE);
    }

    // Método para generar proyectiles cada 2 segundos
    private void spawnProjectiles(float delta) {
        spawnTimer += delta;

        // Intervalo aleatorio entre 0.5 y 3 segundos
        float randomInterval = MathUtils.random(0.5f, 3f);

        if (spawnTimer >= randomInterval) {
            spawnTimer = 0f;

            // Crea el bullet usando el constructor con randomX y Y arriba
            Bullet bullet = new Bullet(
                VIRTUAL_WIDTH,
                VIRTUAL_HEIGHT,
                bulletTx
            );
            projectiles.add(bullet);
        }
    }

    @Override
    public void show() {
        registerESC();
        background = new Texture(Gdx.files.internal("ui/gameGuideBackground.png"));
        lifeIconTexture = new Texture(Gdx.files.internal("ui/life.png"));
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

        // Procesa la entrada del usuario
        float dx = 0f, dy = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx = -2f;
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) dx = 2f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy = -2f;
        else if (Gdx.input.isKeyPressed(Input.Keys.W)) dy = 2f;

        // Movemos el heart
        heart.move(dx, dy, this);
        heart.update();

        batch.begin();
        // Fondo
        Color oldColor = batch.getColor();
        batch.setColor(1f, 1f, 1f, 0.6f);
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.setColor(oldColor);

        header();
        heart.draw(batch, this);

        // Generar proyectiles desde arriba
        spawnProjectiles(delta);

        // Actualizar y dibujar proyectiles
        for (Projectile projectile : projectiles) {
            projectile.update();
            projectile.checkBounds(this);

            // Verificar colisión con Heart
            if (!projectile.isDead() && heart.checkCollision(projectile)) {
                projectile.onCollision();
            }

            projectile.draw(batch, this);
        }

        // Remover proyectiles destruidos
        projectiles.removeIf(Entity::isDead);

        drawMovementArea(batch);

        if (heart.isDead()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            game.removeInputProcessor(stage);
            game.setScreen(new GameOverScreen(game));
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (bulletTx != null) {
            bulletTx.dispose();
        }
    }
}
