package com.heartsoul.entities.powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.heartsoul.entities.Entity;
import com.heartsoul.screens.GameScreen;

public interface PowerUp{

    void apply(Entity target);
    String getName();
    float getDuration();
    boolean isActive();
    void update(int virtualWidth, int virtualHeight);
    void draw(SpriteBatch batch, GameScreen game);
    boolean isDead();
    Rectangle getBoundingRectangle();

    default void applyGlowEffect(Sprite sprite, float time) {
        float scale = 1f + MathUtils.sin(time * 4f) * 0.15f;
        sprite.setScale(scale);
    }
}
