package com.heartsoul;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.screens.GameScreen;

public class Heart extends Entity {
    private static final int MAX_HURT_TIME = 50;
    private boolean hit = false;
    private int hurtTime;

    public Heart(int x, int y, Texture tx) {
        super(x, y, tx, 60, 3); // tamaño 60, 3 vidas
    }

    public void move(float dx, float dy, GameScreen game) {
        if (!hit) {
            float newX = getX() + dx;
            float newY = getY() + dy;

            int maxW = game.getVirtualWidth();
            int maxH = game.getVirtualHeight();
            int bottomLimit = game.getBottomBarHeight();
            int topLimit = maxH - game.getTopBarHeight();

            if (newX < 0) newX = 0;
            if (newX + getWidth() > maxW) newX = maxW - getWidth();
            if (newY < bottomLimit) newY = bottomLimit;
            if (newY + getHeight() > topLimit) newY = topLimit - getHeight();

            setPosition(newX, newY);
        }
    }

    public void update() {
        if (hit) {
            hurtTime--;
            if (hurtTime <= 0) hit = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game){
        if (hit) {
            float shakeOffset = MathUtils.random(-2, 2);

            // Aplica shake solo al dibujar, usando los métodos encapsulados
            setPosition(getX() + shakeOffset, getY());
            super.draw(batch, game); // Llama a la base para dibujar el sprite
            setPosition(getX() - shakeOffset, getY());
        } else {
            super.draw(batch, game); // Llama a la base para dibujar el sprite
        }
    }

    @Override
    public boolean checkCollision(Entity e) {
        if(!hit && e.getBoundingRectangle().overlaps(getBoundingRectangle())) {
            hit = true;
            hurtTime = MAX_HURT_TIME;
            try {
                com.badlogic.gdx.audio.Sound hitSound =
                    com.badlogic.gdx.Gdx.audio.newSound(
                        com.badlogic.gdx.Gdx.files.internal("sounds/hitSound.mp3")
                    );
                hitSound.play();
            } catch (Exception ex) {
                ex.printStackTrace(); // Muestra el error en consola
            }
            super.checkCollision(e);
            return true;
        }
        return false;
    }

    public boolean isHit() {
        return hit;
    }
}
