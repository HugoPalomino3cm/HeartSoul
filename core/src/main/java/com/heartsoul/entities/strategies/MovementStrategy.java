package com.heartsoul.entities.strategies;

import com.heartsoul.entities.Projectile;
import com.heartsoul.screens.GameScreen;

public interface MovementStrategy {

    void move(Projectile projectile, int virtualWidth, int virtualHeight);
    void checkBounds(Projectile projectile, GameScreen game);
}

