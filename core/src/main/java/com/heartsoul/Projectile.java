package com.heartsoul;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.screens.GameScreen;

public abstract class Projectile extends Entity {
    protected float xVel, yVel;

    public Projectile(int x, int y, Texture tx, int size, float xVel, float yVel) {
        super(x, y, tx, size, 1); // normalmente un proyectil tiene 1 vida
        this.xVel = xVel;
        this.yVel = yVel;
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!dead) {
            super.draw(batch, game);
        }
    }

    @Override
    public void update() {
        setPosition(getX() + xVel, getY() + yVel);

        // Si sale de los límites de la pantalla, lo marcamos como muerto
        // (Obtén el GameScreen de contexto donde uses update si necesitas los límites)
        // Aquí se asume que tienes acceso a los límites de pantalla de alguna manera
        // Si necesitas el GameScreen, cambia la firma a update(GameScreen game)
    }

    // Método para verificar si sale de pantalla, llámalo desde el GameScreen o desde update si pasas GameScreen
    public void checkBounds(GameScreen game) {
        int maxW = game.getVirtualWidth();
        int maxH = game.getVirtualHeight();

        if (getX() + getWidth() < 0 || getX() > maxW ||
            getY() + getHeight() < 0 || getY() > maxH) {
            dead = true;
        }
    }

    // Destruir el proyectil al colisionar
    public void onCollision() {
        dead = true;
    }
}
