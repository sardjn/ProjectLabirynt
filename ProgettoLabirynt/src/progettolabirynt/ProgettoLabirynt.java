/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * sounds from: https://downloads.khinsider.com/game-soundtracks/album/pac-man-game-sound-effects
 */
package progettolabirynt;


import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author rdngrl05a04h501o
 */
public class ProgettoLabirynt extends Application {
    private double multiplier = 4.25;
    private double dimensions[] = getDimensions();
    private double bordersX = 70 * multiplier;
    private double bordersY = 5 * multiplier;
    private double vel = 65*multiplier;
    
    private Canvas canvas;
    private double width = dimensions[0] + bordersX*2;
    private double height = dimensions[1] + bordersY*2;
    
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

        canvas = new Canvas( width, height );
        root.getChildren().add( canvas );
        stage.setResizable(false);
        
        
        // sound loader per gli effetti (suoni)
        SoundPlayer soundPlayer = new SoundPlayer();
        
        
        // ArrayList contenente gli input da tastiera (queue)
        ArrayList<String> input = new ArrayList<String>();
        
        // gestione input
        scene.setOnKeyTyped(new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e){
                    
                    if (e.getCharacter().equals(" ") ) {
                        if ( !input.contains("SPACE")){
                            input.add( "SPACE" );
                        }
                    }
                    
                        
                }
            });
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    if ( !input.contains(code) && !code.equals("SPACE") )
                        input.add( code );
                }
            });

        scene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove( code );
                }
            });
        
        
        //gestione sprite per pacman
        Sprite pacman = new Sprite();
        Image[] pacmanSprites = new Image[3];
        pacmanSprites[0] = new Image("progettolabirynt/images/pacman1.png", 10*multiplier, 10*multiplier, false, false);
        pacmanSprites[1] = new Image("progettolabirynt/images/pacman2.png", 10*multiplier, 10*multiplier, false, false);
        pacmanSprites[2] = new Image("progettolabirynt/images/pacman3.png", 10*multiplier, 10*multiplier, false, false);
        pacman.setMultipleImage(pacmanSprites);
        pacman.setPosition((int)(width/2 - pacman.getWidth()*2), (int)(height/2 + 11*multiplier));
        
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        
        //creazione mappa
        Sprite bg = new Sprite();
        bg.setImage("progettolabirynt/images/bg-blue-empty.png", multiplier);
        bg.setPosition(bordersX, bordersY);
        
        
        // font
        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setFill( Color.GREEN );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);
        
        // background color black
        Sprite background = new Sprite();
        background.setImage("progettolabirynt/images/background.png", multiplier);
        background.setPosition(0, 0);
        
        // start the game with music, then starts the gameplay
        background.render(gc);
        bg.render(gc);
        pacman.setImage(pacmanSprites[1]);
        pacman.render(gc);
        
        
        // colori dei muri della mappa
        Color blue = Color.web("#1300bf");
        Color darkBlue = Color.web("#06003f");
        
        // values and stuff
        LongValue lastNanoTime = new LongValue( System.nanoTime() );
        IntValue score = new IntValue(0);
        
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                boolean isPacmanWeak = true; // when pacman is not able to eat ghosts
                boolean isPacmanAlive = true;
                /**
                 * if true:
                 *   -> ghosts can eat pacman
                 *      ghosts are faster
                 *      ghosts are immune
                 * 
                 *   -> pacman is not immune
                 *      pacman is slower
                 */
                    
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                
                if (input.contains("LEFT")){
                    pacman.setVelocity(-vel, 0);
                }
                    
                if (input.contains("RIGHT")){
                    pacman.setVelocity(vel, 0);
                }
                    
                if (input.contains("UP")){
                     pacman.setVelocity(0,-vel);
                }
                   
                if (input.contains("DOWN")){
                    pacman.setVelocity(0,vel);
                }
                
                background.render(gc);
                bg.render(gc);
                // aggiorna il pacman
                if(isPacmanAlive){
                    pacman.render(gc);
                }
                pacman.update(elapsedTime);
            }
        }.start();
        
        stage.show();
    }
    
    // get dimension for the window
    private double[] getDimensions(){
        Image tmp = new Image("progettolabirynt/images/bg-blue-empty.png");
        double arr[] = new double[2];
        arr[0] = tmp.getWidth() * multiplier;
        arr[1] = tmp.getHeight() * multiplier;
        return arr;
    }
}
