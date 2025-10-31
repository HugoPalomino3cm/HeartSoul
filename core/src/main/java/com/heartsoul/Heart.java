package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Heart {
    private boolean dead = false;
    private int lives = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    //private Sound hitSound;
    private boolean hit = false;
    private int maxHurtTime = 50;
    private int hurtTime;

    public Heart(int x, int y, Texture tx) {
        //hitSound = soundChoque;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);

    }

    // Movimiento con teclas y lógica de hit
    public void draw(SpriteBatch batch, GameScreen game){
        float x =  spr.getX();
        float y =  spr.getY();
        if (!hit) {
            // Movimiento horizontal
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                xVel = -2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                xVel = 2;
            } else {
                xVel = 0;
            }

            // Movimiento vertical
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                yVel = -2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                yVel = 2;
            } else {
                yVel = 0;
            }

            // Calcular nueva posición
            float newX = x + xVel;
            float newY = y + yVel;

            // Usar dimensiones virtuales del GameScreen para limitar movimiento
            int maxW = game.getVirtualWidth();
            int maxH = game.getVirtualHeight();

            int bottomLimit = game.getBottomBarHeight();
            int topLimit = maxH - game.getTopBarHeight();

            // Hitbox - limitar movimiento a los bordes de la pantalla y respetar las barras invisibles
            if (newX < 0) newX = 0;
            if (newX + spr.getWidth() > maxW)
                newX = maxW - spr.getWidth();
            if (newY < bottomLimit) newY = bottomLimit;
            if (newY + spr.getHeight() > topLimit)
                newY = topLimit - spr.getHeight();

            // Actualizar posición y bounds
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

    public boolean checkCollision(Entities b) {
        if(!hit && b.getArea().overlaps(spr.getBoundingRectangle())){
            lives--;
            hit = true;
            hurtTime=maxHurtTime;

            //hitSound.play();
            if (lives<=0)
                dead = true;
            return true;

        }
        return false;
    }

    public boolean isDead() {
        return !hit && dead;
    }
    public boolean isHit() {
        return hit;
    }

    public int getLives() {return lives;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public void setLives(int lives2) {lives = lives2;}
}
