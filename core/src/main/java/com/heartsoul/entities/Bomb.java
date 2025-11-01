package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;

public class Bomb extends Projectile {
    private static final int SIZE = 100;
    private static final float LIFETIME = 10f; // segundos
    private static final float DELTA_TIME = 1f / 60f; // asumiendo 60 FPS

    private float lifeTime;

    public Bomb(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel);
        this.lifeTime = 0f;
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        super.update(virtualWidth, virtualHeight); // mueve la bomba según la velocidad

        float minX = 0;
        float minY = 0;
        float maxX = virtualWidth - getWidth();
        float maxY = virtualHeight - getHeight();

        // Lógica extra: rebote
        if (getX() < minX) {
            setPosition(minX, getY());
            setXVelocity(-getXVelocity());
        } else if (getX() > maxX) {
            setPosition(maxX, getY());
            setXVelocity(-getXVelocity());
        }

        if (getY() < minY) {
            setPosition(getX(), minY);
            setYVelocity(-getYVelocity());
        } else if (getY() > maxY) {
            setPosition(getX(), maxY);
            setYVelocity(-getYVelocity());
        }

        // Tiempo de vida
        this.lifeTime += DELTA_TIME;
        if (this.lifeTime > LIFETIME) {
            setDead(true);
        }
    }

    // Getter para tiempo de vida
    public float getLifeTime() {
        return this.lifeTime;
    }
}
