/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettolabirynt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author HP
 */
public class Sprite {
    
    private Rectangle sprite;
    private boolean moveSprite = false;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private boolean end = false;
    
    
    public Sprite(double positionX, double positionY, double velocityX, double velocityY, double width, double height){
        setSprite(positionX, positionY, width, height);  
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    public double getPositionX(){
        return positionX;
    }

    public double getPositionY(){
        return positionY;
    }

    public boolean isEnd(){
        return end;
    }
    
    private void setSprite(double posX, double posY, double width, double height){
        sprite.setX(posX);
        sprite.setY(posY);
        sprite.setWidth(width);
        sprite.setHeight(height);
    }
    
    public void setPosition(double x, double y){
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y){
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y){
        velocityX += x;
        velocityY += y;
    }
    
    public void render(GraphicsContext gc){
        gc.fillRect( width, height, positionX, positionY );
    }
    
    public boolean intersects(Sprite s){
        return s.getBoundary().intersects( this.getBoundary() );
    }
    
    public Rectangle2D getBoundary(){
        return new Rectangle2D(positionX,positionY,width,height);
    }
    
    public void update(double time){
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
}
