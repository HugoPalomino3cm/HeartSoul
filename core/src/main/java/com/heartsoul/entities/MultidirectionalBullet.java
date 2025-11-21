package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.strategies.LinearMovementStrategy;

public class MultidirectionalBullet extends Projectile {
    private static final int SIZE = 64;

    public MultidirectionalBullet(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel, new LinearMovementStrategy());
    }
}

