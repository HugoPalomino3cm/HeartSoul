package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.strategies.LinearMovementStrategy;

/**
 * Patrón Strategy - Client
 * Bullet usa la estrategia de movimiento lineal
 */
public class Bullet extends Projectile {
    private static final int SIZE = 64;

    public Bullet(int x, int y, Texture tx) {
        super(x, y, tx, SIZE, 0f, -5f, new LinearMovementStrategy());
    }
}
