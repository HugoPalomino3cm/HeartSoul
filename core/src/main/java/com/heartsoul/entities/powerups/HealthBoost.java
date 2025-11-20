package com.heartsoul.entities.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.Entity;
import com.heartsoul.entities.Heart;

public class HealthBoost extends AbstractPowerUp {

    public HealthBoost(int screenWidth, int screenHeight, Texture tx, float duration) {
        super(screenWidth, screenHeight, tx, duration);
    }

    @Override
    protected void applyEffect(Entity target) {
        if (target instanceof Heart) {
            Heart heart = (Heart) target;
            heart.setLives(heart.getLives() + 1);
        }
    }

    @Override
    protected void removeEffect(Entity target) {
        // Efecto permanente, no se remueve
    }

    @Override
    protected String getPowerUpName() {
        return "Health";
    }
}

