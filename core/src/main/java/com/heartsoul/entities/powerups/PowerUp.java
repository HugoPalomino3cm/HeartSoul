package com.heartsoul.entities.powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.heartsoul.entities.Entity;

public interface PowerUp{
    /**
     * Ejecuta el poder sobre la entidad objetivo.
     * @param target Entidad sobre la que se aplica el poder
     */
    void apply(Entity target);

    /**
     * Devuelve el nombre del poder.
     * @return nombre del poder
     */
    String getName();

    /**
     * Devuelve la duración del poder en segundos (si aplica).
     * @return duración, o 0 si es instantáneo
     */
    float getDuration();

    /**
     * Indica si el poder sigue activo.
     * @return true si sigue activo, false si terminó
     */
    boolean isActive();

    /**
     * Aplica un efecto visual de resaltado al powerup.
     * @param sprite Sprite de la entidad
     * @param time Tiempo acumulado para la animación
     */
    default void applyGlowEffect(Sprite sprite, float time) {
        float scale = 1f + MathUtils.sin(time * 4f) * 0.15f;
        sprite.setScale(scale);
    }
}
