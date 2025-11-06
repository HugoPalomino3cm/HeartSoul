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
import com.heartsoul.entities.powerups.Shield;
import com.heartsoul.entities.powerups.SpeedUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen extends BaseScreen {
    // Constantes de configuración
    private static final float PROJECTILE_SPAWN_MIN = 0.5f;
    private static final float PROJECTILE_SPAWN_MAX = 3f;
    private static final float POWERUP_SPAWN_MIN = 8f;
    private static final float POWERUP_SPAWN_MAX = 12f;
    private static final float POWERUP_DURATION = 5f;
    private static final float SPEED_MULTIPLIER = 2f;
    private static final int ENTITY_SIZE = 64;

    private final int round;
    private final int initialScore;
    private final Heart heart;
    private final ShapeRenderer shapeRenderer;
    private final List<Projectile> projectiles;
    private final List<Entity> powerUps;

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
    private float nextProjectileSpawn;
    private float nextPowerUpSpawn;

    private Shield activeShield;
    private SpeedUp activeSpeedUp;

    public GameScreen(Main game, int round, int lives, int score) {
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        this.round = round;
        this.powerUpSpawnTimer = 0f;
        this.activeShield = null;
        this.activeSpeedUp = null;
        this.initialScore = score;
        this.powerUps = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.powerUp = "NINGUNO";
        this.timerAccumulator = 0f;
        this.timerSeconds = 0;
        this.spawnTimer = 0f;
        this.nextProjectileSpawn = MathUtils.random(PROJECTILE_SPAWN_MIN, PROJECTILE_SPAWN_MAX);
        this.nextPowerUpSpawn = MathUtils.random(POWERUP_SPAWN_MIN, POWERUP_SPAWN_MAX);

        // Crear el heart centrado
        this.heart = new Heart(
            getVirtualWidth() / 2 - 30,
            getVirtualHeight() / 2 - 30,
            new Texture(Gdx.files.internal("ui/entities/heart.png"))
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
        float iconWidth = 50f;
        float iconHeight = 40f;
        float iconPadding = 12f;

        // Textos de la primera línea
        String leftText = "VIDAS: ";
        String centerText = "PUNTAJE: " + this.initialScore;
        String rightText = "RONDA: " + this.round;

        // Dibujar Vidas (Izquierda)
        this.game.getSmallFont().setColor(Color.RED);
        this.game.getSmallFont().draw(this.batch, leftText, 10, topY);

        // Medir el texto "VIDAS: " para calcular dónde empezar los íconos
        gl.setText(this.game.getSmallFont(), leftText);

        // Posición X inicial para el primer ícono
        float startX = 10 + gl.width + iconPadding;
        // Posición Y (alinea arriba) - ajustado para mover los íconos más arriba
        float iconY = topY + 10 - iconHeight;

        for (int i = 0; i < this.heart.getLives(); i++) {
            this.batch.draw(
                this.lifeTx,
                startX + i * (iconWidth + iconPadding),
                iconY,
                iconWidth,
                iconHeight
            );
        }

        // Dibujar Puntaje (Centro)
        gl.setText(this.game.getSmallFont(), centerText);
        this.game.getSmallFont().setColor(Color.WHITE);
        this.game.getSmallFont().draw(this.batch, centerText, (getVirtualWidth() - gl.width) / 2f, topY);

        // Dibujar Ronda (Derecha)
        gl.setText(this.game.getSmallFont(), rightText);
        this.game.getSmallFont().setColor(Color.GOLD);
        this.game.getSmallFont().draw(this.batch, rightText, getVirtualWidth() - gl.width - 10f, topY);

        // Textos de la segunda línea
        String powerText = "Power-Up: [" + this.powerUp + "]";
        String timeText = String.format("Tiempo: %02d:%02d", this.timerSeconds / 60, this.timerSeconds % 60);

        // Power-Up a la izquierda (segunda línea)
        this.game.getSmallFont().setColor(Color.CYAN);
        this.game.getSmallFont().draw(this.batch, powerText, 10, secondLineY);

        // Tiempo a la derecha (segunda línea)
        gl.setText(this.game.getSmallFont(), timeText);
        this.game.getSmallFont().setColor(Color.LIGHT_GRAY);
        this.game.getSmallFont().draw(this.batch, timeText, getVirtualWidth() - gl.width - 10f, secondLineY);
    }

    // Método para generar proyectiles
    private void spawnProjectiles(float delta) {
        // Verificar que las texturas estén cargadas
        if (this.bulletTx == null || this.bombTx == null) {
            return;
        }

        this.spawnTimer += delta;

        if (this.spawnTimer >= this.nextProjectileSpawn) {
            this.spawnTimer = 0f;
            this.nextProjectileSpawn = MathUtils.random(PROJECTILE_SPAWN_MIN, PROJECTILE_SPAWN_MAX);

            if (MathUtils.randomBoolean(0.3f)) {
                spawnBomb();
            } else {
                spawnBullet();
            }
        }
    }

    private void spawnBullet() {
        int topLimit = getVirtualHeight() - getTopBarHeight();
        int randomX = MathUtils.random(0, getVirtualWidth() - ENTITY_SIZE);
        int spawnY = topLimit - ENTITY_SIZE;

        this.projectiles.add(new Bullet(randomX, spawnY, this.bulletTx));
    }

    private void spawnBomb() {
        int bottomLimit = getBottomBarHeight();
        int topLimit = getVirtualHeight() - getTopBarHeight();

        float x = MathUtils.random(0, getVirtualWidth() - ENTITY_SIZE);
        float y = MathUtils.random(bottomLimit, topLimit - ENTITY_SIZE);
        float xVel = MathUtils.random(-4f, 4f);
        float yVel = MathUtils.random(-4f, 4f);

        this.projectiles.add(new Bomb((int)x, (int)y, this.bombTx, xVel, yVel));
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

    // Método para generar power-ups
    private void spawnPowerUps(float delta) {
        // Verificar que las texturas estén cargadas
        if (this.shieldTx == null || this.speedUpTx == null) {
            return;
        }

        this.powerUpSpawnTimer += delta;

        if (this.powerUpSpawnTimer >= this.nextPowerUpSpawn) {
            this.powerUpSpawnTimer = 0f;
            this.nextPowerUpSpawn = MathUtils.random(POWERUP_SPAWN_MIN, POWERUP_SPAWN_MAX);

            if (MathUtils.randomBoolean(0.5f)) {
                this.powerUps.add(new Shield(
                    getVirtualWidth(),
                    getVirtualHeight(),
                    this.shieldTx,
                    POWERUP_DURATION
                ));
            } else {
                this.powerUps.add(new SpeedUp(
                    getVirtualWidth(),
                    getVirtualHeight(),
                    this.speedUpTx,
                    POWERUP_DURATION,
                    SPEED_MULTIPLIER
                ));
            }
        }
    }

    @Override
    public void show() {
        registerESC();
        setBackground(new Texture(Gdx.files.internal("ui/background2.png")));
        this.lifeTx = new Texture(Gdx.files.internal("ui/life.png"));

        //Entities
        this.bulletTx = new Texture(Gdx.files.internal("ui/entities/bullet.png"));
        this.bombTx = new Texture(Gdx.files.internal("ui/entities/bomb.png"));
        this.shieldTx = new Texture(Gdx.files.internal("ui/entities/shield.png"));
        this.speedUpTx = new Texture(Gdx.files.internal("ui/entities/speedUp.png"));

        // Cambiar a la música del juego (respetando el estado de pausa)
        SoundManager.getInstance().playMusic("sounds/game_music.mp3", 100);
    }

    // Lineas de contorno que limitan el movimiento del jugador
    private void drawMovementArea(SpriteBatch batch) {
        batch.end();

        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(255, 255, 255, 1); // Blanco

        float x = 1;
        float y = getBottomBarHeight();
        float width = getVirtualWidth() - 2;
        float height = getVirtualHeight() - getTopBarHeight() - getBottomBarHeight();

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

        // Actualizar el temporizador (cuenta hacia arriba)
        this.timerAccumulator += delta;
        if (this.timerAccumulator >= 1f) {
            this.timerAccumulator -= 1f;
            this.timerSeconds++;
        }

        this.heart.update();

        // Fondo (renderBackground() maneja su propio begin/end)
        this.batch.setColor(1f, 1f, 1f, 0.6f);
        renderBackground();
        this.batch.setColor(1f, 1f, 1f, 1f); // Restaurar a blanco opaco

        // Ahora comenzamos el batch para dibujar el resto
        this.batch.begin();
        header();
        this.heart.draw(this.batch, this);

        // Generar proyectiles desde arriba
        spawnProjectiles(delta);

        // Generar power-ups
        spawnPowerUps(delta);

        // Actualizar y dibujar proyectiles usando Iterator para evitar ConcurrentModificationException
        Iterator<Projectile> projectileIterator = this.projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectile.update(getVirtualWidth(), getVirtualHeight());
            projectile.checkBounds(this);

            // Verificar colisión con Heart
            if (!projectile.isDead() && this.heart.checkCollision(projectile)) {
                projectile.onCollision();
            }

            projectile.draw(this.batch, this);

            // Remover si está muerto
            if (projectile.isDead()) {
                projectileIterator.remove();
            }
        }

        // Dibujar power-ups y detectar colisiones usando Iterator
        Iterator<Entity> powerUpIterator = this.powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            Entity powerUp = powerUpIterator.next();
            powerUp.update(getVirtualWidth(), getVirtualHeight());
            powerUp.draw(this.batch, this);

            // Verificar colisión con Heart (recoger power-up)
            if (!powerUp.isDead() && powerUp.getBoundingRectangle().overlaps(this.heart.getBoundingRectangle())) {
                if (powerUp instanceof Shield) {
                    Shield shield = (Shield) powerUp;
                    shield.apply(this.heart);
                    this.activeShield = shield;
                } else if (powerUp instanceof SpeedUp) {
                    SpeedUp speedUp = (SpeedUp) powerUp;
                    speedUp.apply(this.heart);
                    this.activeSpeedUp = speedUp;
                }
            }

            // Remover si está muerto
            if (powerUp.isDead()) {
                powerUpIterator.remove();
            }
        }

        // Actualizar power-ups activos
        if (this.activeShield != null) {
            this.activeShield.updatePowerUp(delta, this.heart);
            if (!this.activeShield.isActive()) {
                this.activeShield = null;
            }
        }

        if (this.activeSpeedUp != null) {
            this.activeSpeedUp.updatePowerUp(delta, this.heart);
            if (!this.activeSpeedUp.isActive()) {
                this.activeSpeedUp = null;
            }
        }

        // Actualizar el display del power-up cada frame para mostrar tiempo restante
        updatePowerUpDisplay();

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

