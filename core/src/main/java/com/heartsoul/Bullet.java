package com.heartsoul;

import com.badlogic.gdx.graphics.Texture;

public class Bullet extends Projectile {
    public Bullet(int x, int y, Texture tx, float xVel, float yVel) {
        super(x, y, tx, 40, xVel, yVel); // tamaño 30 pixels
    }
}
