package com.heartsoul.entities;

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
}
