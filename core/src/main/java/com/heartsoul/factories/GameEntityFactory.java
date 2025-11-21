package com.heartsoul.factories;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.Projectile;
import com.heartsoul.entities.powerups.PowerUp;

public interface GameEntityFactory {

    Projectile createBullet(int x, int y, Texture texture);
    Projectile createBomb(int x, int y, Texture texture, float xVel, float yVel);
    PowerUp createShield(int screenWidth, int screenHeight, Texture texture, float duration);
    PowerUp createSpeedUp(int screenWidth, int screenHeight, Texture texture, float duration, float speedMultiplier);
    PowerUp createHealthBoost(int screenWidth, int screenHeight, Texture texture, float duration);
    String getFactoryTheme();
}

