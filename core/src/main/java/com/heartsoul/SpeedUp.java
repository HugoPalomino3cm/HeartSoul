package com.heartsoul;

public class SpeedUp implements PowerUp{
    private float duration;
    private float timeLeft;
    private boolean active;
    private float speedMultiplier;

    public SpeedUp(float duration) {
        this.duration = duration;
        this.speedMultiplier = 1;
        this.timeLeft = duration;
        this.active = false;
    }

    @Override
    public void apply(Entity target) {
        if (target instanceof Heart) {
            ((Heart) target).setSpeedMultiplier(speedMultiplier);
        }
        active = true;
        timeLeft = duration;
    }

    public void update(float delta, Entity target) {
        if (active) {
            timeLeft -= delta;
            if (timeLeft <= 0) {
                active = false;
                if (target instanceof Heart) {
                    ((Heart) target).setSpeedMultiplier(1f); // Vuelve a velocidad normal
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
        return duration;
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
