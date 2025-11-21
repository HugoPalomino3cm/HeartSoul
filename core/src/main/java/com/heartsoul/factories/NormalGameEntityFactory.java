package com.heartsoul.factories;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.Bomb;
import com.heartsoul.entities.Bullet;
import com.heartsoul.entities.Projectile;
import com.heartsoul.entities.powerups.HealthBoost;
import com.heartsoul.entities.powerups.PowerUp;
import com.heartsoul.entities.powerups.Shield;
import com.heartsoul.entities.powerups.SpeedUp;

public class NormalGameEntityFactory implements GameEntityFactory {

    @Override
    public Projectile createBullet(int x, int y, Texture texture) {
        return new Bullet(x, y, texture);
    }

    @Override
    public Projectile createBomb(int x, int y, Texture texture, float xVel, float yVel) {
        return new Bomb(x, y, texture, xVel, yVel);
    }

    @Override
    public PowerUp createShield(int screenWidth, int screenHeight, Texture texture, float duration) {
        return new Shield(screenWidth, screenHeight, texture, duration);
    }

    @Override
    public PowerUp createSpeedUp(int screenWidth, int screenHeight, Texture texture, float duration, float speedMultiplier) {
        return new SpeedUp(screenWidth, screenHeight, texture, duration, speedMultiplier);
    }

    @Override
    public PowerUp createHealthBoost(int screenWidth, int screenHeight, Texture texture, float duration) {
        return new HealthBoost(screenWidth, screenHeight, texture, duration);
    }

    @Override
    public String getFactoryTheme() {
        return "Normal Mode";
    }
}

