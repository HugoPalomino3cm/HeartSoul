package com.heartsoul;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends Projectile {
    private static final int SIZE = 20;

    // Constructor estándar, por si quieres usarlo manualmente
    public Bullet(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, SIZE, xVel, yVel);
    }

    // Constructor de conveniencia: crea un bullet random desde arriba
    public Bullet(int screenWidth, int screenHeight, Texture tx) {
        super(MathUtils.random(0, screenWidth - SIZE), screenHeight - SIZE, tx, SIZE, 0f, -5f);
    }
}
