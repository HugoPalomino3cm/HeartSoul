package com.heartsoul;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public abstract class Projectile extends Entity {
    protected float xVel, yVel;

    // Constructor del proyectil
    public Projectile(int x, int y, Texture tx, int size, float xVel, float yVel) {
        super(x, y, tx, size, 1); // normal un proyectil tiene 1 vida, puedes cambiarlo
        this.xVel = xVel;
        this.yVel = yVel;
    }

    // Dibujar el proyectil si no está muerto
    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!dead) {
            spr.draw(batch);
        }
    }

    // Método para actualizar posición y verificar si sale de pantalla
    public void update(GameScreen game) {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);

        int maxW = game.getVirtualWidth();
        int maxH = game.getVirtualHeight();

        // Destruir si sale de los límites
        if (spr.getX() + spr.getWidth() < 0 || spr.getX() > maxW ||
            spr.getY() + spr.getHeight() < 0 || spr.getY() > maxH) {
            dead = true;
        }
    }

    // Destruir el proyectil al colisionar
    public void onCollision() {
        dead = true;
    }
}
