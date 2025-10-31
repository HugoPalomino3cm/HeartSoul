package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Heart extends Entity {
    private float xVel = 0;
    private float yVel = 0;
    private boolean hit = false;
    private int maxHurtTime = 50;
    private int hurtTime;

    public Heart(int x, int y, Texture tx) {
        super(x, y, tx, 45, 3); // tamaño 45, 3 vidas
    }

    @Override
    public void draw(SpriteBatch batch, GameScreen game){
        float x = spr.getX();
        float y = spr.getY();
        if (!hit) {
            // Movimiento horizontal
            if (Gdx.input.isKeyPressed(Input.Keys.A)) xVel = -2;
            else if (Gdx.input.isKeyPressed(Input.Keys.D)) xVel = 2;
            else xVel = 0;
            // Movimiento vertical
            if (Gdx.input.isKeyPressed(Input.Keys.S)) yVel = -2;
            else if (Gdx.input.isKeyPressed(Input.Keys.W)) yVel = 2;
            else yVel = 0;

            float newX = x + xVel;
            float newY = y + yVel;

            int maxW = game.getVirtualWidth();
            int maxH = game.getVirtualHeight();
            int bottomLimit = game.getBottomBarHeight();
            int topLimit = maxH - game.getTopBarHeight();

            if (newX < 0) newX = 0;
            if (newX + spr.getWidth() > maxW) newX = maxW - spr.getWidth();
            if (newY < bottomLimit) newY = bottomLimit;
            if (newY + spr.getHeight() > topLimit) newY = topLimit - spr.getHeight();

            spr.setPosition(newX, newY);
            spr.setBounds(newX, newY, spr.getWidth(), spr.getHeight());

            spr.draw(batch);
        } else {
            spr.setX(spr.getX()+MathUtils.random(-2,2));
            spr.draw(batch);
            spr.setX(x);
            hurtTime--;
            if (hurtTime<=0) hit = false;
        }
    }

    public boolean checkCollision(Entity b) {
        if(!hit && b.spr.getBoundingRectangle().overlaps(spr.getBoundingRectangle())){
            lives--;
            hit = true;
            hurtTime=maxHurtTime;
            if (lives<=0)
                dead = true;
            return true;
        }
        return false;
    }

    public boolean isHit() {
        return hit;
    }
}
