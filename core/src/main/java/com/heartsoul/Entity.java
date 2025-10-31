package com.heartsoul;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.heartsoul.screens.GameScreen;

public abstract class Entity {
    private Sprite spr;
    private int lives;
    private boolean dead = false;

    public Entity(int x, int y, Texture tx, int size, int lives) {
        this.lives = lives;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setOriginCenter();
        spr.setBounds(x, y, size, size);
    }

    public boolean checkCollision(Entity e) {
        if(e.getBoundingRectangle().overlaps(spr.getBoundingRectangle())){
            lives--;
            if (lives<=0)
                dead = true;
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch, GameScreen game) {
        spr.draw(batch);
    }

    public void update(int virtualWidth, int virtualHeight) {}

    public boolean isDead() { return dead; }
    public int getLives() { return lives; }

    public void setDead(boolean dead) { this.dead = dead; }

    public float getX() { return spr.getX(); }
    public float getY() { return spr.getY(); }
    public float getWidth() { return spr.getWidth(); }
    public float getHeight() { return spr.getHeight(); }
    public void setPosition(float x, float y) { spr.setPosition(x, y); }
    public Rectangle getBoundingRectangle() { return spr.getBoundingRectangle(); }
    public void setLives(int lives) { this.lives = lives; }

    public void setSpeedMultiplier(float speedMultiplier) {
        // Por defecto, no hace nada
    }
}
