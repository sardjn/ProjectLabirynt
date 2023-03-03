/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettolabirynt;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author HP
 */
public class VectorBox {
    private Rectangle vectorBox = null;
    
    private double x, y, width, height;
    private double record[] = new double[2]; // [0] = previous X pos (Sprite), [1] = previous Y pos (Sprite)
    
    public VectorBox() {}
    public VectorBox(
            double startX,
            double startY,
            double width,
            double height){
        
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        record[0] = startX;
        record[1] = startY;
        
        // create the vector box
        vectorBox = new Rectangle(x, y, this.width, this.height);
    }

    // get data from the record
    public double[] getRecord() {
        return record;
    }

    public void setRecord(double[] record) {
        this.record = record;
    }
    
    

    public Rectangle getVectorBox() {
        return vectorBox;
    }

    public void setVectorBox(Rectangle vectorBox) {
        this.vectorBox = vectorBox;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        vectorBox.setX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        vectorBox.setY(y);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        vectorBox.setWidth(width);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        vectorBox.setHeight(height);
    }
    
    
}
