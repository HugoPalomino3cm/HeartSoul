package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {
    private final int round;
    private final int lives;
    private final int score;
    private final Heart heart;
    private final ShapeRenderer shapeRenderer;
    private final List<Projectile> projectiles = new ArrayList<>();


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

        // Dibujar fondo de la barra superior con ShapeRenderer (finaliza batch temporalmente)
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.5f));
        shapeRenderer.rect(0, VIRTUAL_HEIGHT - getTopBarHeight() - 40, VIRTUAL_WIDTH, getTopBarHeight() + 40);
        shapeRenderer.end();
        batch.begin();

        // Preparar corazones
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < heart.getLives(); i++) {
            hearts.append("♡"); // corazón
            if (i < heart.getLives() - 1) hearts.append(" ");
        }

        // Posiciones de texto (dos líneas dentro de la barra superior)
        float topY = VIRTUAL_HEIGHT - 10f;
        float secondLineY = VIRTUAL_HEIGHT - 50f;

        // Primera línea: Vidas / PUNTAJE / RONDA
        String leftText = "VIDAS: " + hearts.toString();
        String centerText = "PUNTAJE: " + this.score;
        String rightText = "RONDA: " + this.round;

        // Dibujar izquierda
        game.getSmallFont().setColor(Color.RED);
        game.getSmallFont().draw(batch, leftText, 10, topY);

        // Dibujar centro
        GlyphLayout gl = new GlyphLayout(game.getSmallFont(), centerText);
        game.getSmallFont().setColor(Color.WHITE);
        game.getSmallFont().draw(batch, centerText, (VIRTUAL_WIDTH - gl.width) / 2f, topY);

        // Dibujar derecha
        gl.setText(game.getSmallFont(), rightText);
        game.getSmallFont().setColor(Color.GOLD);
        game.getSmallFont().draw(batch, rightText, VIRTUAL_WIDTH - gl.width - 10f, topY);

        // Segunda línea: Power-Up y Tiempo
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

        // variable para intervalo aleatorio
        float randomInterval = com.badlogic.gdx.math.MathUtils.random(0.5f, 3f);

        //
        if (spawnTimer >= randomInterval) {
            spawnTimer = 0f;

            // Crear textura de la bala (usa heart.png temporalmente)
            Texture bulletTx = new Texture(Gdx.files.internal("ui/bullet.png"));

            // Crear bala desde la izquierda moviéndose hacia la derecha
            Bullet bullet = new Bullet(
                0, // x inicial (borde izquierdo)
                VIRTUAL_HEIGHT / 2, // y inicial (centro vertical)
                bulletTx,
                3f, // velocidad x (hacia la derecha)
                0f  // velocidad y (sin movimiento vertical)
            );

            projectiles.add(bullet);
        }
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

        // generar proyectiles
        spawnProjectiles(delta);

        // AQUÍ VA EL CÓDIGO DE LOS PROYECTILES ⬇️
        // Actualizar proyectiles
        for (Projectile projectile : projectiles) {
            projectile.update(this);

            // Verificar colisión con Heart
            if (!projectile.isDead() && heart.checkCollision(projectile)) {
                projectile.onCollision(); // Destruir proyectil
            }

            projectile.draw(batch, this);
        }

        // Remover proyectiles destruidos
        projectiles.removeIf(Entity::isDead);
        // FIN DEL CÓDIGO DE PROYECTILES ⬆️

        drawMovementArea(batch);

        /*
        if (!heart.isHit()) {
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile b = projectiles.get(i);
                // Código comentado que usaba balls1 y balls2
                // for (int j = 0; j < balls1.size(); j++) {
                //     if (b.checkCollision(balls1.get(j))) {
                //         explosionSound.play();
                //         balls1.remove(j);
                //         balls2.remove(j);
                //         j--;
                //         score +=10;
                //     }
                // }

                if (b.isDead()) {
                    projectiles.remove(b);
                    i--;
                }
            }
        }

        // colisiones entre asteroides y sus rebotes
        // for (int i=0; i<projectiles.size(); i++) {
        //     Projectile ball1 = projectiles.get(i);
        //     for (int j=0; j<projectiles.size(); j++) {
        //         Projectile ball2 = projectiles.get(j);
        //         if (i<j) {
        //             ball1.checkCollision(ball2);
        //         }
        //     }
        // }

        // dibujar asteroides y manejar colision con nave
        // for (int i = 0; i < balls1.size(); i++) {
        //     Ball2 b = balls1.get(i);
        //     b.draw(batch);
        //     if (heart.checkCollision(b)) {
        //         balls1.remove(i);
        //         balls2.remove(i);
        //         i--;
        //     }
        // }
        */

        if (heart.isDead()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            // Screen ss = new GameOverScreen(game);
            // game.setScreen(ss);
            // dispose();
        }

        batch.end();

        // nivel completado
        /*
        if (projectiles.size() == 0) {
            Screen ss = new GameScreen(game, round+1, heart.getLives(), score);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
        */
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
