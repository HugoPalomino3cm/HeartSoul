package com.heartsoul.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.heartsoul.screens.GameScreen;

public abstract class Entity {
    private final Sprite sprite;
    private int lives;
    private boolean dead;

    public Entity(int x, int y, Texture tx, int size, int lives) {
        this.lives = lives;
        this.dead = false;
        this.sprite = new Sprite(tx);
        this.sprite.setPosition(x, y);
        this.sprite.setOriginCenter();
        this.sprite.setBounds(x, y, size, size);
    }

    public boolean checkCollision(Entity e) {
        if(e.getBoundingRectangle().overlaps(this.sprite.getBoundingRectangle())){
            this.lives--;
            if (this.lives <= 0) {
                this.dead = true;
            }
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch, GameScreen game) {
        this.sprite.draw(batch);
    }

    public void update(int virtualWidth, int virtualHeight) {
        // Método por defecto, se sobrescribe en subclases
    }

    // Getters
    public boolean isDead() {
        return this.dead;
    }

    public int getLives() {
        return this.lives;
    }

    public float getX() {
        return this.sprite.getX();
    }

    public float getY() {
        return this.sprite.getY();
    }

    public float getWidth() {
        return this.sprite.getWidth();
    }

    public float getHeight() {
        return this.sprite.getHeight();
    }

    public Rectangle getBoundingRectangle() {
        return this.sprite.getBoundingRectangle();
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setPosition(float x, float y) {
        this.sprite.setPosition(x, y);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        // Lo utiliza el poder SpeedUp
    }
}
