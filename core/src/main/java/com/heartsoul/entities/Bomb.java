package com.heartsoul.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.screens.GameScreen;

public class Bomb extends Projectile {
    private static final int SIZE = 64;
    private static final float LIFETIME = 10f;

    private float lifeTime;

    public Bomb(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel);
        this.lifeTime = 0f;
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        // Rotar la bomba constantemente
        getSprite().rotate(120f * Gdx.graphics.getDeltaTime());

        // Mover la bomba
        super.update(virtualWidth, virtualHeight);

        // Tiempo de vida
        this.lifeTime += Gdx.graphics.getDeltaTime();
        if (this.lifeTime > LIFETIME) {
            setDead(true);
        }
    }

    @Override
    public void checkBounds(GameScreen game) {
        int maxW = game.getVirtualWidth();
        int bottomLimit = game.getBottomBarHeight();
        int topLimit = game.getVirtualHeight() - game.getTopBarHeight();

        // Rebote horizontal
        if (getX() < 0) {
            setPosition(0, getY());
            setXVelocity(-getXVelocity());
        } else if (getX() + getWidth() > maxW) {
            setPosition(maxW - getWidth(), getY());
            setXVelocity(-getXVelocity());
        }

        // Rebote vertical dentro del área de juego
        if (getY() < bottomLimit) {
            setPosition(getX(), bottomLimit);
            setYVelocity(-getYVelocity());
        } else if (getY() + getHeight() > topLimit) {
            setPosition(getX(), topLimit - getHeight());
            setYVelocity(-getYVelocity());
        }
    }
}

