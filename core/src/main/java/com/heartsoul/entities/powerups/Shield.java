package com.heartsoul.entities.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.entities.Entity;
import com.heartsoul.entities.Heart;
import com.heartsoul.screens.GameScreen;

public class Shield extends Entity implements PowerUp {
    private static final int SIZE = 64;
    private final float duration;
    private float timeLeft;
    private boolean active;
    private boolean collected;
    private float animTime;

    // Constructor para aparecer en posición aleatoria
    public Shield(int screenWidth, int screenHeight, Texture tx, float duration) {
        super(
            (int) MathUtils.random(0, screenWidth - SIZE),
            (int) MathUtils.random(100, screenHeight - 150),
            tx,
            SIZE,
            1
        );
        this.duration = duration;
        this.timeLeft = duration;
        this.active = false;
        this.collected = false;
        this.animTime = 0;
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game) {
        if (!collected) {
            applyGlowEffect(getSprite(), animTime);
        }
        super.draw(batch, game);
    }

    @Override
    public void update(int virtualWidth, int virtualHeight) {
        animTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void apply(Entity target) {
        if (target instanceof Heart) {
            Heart heart = (Heart) target;
            heart.setInvulnerable(true);
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
                    heart.setInvulnerable(false);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Shield";
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
