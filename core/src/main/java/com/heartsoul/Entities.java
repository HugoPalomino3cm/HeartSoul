package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Entities {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private Sprite spr;

    public Entities(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;

        //validar que borde de esfera no quede fuera
        if (x-size < 0) this.x = x+size;
        if (x+size > Gdx.graphics.getWidth())this.x = x-size;

        this.y = y;
        //validar que borde de esfera no quede fuera
        if (y-size < 0) this.y = y+size;
        if (y+size > Gdx.graphics.getHeight())this.y = y-size;

        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }

    public void update() {
        x += getXSpeed();
        y += getySpeed();

        if (x+getXSpeed() < 0 || x+getXSpeed()+spr.getWidth() > Gdx.graphics.getWidth())
            setXSpeed(getXSpeed() * -1);
        if (y+getySpeed() < 0 || y+getySpeed()+spr.getHeight() > Gdx.graphics.getHeight())
            setySpeed(getySpeed() * -1);
        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Entities enemies) {
        if(spr.getBoundingRectangle().overlaps(enemies.spr.getBoundingRectangle())){
            // rebote
            if (getXSpeed() ==0) setXSpeed(getXSpeed() + enemies.getXSpeed()/2);
            if (enemies.getXSpeed() ==0) enemies.setXSpeed(enemies.getXSpeed() + getXSpeed()/2);
            setXSpeed(- getXSpeed());
            enemies.setXSpeed(-enemies.getXSpeed());

            if (getySpeed() ==0) setySpeed(getySpeed() + enemies.getySpeed()/2);
            if (enemies.getySpeed() ==0) enemies.setySpeed(enemies.getySpeed() + getySpeed()/2);
            setySpeed(- getySpeed());
            enemies.setySpeed(- enemies.getySpeed());
        }
    }
    public int getXSpeed() {
        return xSpeed;
    }
    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }
    public int getySpeed() {
        return ySpeed;
    }
    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
