package com.heartsoul.factories;

import com.badlogic.gdx.graphics.Texture;
import com.heartsoul.entities.Bomb;
import com.heartsoul.entities.Bullet;
import com.heartsoul.entities.Projectile;
import com.heartsoul.entities.powerups.HealthBoost;
import com.heartsoul.entities.powerups.PowerUp;
import com.heartsoul.entities.powerups.Shield;
import com.heartsoul.entities.powerups.SpeedUp;

public class EasyGameEntityFactory implements GameEntityFactory {

    private static final float EASY_MODE_SPEED_MULTIPLIER = 0.7f; // Proyectiles 30% más lentos
    private static final float EASY_MODE_DURATION_MULTIPLIER = 1.5f; // Power-ups duran 50% más

    @Override
    public Projectile createBullet(int x, int y, Texture texture) {
        // En modo fácil, las balas son más lentas
        // Nota: Para implementar velocidad reducida, necesitaríamos modificar Bullet
        // Por ahora retorna bala normal (se puede extender en el futuro)
        return new Bullet(x, y, texture);
    }

    @Override
    public Projectile createBomb(int x, int y, Texture texture, float xVel, float yVel) {
        // Bombas en modo fácil tienen velocidades reducidas
        float easyXVel = xVel * EASY_MODE_SPEED_MULTIPLIER;
        float easyYVel = yVel * EASY_MODE_SPEED_MULTIPLIER;
        return new Bomb(x, y, texture, easyXVel, easyYVel);
    }

    @Override
    public PowerUp createShield(int screenWidth, int screenHeight, Texture texture, float duration) {
        // En modo fácil, el escudo dura más tiempo
        return new Shield(screenWidth, screenHeight, texture, duration * EASY_MODE_DURATION_MULTIPLIER);
    }

    @Override
    public PowerUp createSpeedUp(int screenWidth, int screenHeight, Texture texture, float duration, float speedMultiplier) {
        // En modo fácil, el speed boost dura más
        return new SpeedUp(screenWidth, screenHeight, texture, duration * EASY_MODE_DURATION_MULTIPLIER, speedMultiplier);
    }

    @Override
    public PowerUp createHealthBoost(int screenWidth, int screenHeight, Texture texture, float duration) {
        // En modo fácil, el health boost es igual (es instantáneo)
        return new HealthBoost(screenWidth, screenHeight, texture, duration);
    }

    @Override
    public String getFactoryTheme() {
        return "Easy Mode";
    }
}

