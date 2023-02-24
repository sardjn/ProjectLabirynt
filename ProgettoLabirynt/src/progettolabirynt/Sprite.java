package progettolabirynt;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite
{
    private Image image;
    //array che prevede più immagini sprite per simulare il movimento
    private Image[] multipleImage;
    private boolean movimentoImage=false;
    private int numImmagine;
    private boolean termina=false;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    public Sprite()
    {
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public boolean isTermina() {
        return termina;
    }
    
    

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setMultipleImage(Image[] multipleImage) {
        this.multipleImage = multipleImage;
        movimentoImage=true;
        numImmagine=0;
        image=multipleImage[0];
    }
    
    

    public void setImage(String filename, double multiplierResize)
    {
        Image tmp = new Image(filename);
        double w = tmp.getWidth();
        double h = tmp.getHeight();
        Image i = new Image(filename, w*multiplierResize, h*multiplierResize, false, false);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
        if(movimentoImage){
            numImmagine++;
            if(numImmagine>=multipleImage.length){
                numImmagine=0;
                termina=true;
            }
            image=multipleImage[numImmagine];
        }
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
    
    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]" 
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}