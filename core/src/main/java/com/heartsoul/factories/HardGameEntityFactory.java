package com.heartsoul.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.entities.Bomb;
import com.heartsoul.entities.Bullet;
import com.heartsoul.entities.MultidirectionalBullet;
import com.heartsoul.entities.Projectile;
import com.heartsoul.entities.powerups.HealthBoost;
import com.heartsoul.entities.powerups.PowerUp;
import com.heartsoul.entities.powerups.Shield;
import com.heartsoul.entities.powerups.SpeedUp;

public class HardGameEntityFactory implements GameEntityFactory {

    private static final float HARD_MODE_DURATION_MULTIPLIER = 0.3f; // Power-ups duran solo 30% del tiempo (antes 50%)
    private static final float HARD_BULLET_SPEED = 10f; // Balas MUCHO más rápidas (antes: 7, normal: 5)
    private static final float HARD_BOMB_SPEED_MULTIPLIER = 3.0f; // Bombas 3x más rápidas (antes 2x)

    @Override
    public Projectile createBullet(int x, int y, Texture texture) {
        // En modo difícil, las balas vienen desde múltiples direcciones
        // Seleccionar aleatoriamente una dirección
        int direction = MathUtils.random(7); // 8 direcciones posibles

        float xVel = 0f;
        float yVel = 0f;

        switch (direction) {
            case 0: // Arriba
                xVel = 0f;
                yVel = -HARD_BULLET_SPEED;
                break;
            case 1: // Abajo
                xVel = 0f;
                yVel = HARD_BULLET_SPEED;
                break;
            case 2: // Izquierda
                xVel = -HARD_BULLET_SPEED;
                yVel = 0f;
                break;
            case 3: // Derecha
                xVel = HARD_BULLET_SPEED;
                yVel = 0f;
                break;
            case 4: // Diagonal arriba-izquierda
                xVel = -HARD_BULLET_SPEED * 0.7f;
                yVel = -HARD_BULLET_SPEED * 0.7f;
                break;
            case 5: // Diagonal arriba-derecha
                xVel = HARD_BULLET_SPEED * 0.7f;
                yVel = -HARD_BULLET_SPEED * 0.7f;
                break;
            case 6: // Diagonal abajo-izquierda
                xVel = -HARD_BULLET_SPEED * 0.7f;
                yVel = HARD_BULLET_SPEED * 0.7f;
                break;
            case 7: // Diagonal abajo-derecha
                xVel = HARD_BULLET_SPEED * 0.7f;
                yVel = HARD_BULLET_SPEED * 0.7f;
                break;
        }

        return new MultidirectionalBullet(x, y, texture, xVel, yVel);
    }

    @Override
    public Projectile createBomb(int x, int y, Texture texture, float xVel, float yVel) {
        // Bombas en modo difícil tienen velocidades TRIPLICADAS
        float hardXVel = xVel * HARD_BOMB_SPEED_MULTIPLIER;
        float hardYVel = yVel * HARD_BOMB_SPEED_MULTIPLIER;
        return new Bomb(x, y, texture, hardXVel, hardYVel);
    }

    @Override
    public PowerUp createShield(int screenWidth, int screenHeight, Texture texture, float duration) {
        // En modo difícil, el escudo dura MUCHO menos tiempo (30%)
        return new Shield(screenWidth, screenHeight, texture, duration * HARD_MODE_DURATION_MULTIPLIER);
    }

    @Override
    public PowerUp createSpeedUp(int screenWidth, int screenHeight, Texture texture, float duration, float speedMultiplier) {
        // En modo difícil, el speed boost dura mucho menos (30%)
        return new SpeedUp(screenWidth, screenHeight, texture, duration * HARD_MODE_DURATION_MULTIPLIER, speedMultiplier);
    }

    @Override
    public PowerUp createHealthBoost(int screenWidth, int screenHeight, Texture texture, float duration) {
        // En modo difícil, el health boost es igual (es instantáneo)
        return new HealthBoost(screenWidth, screenHeight, texture, duration);
    }

    @Override
    public String getFactoryTheme() {
        return "Hard Mode - Extreme Challenge";
    }
}
