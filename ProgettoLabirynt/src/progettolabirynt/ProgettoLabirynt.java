/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettolabirynt;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author rdngrl05a04h501o
 */
public class ProgettoLabirynt extends Application {
    private Canvas canvas;
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
            
        /* dichiara canvas   */
        stage.setTitle( "Gioco" );

        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );

        canvas = new Canvas( 800, 500 );
        root.getChildren().add( canvas );
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Rectangle b = drawRect(200, 300);
        root.getChildren().add( b );
        stage.show();
        
        // update stuff
        LongValue lastNanoTime = new LongValue( System.nanoTime() );
        
        Sprite a = new Sprite(150, 50, 0, 0, 100, 100);
        
        a.render(gc);
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;            
                
                a.setVelocity(0, 0);
                
                a.update(elapsedTime);
            }
        };
        
        
    }

    private void drawMap() {
        //disegno
    }
    
    private void drawPlayer() {
        //disegno
    }
    
    private Rectangle drawRect(double x, double y){
        // draw 2 rectangles
        return new Rectangle(x, y, 100, 100);
    }
    
    private void fall(Rectangle a){
        double increase = 10;
        a.setY(a.getY() + increase);
    }
    
    private boolean isColliding(Rectangle a, Rectangle b){
        double rangeX = a.getX() + a.getWidth();
        double rangeY = a.getY() + a.getHeight();
        
        if(b.getX() <= rangeX && b.getX() >= a.getX())
            if(b.getY() <= rangeY && b.getY() >= a.getY())
                return true;
            
        return false;
    }
}
