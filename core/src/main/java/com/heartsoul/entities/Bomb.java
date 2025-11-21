package com.heartsoul.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.strategies.BouncingMovementStrategy;

/**
 * Patrón Strategy - Client
 * Bomb usa la estrategia de movimiento con rebote
 */
public class Bomb extends Projectile {
    private static final int SIZE = 64;
    private static final float LIFETIME = 10f;

    private float lifeTime;

    public Bomb(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel, new BouncingMovementStrategy());
        this.lifeTime = 0f;
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        // Rotar la bomba constantemente
        getSprite().rotate(120f * Gdx.graphics.getDeltaTime());

        // Delegar movimiento a la estrategia (llamada al padre)
        super.update(virtualWidth, virtualHeight);

        // Tiempo de vida
        this.lifeTime += Gdx.graphics.getDeltaTime();
        if (this.lifeTime > LIFETIME) {
            setDead(true);
        }
    }
}
