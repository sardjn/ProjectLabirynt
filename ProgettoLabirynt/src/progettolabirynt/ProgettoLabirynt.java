/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectlabirynt;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author rdngrl05a04h501o
 */
public class ProgettoLabirynt extends Application {
    private Canvas canvas;
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
        
        /* dichiara canvas   */
        stage.setTitle( "Gioco" );

        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );

        canvas = new Canvas( 800, 500 );
        root.getChildren().add( canvas );
     
        drawMap();
        drawPlayer();
        
        Rectangle a = drawRect(150, 50);
        Rectangle b = drawRect(200, 300);
        
        root.getChildren().add( a );
        root.getChildren().add( b );
        
        System.out.println(isColliding(a, b));
        
        while(isColliding(a, b) == false){
            fall(a);
            stage.show();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
