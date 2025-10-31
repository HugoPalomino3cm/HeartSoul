package com.heartsoul;

import com.badlogic.gdx.graphics.Texture;

public class Bomb extends Projectile {
    private static final int SIZE = 100;
    private static final float LIFETIME = 10f; // segundos
    private float life = 0f;

    public Bomb(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel);
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        super.update(virtualWidth, virtualHeight); // mueve la bomba según la velocidad

        float minX = 0, minY = 0;
        float maxX = virtualWidth - getWidth();
        float maxY = virtualHeight - getHeight();

        // Lógica extra: rebote y tiempo de vida
        if (getX() < minX) {
            setPosition(minX, getY());
            xVel = -xVel;
        } else if (getX() > maxX) {
            setPosition(maxX, getY());
            xVel = -xVel;
        }

        if (getY() < minY) {
            setPosition(getX(), minY);
            yVel = -yVel;
        } else if (getY() > maxY) {
            setPosition(getX(), maxY);


            life += 1f / 60f; // o usa un delta real si lo tienes
            if (life > LIFETIME) setDead(true);
        }
    }
}
