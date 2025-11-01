package com.heartsoul.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.screens.GameScreen;

public abstract class Projectile extends Entity {
    private float xVelocity;
    private float yVelocity;

    public Projectile(int x, int y, Texture tx, int size, float xVel, float yVel) {
        super(x, y, tx, size, 1); // normalmente un proyectil tiene 1 vida
        this.xVelocity = xVel;
        this.yVelocity = yVel;
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!isDead()) {
            super.draw(batch, game);
        }
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        setPosition(getX() + this.xVelocity, getY() + this.yVelocity);
    }

    /**
     * Método para verificar si el proyectil sale de pantalla
     */
    public void checkBounds(GameScreen game) {
        int maxW = game.getVirtualWidth();
        int maxH = game.getVirtualHeight();

        if (getX() + getWidth() < 0 || getX() > maxW ||
            getY() + getHeight() < 0 || getY() > maxH) {
            setDead(true);
        }
    }

    /**
     * Destruir el proyectil al colisionar
     */
    public void onCollision() {
        setDead(true);
    }

    // Getters
    protected float getXVelocity() {
        return this.xVelocity;
    }

    protected float getYVelocity() {
        return this.yVelocity;
    }

    // Setters
    protected void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    protected void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }
}
