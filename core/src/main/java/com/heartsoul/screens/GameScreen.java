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
import com.heartsoul.entities.*;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {
    private final int round;
    private final int initialLives;
    private final int initialScore;
    private final Heart heart;
    private final ShapeRenderer shapeRenderer;
    private final List<Projectile> projectiles;
    private final List<Entity> powerUps; // Lista para Shield y SpeedUp

    // Texturas
    private Texture bulletTx;
    private Texture bombTx;
    private Texture lifeTx;
    private Texture shieldTx;
    private Texture speedUpTx;

    // Header info
    private String powerUp;
    private int timerSeconds;
    private float timerAccumulator;
    private float spawnTimer;
    private float powerUpSpawnTimer;

    // Power-ups activos
    private Shield activeShield;
    private SpeedUp activeSpeedUp;

    public GameScreen(Main game, int round, int lives, int score) {
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        this.round = round;
        this.initialLives = lives;
        this.powerUpSpawnTimer = 0f;
        this.activeShield = null;
        this.activeSpeedUp = null;
        this.initialScore = score;
        this.powerUps = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.powerUp = "NINGUNO";
        this.timerSeconds = 45;
        this.timerAccumulator = 0f;
        this.spawnTimer = 0f;

        // Crear el heart en coordenadas del mundo virtual (centrado)
        this.heart = new Heart(
            (int) (getVirtualWidth() / 2 - 22),
            (int) (getVirtualHeight() / 2 - 22),
            new Texture(Gdx.files.internal("ui/heart.png"))
        );

        this.heart.setLives(lives);
    }

    public void header() {
        // Preparar fuente
        this.game.getSmallFont().getData().setScale(1f);

        GlyphLayout gl = new GlyphLayout();

        // Dibujar fondo de la barra superior con ShapeRenderer (finaliza batch temporalmente)
        this.batch.end();
        this.shapeRenderer.setProjectionMatrix(this.batch.getProjectionMatrix());
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.5f));
        this.shapeRenderer.rect(0, getVirtualHeight() - getTopBarHeight() - 40, getVirtualWidth(), getTopBarHeight() + 40);
        this.shapeRenderer.end();
        this.batch.begin();

        // Posiciones de texto (dos líneas dentro de la barra superior)
        float topY = getVirtualHeight() - 10f;
        float secondLineY = getVirtualHeight() - 50f;

        // Estilos para los íconos de vida
        float iconWidth = 50f; // ancho
        float iconHeight = 40f; // alto
        float iconPadding = 12f; // espacio entre íconos

        // Textos de la primera línea
        String leftText = "VIDAS: ";
        String centerText = "PUNTAJE: " + this.initialScore;
        String rightText = "RONDA: " + this.round;

        // Dibujar Vidas (Izquierda)
        this.game.getSmallFont().setColor(Color.RED);
        this.game.getSmallFont().draw(this.batch, leftText, 10, topY);

        // Dibujar íconos de vida junto al texto

        // Medir el texto "VIDAS: " para calcular dónde empezar los íconos
        gl.setText(this.game.getSmallFont(), leftText);

        // Posición X inicial para el primer ícono
        float startX = 10 + gl.width + iconPadding;
        // Posición Y (alinea arriba)
        float iconY = topY + 5 - iconHeight;

        for (int i = 0; i < this.heart.getLives(); i++) {
            // Usamos el método draw() que escala la imagen
            this.batch.draw(
                this.lifeTx,
                startX + i * (iconWidth + iconPadding), // Usamos iconPadding
                iconY,
                iconWidth,
                iconHeight
            );
        }

        // Dibujar Puntaje (Centro)
        gl.setText(this.game.getSmallFont(), centerText); // Reutilizamos 'gl'
        this.game.getSmallFont().setColor(Color.WHITE);
        this.game.getSmallFont().draw(this.batch, centerText, (getVirtualWidth() - gl.width) / 2f, topY);

        // Dibujar Ronda (Derecha)
        gl.setText(this.game.getSmallFont(), rightText); // Reutilizamos 'gl'
        this.game.getSmallFont().setColor(Color.GOLD);
        this.game.getSmallFont().draw(this.batch, rightText, getVirtualWidth() - gl.width - 10f, topY);

        // Textos de la segunda línea
        String powerText = "Power-Up: [" + this.powerUp + "]";
        String timeText = String.format("Tiempo Rund: %02d:%02d", this.timerSeconds / 60, this.timerSeconds % 60);

        // Power-Up a la izquierda (segunda línea)
        this.game.getSmallFont().setColor(Color.CYAN);
        this.game.getSmallFont().draw(this.batch, powerText, 10, secondLineY);

        // Tiempo a la derecha (segunda línea)
        gl.setText(this.game.getSmallFont(), timeText);
        this.game.getSmallFont().setColor(Color.LIGHT_GRAY);
        this.game.getSmallFont().draw(this.batch, timeText, getVirtualWidth() - gl.width - 10f, secondLineY);

        // Restaurar color por si acaso
        this.game.getSmallFont().setColor(Color.WHITE);
    }

    // Método para generar proyectiles cada 2 segundos
    private void spawnProjectiles(float delta) {
        this.spawnTimer += delta;
        float randomInterval = MathUtils.random(0.5f, 3f);

        if (this.spawnTimer >= randomInterval) {
            this.spawnTimer = 0f;

            if (MathUtils.randomBoolean(0.5f)) {
                // Genera Bullet
                Bullet bullet = new Bullet(
                    getVirtualWidth(),
                    getVirtualHeight(),
                    this.bulletTx
                );
                this.projectiles.add(bullet);
            } else {
                // Genera Bomb en posición aleatoria y velocidad aleatoria
                float x = MathUtils.random(0, getVirtualWidth() - 20);
                float y = MathUtils.random(0, getVirtualHeight() - 20);
                float xVel = MathUtils.random(-4f, 4f);
                float yVel = MathUtils.random(-4f, 4f);
                Bomb bomb = new Bomb((int)x, (int)y, this.bombTx, xVel, yVel);
                this.projectiles.add(bomb);
            }
        }
    }

    // Método para actualizar el texto del power-up activo
    private void updatePowerUpDisplay() {
        if (this.activeShield != null && this.activeShield.isActive()) {
            this.powerUp = String.format("SHIELD (%.1fs)", this.activeShield.getTimeLeft());
        } else if (this.activeSpeedUp != null && this.activeSpeedUp.isActive()) {
            this.powerUp = String.format("SPEED (%.1fs)", this.activeSpeedUp.getTimeLeft());
        } else {
            this.powerUp = "NINGUNO";
        }
    }

    // Método para generar power-ups cada 8-12 segundos
    private void spawnPowerUps(float delta) {
        this.powerUpSpawnTimer += delta;
        float randomInterval = MathUtils.random(8f, 12f);

        if (this.powerUpSpawnTimer >= randomInterval) {
            this.powerUpSpawnTimer = 0f;

            // 50% probabilidad de generar Shield o SpeedUp
            if (MathUtils.randomBoolean(0.5f)) {
                Shield shield = new Shield(
                    getVirtualWidth(),
                    getVirtualHeight(),
                    this.shieldTx,
                    5f // Duración de 5 segundos
                );
                this.powerUps.add(shield);
            } else {
                SpeedUp speedUp = new SpeedUp(
                    getVirtualWidth(),
                    getVirtualHeight(),
                    this.speedUpTx,
                    5f, // Duración de 5 segundos
                    2f  // Velocidad x2
                );
                this.powerUps.add(speedUp);
            }
        }
    }


    @Override
    public void show() {
        registerESC();
        setBackground(new Texture(Gdx.files.internal("ui/gameGuideBackground.png")));
        this.bulletTx = new Texture(Gdx.files.internal("ui/bullet.png"));
        this.bombTx = new Texture(Gdx.files.internal("ui/bomb.png"));
        this.lifeTx = new Texture(Gdx.files.internal("ui/life.png"));
        this.shieldTx = new Texture(Gdx.files.internal("ui/shield.png"));
        this.speedUpTx = new Texture(Gdx.files.internal("ui/speedUp.png"));

        // Cambiar a la música del juego (respetando el estado de pausa)
        SoundManager.getInstance().playMusic("sounds/game_music.mp3", 100);
    }

    // Lineas de contorno que limitan el movimiento del jugar
    private void drawMovementArea(SpriteBatch batch) {
        batch.end();

        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(255, 255, 255, 1); // Blanco

        float x = 1; // Posición X inicial (1 pixel desde el borde izquierdo)
        float y = getBottomBarHeight(); // Posición Y inicial (altura de la barra inferior)
        float width = getVirtualWidth() - 2; // Ancho del rectángulo
        float height = getVirtualHeight() - getTopBarHeight() - getBottomBarHeight(); // Alto del rectángulo

        // Línea superior
        this.shapeRenderer.line(x, y + height, x + width, y + height);
        // Línea derecha
        this.shapeRenderer.line(x + width, y, x + width, y + height);
        // Línea inferior
        this.shapeRenderer.line(x, y, x + width, y);
        // Línea izquierda
        this.shapeRenderer.line(x, y, x, y + height);

        this.shapeRenderer.end();

        batch.begin();
    }

    @Override
    public void render(float delta) {
        clearScreen();
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);

        // Procesa la entrada del usuario
        float dx = 0f;
        float dy = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -2f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = 2f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy = -2f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy = 2f;
        }

        // Movemos el heart
        this.heart.move(dx, dy, this);
        this.heart.update();

        // Fondo (renderBackground() maneja su propio begin/end)
        Color oldColor = this.batch.getColor();
        this.batch.setColor(1f, 1f, 1f, 0.6f);
        renderBackground();
        this.batch.setColor(oldColor);

        // Ahora comenzamos el batch para dibujar el resto
        this.batch.begin();
        header();
        this.heart.draw(this.batch, this);

        // Generar proyectiles desde arriba
        spawnProjectiles(delta);

        // Generar power-ups
        spawnPowerUps(delta);

        // Actualizar y dibujar proyectiles
        for (Projectile projectile : this.projectiles) {
            projectile.update(getVirtualWidth(), getVirtualHeight());
            projectile.checkBounds(this);

            // Verificar colisión con Heart
            if (!projectile.isDead() && this.heart.checkCollision(projectile)) {
                projectile.onCollision();
            }

            projectile.draw(this.batch, this);
        }

        // Remover proyectiles destruidos
        this.projectiles.removeIf(Entity::isDead);

        // Dibujar power-ups y detectar colisiones
        for (Entity powerUp : this.powerUps) {
            powerUp.draw(this.batch, this);

            // Verificar colisión con Heart (recoger power-up)
            if (!powerUp.isDead() && powerUp.getBoundingRectangle().overlaps(this.heart.getBoundingRectangle())) {
                if (powerUp instanceof Shield) {
                    Shield shield = (Shield) powerUp;
                    shield.apply(this.heart);
                    this.activeShield = shield;
                    updatePowerUpDisplay();
                } else if (powerUp instanceof SpeedUp) {
                    SpeedUp speedUp = (SpeedUp) powerUp;
                    speedUp.apply(this.heart);
                    this.activeSpeedUp = speedUp;
                    updatePowerUpDisplay();
                }
            }
        }

        // Remover power-ups recogidos
        this.powerUps.removeIf(Entity::isDead);

        // Actualizar power-ups activos
        if (this.activeShield != null) {
            this.activeShield.updatePowerUp(delta, this.heart);
            if (!this.activeShield.isActive()) {
                this.activeShield = null;
                updatePowerUpDisplay();
            }
        }

        if (this.activeSpeedUp != null) {
            this.activeSpeedUp.updatePowerUp(delta, this.heart);
            if (!this.activeSpeedUp.isActive()) {
                this.activeSpeedUp = null;
                updatePowerUpDisplay();
            }
        }

        drawMovementArea(this.batch);

        if (this.heart.isDead()) {
            if (this.initialScore > this.game.getHighScore()) {
                this.game.setHighScore(this.initialScore);
            }
            unregisterESC();
            this.game.setScreen(new GameOverScreen(this.game));
        }
        this.batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.shapeRenderer != null) {
            this.shapeRenderer.dispose();
        }
        if (this.bulletTx != null) {
            this.bulletTx.dispose();
        }
        if (this.bombTx != null) {
            this.bombTx.dispose();
        }
        if (this.lifeTx != null) {
            this.lifeTx.dispose();
        }
        if (this.shieldTx != null) {
            this.shieldTx.dispose();
        }
        if (this.speedUpTx != null) {
            this.speedUpTx.dispose();
        }
    }
}
