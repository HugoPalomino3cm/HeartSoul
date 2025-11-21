package com.heartsoul.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.screens.GameScreen;
import com.heartsoul.entities.strategies.MovementStrategy;

public abstract class Projectile extends Entity {
    private float xVelocity;
    private float yVelocity;
    private MovementStrategy movementStrategy;

    public Projectile(int x, int y, Texture tx, int size, float xVel, float yVel, MovementStrategy strategy) {
        super(x, y, tx, size, 1);
        this.xVelocity = xVel;
        this.yVelocity = yVel;
        this.movementStrategy = strategy;
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!isDead()) {
            super.draw(batch, game);
        }
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        // Delegar el movimiento a la estrategia
        if (this.movementStrategy != null) {
            this.movementStrategy.move(this, virtualWidth, virtualHeight);
        }
    }

    /**
     * Método para verificar límites usando la estrategia
     */
    public void checkBounds(GameScreen game) {
        if (this.movementStrategy != null) {
            this.movementStrategy.checkBounds(this, game);
        }
    }

    /**
     * Destruir el proyectil al colisionar
     */
    public void onCollision() {
        setDead(true);
    }

    // Getters
    public float getXVelocity() {
        return this.xVelocity;
    }

    public float getYVelocity() {
        return this.yVelocity;
    }

    public MovementStrategy getMovementStrategy() {
        return this.movementStrategy;
    }

    // Setters
    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    /**
     * Permite cambiar la estrategia de movimiento en tiempo de ejecución
     */
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }
}
