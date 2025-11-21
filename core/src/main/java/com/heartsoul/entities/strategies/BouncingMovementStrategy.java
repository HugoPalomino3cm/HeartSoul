package com.heartsoul.entities.strategies;

import com.heartsoul.entities.Projectile;
import com.heartsoul.screens.GameScreen;

public class BouncingMovementStrategy implements MovementStrategy {

    @Override
    public void move(Projectile projectile, int virtualWidth, int virtualHeight) {
        // Movimiento simple
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

        // Rebote horizontal
        if (projectile.getX() < 0) {
            projectile.setPosition(0, projectile.getY());
            projectile.setXVelocity(-projectile.getXVelocity());
        } else if (projectile.getX() + projectile.getWidth() > maxW) {
            projectile.setPosition(maxW - projectile.getWidth(), projectile.getY());
            projectile.setXVelocity(-projectile.getXVelocity());
        }

        // Rebote vertical
        if (projectile.getY() < bottomLimit) {
            projectile.setPosition(projectile.getX(), bottomLimit);
            projectile.setYVelocity(-projectile.getYVelocity());
        } else if (projectile.getY() + projectile.getHeight() > topLimit) {
            projectile.setPosition(projectile.getX(), topLimit - projectile.getHeight());
            projectile.setYVelocity(-projectile.getYVelocity());
        }
    }
}
