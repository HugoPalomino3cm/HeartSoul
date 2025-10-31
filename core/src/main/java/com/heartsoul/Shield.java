package com.heartsoul;

public class Shield implements PowerUp {
    private float duration;
    private float timeLeft;
    private boolean active;

    public Shield(float duration) {
        this.duration = duration;
        this.timeLeft = duration;
        this.active = true;
    }

    @Override
    public void apply(Entity target) {
        // Ejemplo: podrías tener un método setInvulnerable en Heart
        if (target instanceof Heart) {
            //((Heart) target).setInvulnerable(true);
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
                    //((Heart) target).setInvulnerable(false);
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
        return duration;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
