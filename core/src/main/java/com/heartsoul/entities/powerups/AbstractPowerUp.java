package com.heartsoul.entities.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.heartsoul.entities.Entity;
import com.heartsoul.screens.GameScreen;

/**
 * Clase abstracta base para Power-Ups
 * Proporciona funcionalidad común para todos los power-ups
 */
public abstract class AbstractPowerUp implements PowerUp {
    private static final int SIZE = 64;
    private static final float FALL_SPEED = 2f;

    private final Sprite sprite;
    private final float duration;
    private float timeLeft;
    private float lifeTime;
    private boolean dead;
    private boolean active;
    private Entity currentTarget;

    public AbstractPowerUp(int screenWidth, int screenHeight, Texture tx, float duration) {
        this.duration = duration;
        this.timeLeft = duration;
        this.lifeTime = 0f;
        this.dead = false;
        this.active = false;
        this.currentTarget = null;

        // Posición aleatoria en la parte superior
        int randomX = MathUtils.random(0, screenWidth - SIZE);
        int spawnY = screenHeight - SIZE;

        this.sprite = new Sprite(tx);
        this.sprite.setPosition(randomX, spawnY);
        this.sprite.setOriginCenter();
        this.sprite.setBounds(randomX, spawnY, SIZE, SIZE);
    }

    @Override
    public void apply(Entity target) {
        if (!this.active) {
            this.active = true;
            this.currentTarget = target;
            applyEffect(target);
            this.dead = true; // El power-up desaparece al ser recogido
        }
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        if (!this.active) {
            // Mover hacia abajo
            this.sprite.setPosition(this.sprite.getX(), this.sprite.getY() - FALL_SPEED);

            // Efecto de brillo
            this.lifeTime += Gdx.graphics.getDeltaTime();
            applyGlowEffect(this.sprite, this.lifeTime);

            // Verificar si sale de pantalla
            if (this.sprite.getY() + SIZE < 0) {
                this.dead = true;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!this.dead && !this.active) {
            this.sprite.draw(batch);
        }
    }

    /**
     * Actualiza el power-up activo (reduce tiempo restante)
     */
    public void updatePowerUp(float delta, Entity target) {
        if (this.active) {
            this.timeLeft -= delta;
            if (this.timeLeft <= 0) {
                this.active = false;
                removeEffect(target);
            }
        }
    }

    @Override
    public String getName() {
        return getPowerUpName();
    }

    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return this.sprite.getBoundingRectangle();
    }

    public float getTimeLeft() {
        return this.timeLeft;
    }

    // Métodos abstractos que deben implementar las subclases
    protected abstract void applyEffect(Entity target);
    protected abstract void removeEffect(Entity target);
    protected abstract String getPowerUpName();
}

