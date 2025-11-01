package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.SoundManager;
import com.heartsoul.screens.GameScreen;

public class Heart extends Entity {
    private static final int MAX_HURT_TIME = 50;
    private static final int DEFAULT_SIZE = 60;
    private static final int DEFAULT_LIVES = 3;

    private boolean hit;
    private int hurtTime;
    private float speedMultiplier;
    private boolean invulnerable;

    public Heart(int x, int y, Texture tx) {
        super(x, y, tx, DEFAULT_SIZE, DEFAULT_LIVES);
        this.hit = false;
        this.hurtTime = 0;
        this.speedMultiplier = 1.0f;
        this.invulnerable = false;
    }

    public void move(float dx, float dy, GameScreen game) {
        if (!this.hit) {
            float newX = getX() + (dx * this.speedMultiplier);
            float newY = getY() + (dy * this.speedMultiplier);

            int maxW = game.getVirtualWidth();
            int maxH = game.getVirtualHeight();
            int bottomLimit = game.getBottomBarHeight();
            int topLimit = maxH - game.getTopBarHeight();

            if (newX < 0) {
                newX = 0;
            }
            if (newX + getWidth() > maxW) {
                newX = maxW - getWidth();
            }
            if (newY < bottomLimit) {
                newY = bottomLimit;
            }
            if (newY + getHeight() > topLimit) {
                newY = topLimit - getHeight();
            }

            setPosition(newX, newY);
        }
    }

    public void update() {
        if (this.hit) {
            this.hurtTime--;
            if (this.hurtTime <= 0) {
                this.hit = false;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game){
        if (this.hit) {
            float shakeOffset = MathUtils.random(-2, 2);

            // Aplica shake solo al dibujar, usando los métodos encapsulados
            setPosition(getX() + shakeOffset, getY());
            super.draw(batch, game);
            setPosition(getX() - shakeOffset, getY());
        } else {
            super.draw(batch, game);
        }
    }

    @Override
    public boolean checkCollision(Entity e) {
        if(this.invulnerable) {
            return false; // No recibe daño si está invulnerable
        }
        if(!this.hit && e.getBoundingRectangle().overlaps(getBoundingRectangle())) {
            this.hit = true;
            this.hurtTime = MAX_HURT_TIME;
            playHitSound();
            super.checkCollision(e);
            return true;
        }
        return false;
    }

    private void playHitSound() {
        SoundManager.getInstance().playSound("sounds/hit.mp3");
    }

    // Getters
    public boolean isHit() {
        return this.hit;
    }

    public float getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    // Setters
    @Override
    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

}
