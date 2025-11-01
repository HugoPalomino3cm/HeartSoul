package com.heartsoul.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class SpeedUp extends Entity implements PowerUp {
    private static final int SIZE = 50;
    private final float duration;
    private final float speedMultiplier;
    private float timeLeft;
    private boolean active;
    private boolean collected;

    // Constructor para aparecer en posición aleatoria
    public SpeedUp(int screenWidth, int screenHeight, Texture tx, float duration, float speedMultiplier) {
        super(
            (int) MathUtils.random(0, screenWidth - SIZE),
            (int) MathUtils.random(100, screenHeight - 150),
            tx,
            SIZE,
            1
        );
        this.duration = duration;
        this.speedMultiplier = speedMultiplier;
        this.timeLeft = duration;
        this.active = false;
        this.collected = false;
    }

    @Override
    public void apply(Entity target) {
        if (target instanceof Heart) {
            Heart heart = (Heart) target;
            heart.setSpeedMultiplier(this.speedMultiplier);
        }
        this.active = true;
        this.collected = true;
        this.timeLeft = this.duration;
        setDead(true); // Desaparece del juego cuando es recogido
    }

    public void updatePowerUp(float delta, Entity target) {
        if (this.active) {
            this.timeLeft -= delta;
            if (this.timeLeft <= 0) {
                this.active = false;
                if (target instanceof Heart) {
                    Heart heart = (Heart) target;
                    heart.setSpeedMultiplier(1f); // Vuelve a velocidad normal
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    public float getTimeLeft() {
        return this.timeLeft;
    }

    public boolean isCollected() {
        return this.collected;
    }
}
