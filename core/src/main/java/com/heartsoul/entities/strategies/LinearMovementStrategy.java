package com.heartsoul.entities.strategies;

import com.heartsoul.entities.Projectile;
import com.heartsoul.screens.GameScreen;


public class LinearMovementStrategy implements MovementStrategy {

    @Override
    public void move(Projectile projectile, int virtualWidth, int virtualHeight) {
        // Movimiento lineal simple
        projectile.setPosition(
            projectile.getX() + projectile.getXVelocity(),
            projectile.getY() + projectile.getYVelocity()
        );
    }

    @Override
    public void checkBounds(Projectile projectile, GameScreen game) {
        int maxW = game.getVirtualWidth();
        int bottomLimit = game.getBottomBarHeight();
        int topLimit = game.getVirtualHeight() - game.getTopBarHeight();

        // Destruir si sale de pantalla
        if (projectile.getX() + projectile.getWidth() < 0 ||
            projectile.getX() > maxW ||
            projectile.getY() + projectile.getHeight() < bottomLimit ||
            projectile.getY() > topLimit) {
            projectile.setDead(true);
        }
    }
}

