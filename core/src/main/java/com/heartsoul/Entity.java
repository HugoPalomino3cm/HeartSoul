package com.heartsoul;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity {
    protected Sprite spr;
    protected int lives;
    protected boolean dead = false;

    public Entity(int x, int y, Texture tx, int size, int lives) {
        this.lives = lives;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setOriginCenter();
        spr.setBounds(x, y, size, size);
    }

    public abstract void draw(SpriteBatch batch, GameScreen game);

    public boolean isDead() {
        return dead;
    }

    public int getLives() {
        return lives;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
