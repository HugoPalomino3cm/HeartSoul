package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends Projectile {
    private static final int SIZE = 64;

    public Bullet(int screenWidth, int screenHeight, Texture tx) {
        super((int) MathUtils.random(0, screenWidth - SIZE), screenHeight - SIZE, tx, SIZE, 0f, -5f);
    }
}
