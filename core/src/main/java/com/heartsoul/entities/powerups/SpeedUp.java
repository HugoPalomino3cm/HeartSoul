package com.heartsoul.entities.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.Entity;
import com.heartsoul.entities.Heart;

public class SpeedUp extends AbstractPowerUp {
    private final float speedMultiplier;

    public SpeedUp(int screenWidth, int screenHeight, Texture tx, float duration, float speedMultiplier) {
        super(screenWidth, screenHeight, tx, duration);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    protected void applyEffect(Entity target) {
        if (target instanceof Heart) {
            Heart heart = (Heart) target;
            heart.setSpeedMultiplier(this.speedMultiplier);
        }
    }

    @Override
    protected void removeEffect(Entity target) {
        if (target instanceof Heart) {
            Heart heart = (Heart) target;
            heart.setSpeedMultiplier(1f);
        }
    }

    @Override
    protected String getPowerUpName() {
        return "Speed";
    }
}
