/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * sounds from: https://downloads.khinsider.com/game-soundtracks/album/pac-man-game-sound-effects
 */
package progettolabirynt;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author rdngrl05a04h501o
 */
public class ProgettoLabirynt extends Application {
    
    private boolean NOCLIP = false;
    
    private final double multiplier = 4.25;
    private final double dimensions[] = getDimensions();
    private final double bordersX = 70 * multiplier;
    private final double bordersY = 5 * multiplier;
    private final double speed = 50 * multiplier;
    
    private Canvas canvas;
    private final double width = dimensions[0] + bordersX*2;
    private final double height = dimensions[1] + bordersY*2;
    
    // coordinata (Y) delle due uscite, da cui pacman si teletrasporta
    private final double exitY = 102 * multiplier + bordersY;
    
    
    /*
        int latest[2] -->   [0] = direction
                                    1 = to left
                                    0 = to right
                                        . . .
                            [1] = check (can I teleport again?)
                                    1 = yes
                                    0 = no
                                        . . .
    */
    private int latest[] = {0, 1}; // both 0, as startup
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    
        AtomicBoolean keyWait = new AtomicBoolean(true); // used on key release
        
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
                    keyWait.set(true);
                }
            });
        
        
        //gestione sprite per pacman
        Sprite pacman = new Sprite();
        
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction RIGHT
        Image[] pSprites_RIGHT = new Image[3];
        pSprites_RIGHT[0] = new Image("progettolabirynt/images/pacman1.png", 10*multiplier, 10*multiplier, false, false);
        pSprites_RIGHT[1] = new Image("progettolabirynt/images/pacman2_right.png", 10*multiplier, 10*multiplier, false, false);
        pSprites_RIGHT[2] = new Image("progettolabirynt/images/pacman3_right.png", 10*multiplier, 10*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction LEFT
        Image[] pSprites_LEFT = new Image[3];
        pSprites_LEFT[0] = pSprites_RIGHT[0];
        pSprites_LEFT[1] = new Image("progettolabirynt/images/pacman2_left.png", 10*multiplier, 10*multiplier, false, false);
        pSprites_LEFT[2] = new Image("progettolabirynt/images/pacman3_left.png", 10*multiplier, 10*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction UP
        Image[] pSprites_UP = new Image[3];
        pSprites_UP[0] = pSprites_RIGHT[0];
        pSprites_UP[1] = new Image("progettolabirynt/images/pacman2_up.png", 10*multiplier, 10*multiplier, false, false);
        pSprites_UP[2] = new Image("progettolabirynt/images/pacman3_up.png", 10*multiplier, 10*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction LOW
        Image[] pSprites_LOW = new Image[3];
        pSprites_LOW[0] = pSprites_RIGHT[0];
        pSprites_LOW[1] = new Image("progettolabirynt/images/pacman2_low.png", 10*multiplier, 10*multiplier, false, false);
        pSprites_LOW[2] = new Image("progettolabirynt/images/pacman3_low.png", 10*multiplier, 10*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ set startup sprites
        pacman.setMultipleImage(pSprites_RIGHT);
        pacman.setPosition((int)(width/2 - pacman.getWidth()*2), (int)(height/2 + 11*multiplier));
        
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);                                // change to black !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        gc.fillRect(0, 0, width, height);
        
        //creazione mappa
        Sprite bg = new Sprite();
        bg.setImage("progettolabirynt/images/bg-blue-empty.png", multiplier);
        bg.setPosition(bordersX, bordersY);
        
        Sprite rBorder = new Sprite();
        rBorder.setImage("progettolabirynt/images/bg-borders.png", multiplier);
        rBorder.setPosition(0, 0);
        Sprite lBorder = new Sprite();
        lBorder.setImage("progettolabirynt/images/bg-borders.png", multiplier);
        lBorder.setPosition(width-bordersX, 0);
        
        
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
        //background.render(gc);
        bg.render(gc);
        pacman.setImage(pSprites_RIGHT[1]);
        //pacman.render(gc);
        
        // values and stuff
        LongValue lastNanoTime = new LongValue( System.nanoTime() );
        IntValue score = new IntValue(0);
        
        
        // create all the collision boxes (players and walls)
        createCollisionBoxes(gc);
        
        
        new AnimationTimer()
        {
            
            // when pacman is not able to eat ghosts
            boolean isPacmanWeak = true;
            
            // check if pacman was eaten from the ghosts
            boolean isPacmanAlive = true;
            
            
            @Override
            public void handle(long currentNanoTime)
            {
                if(!isPacmanAlive){
                    System.exit(0);
                }
                    
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                
                if (input.contains("LEFT") && keyWait.get() == true){
                    pacman.setVelocity(-speed, 0);
                    pacman.setMultipleImage(pSprites_LEFT);
                    keyWait.set(false);
                }
                    
                if (input.contains("RIGHT") && keyWait.get() == true){
                    pacman.setVelocity(speed, 0);
                    pacman.setMultipleImage(pSprites_RIGHT);
                    keyWait.set(false);
                }
                    
                if (input.contains("UP") && keyWait.get() == true){
                     pacman.setVelocity(0,-speed);
                     pacman.setMultipleImage(pSprites_UP);
                     keyWait.set(false);
                }
                   
                if (input.contains("DOWN") && keyWait.get() == true){
                    pacman.setVelocity(0,speed);
                    pacman.setMultipleImage(pSprites_LOW);
                    keyWait.set(false);
                }
                
                //background.render(gc);
                //bg.render(gc);
                // aggiorna il pacman
                pacman.render(gc);
                
                int collision = isColliding(pacman);
                int direction = latest[0];
                int wait = latest[1];
                double mult = 3;
                
                
                // check if pacman is in the tunnel
                if(pacman.getPositionY() >= exitY-10*multiplier && pacman.getPositionY() <= exitY+10*multiplier){
                    
                    if(wait == 1){
                        
                        if(pacman.getPositionX()+pacman.getWidth() < bg.getPositionX()){
                            teleport(pacman, collision, bg.getWidth());
                            latest[0] = 0;
                            latest[1] = 0;
                        }
                        
                        else if(pacman.getPositionX() > bg.getPositionX()+bg.getWidth()){
                            teleport(pacman, collision, bg.getWidth());
                            latest[0] = 1;
                            latest[1] = 0;
                        }
                        
                    }
                    
                    else if(wait == 0){
                        
                        //  in order to avoid perpetual teleport, lets add an if block to understand
                        //  if pacman has crossed the map already (using 'latest' array (int))          
                        
                        switch(direction){
                            case 0:
                                if(pacman.getPositionX()+pacman.getWidth()+multiplier < bg.getPositionX()+bg.getWidth()){
                                    latest[1] = 1;
                                }
                                break;
                                
                            case 1:
                                if(pacman.getPositionX()+pacman.getWidth() > bg.getPositionX()+multiplier){
                                    latest[1] = 1;
                                }
                                break;
                                
                            // default used in case 'latest[1]' isn't valid (bug avoiding)
                            default:
                                latest[0] = 0;
                                break;
                        }
                    }
                    
                }else if (collision != -1){
                    riposiziona(pacman, collision);
                }
                
                pacman.update(elapsedTime);
                
                //rBorder.render(gc);
                //lBorder.render(gc);
            }
        }.start();
        
        
        stage.show();
    }
    
    
    private void teleport(Sprite a, int x, double distance){
        
        switch(x){
            case 0:
                a.setPosition(a.getPositionX()-distance-a.getWidth()*1.5, a.getPositionY());
                a.setVelocity(speed, 0);
                break;
            case 1:
                a.setPosition(a.getPositionX()+distance+a.getWidth()*1.5, a.getPositionY());
                a.setVelocity(-speed, 0);
                break;
            default:
                break;
        }
    }
    
    private void riposiziona(Sprite a, int x){
        
        a.setVelocity(0, 0);
        
        switch(x){
            case 0:
                a.setPosition(a.getPositionX()+1, a.getPositionY());
                break;
            case 1:
                a.setPosition(a.getPositionX()-1, a.getPositionY());
                break;
            case 2:
                a.setPosition(a.getPositionX(), a.getPositionY()+1);
                break;
            case 3:
                a.setPosition(a.getPositionX(), a.getPositionY()-1);
                break;
            default:
                break;
        }
    }
    
    
    // check if pacman is colliding
    private boolean isColliding(Rectangle player, Rectangle wall){
        
        Bounds bounds1 = player.getBoundsInLocal();
        Bounds bounds2 = wall.getBoundsInLocal();

        return bounds1.intersects(bounds2);
    }
    
    private int isColliding(Sprite a) {
        
        Rectangle player = new Rectangle(a.getPositionX(), a.getPositionX(), a.getWidth(), a.getHeight());

        for (Rectangle wall: walls) {
            if (isColliding(player, wall)) {
                return isColliding(a, wall);
            }
        }

        return -1;
    }
    
    private int isColliding(Sprite a, Rectangle wall){
        if (a.getPositionX() + a.getWidth() > wall.getX()) {
            // Collision with right wall
            return 0;
        } else if (a.getPositionX() < wall.getX() + wall.getWidth()) {
            // Collision with left wall
            return 1;
        } else if (a.getPositionY() < wall.getY() + wall.getHeight()) {
            // Collision with upper wall
            return 2;
        } else if (a.getPositionY() + a.getHeight() > wall.getY()) {
            // Collision with bottom wall
            return 3;
        }
        return -1;
    }
    
    
    // create all the walls collision boxes (rectangles)
    private Rectangle WALL_UP;
    private Rectangle WALL_LOW;
    private Rectangle WALL_LEFT;
    private Rectangle WALL_RIGHT;
    
    private ArrayList<Rectangle> walls = null;
    
    private void createCollisionBoxes(GraphicsContext g){
        
        double ex, ey;
        double spessore = 6*multiplier; // spessore del muro in pixel
                                                                                    /*      Rectangle rect = new Rectangle(cornerUpLeftX, cornerUpLeftY, width, height)     */
        
        // set the walls' color
        g.setFill(Color.RED);
        
        // create walls' collision boxes
        ex = width-bordersX*2 - 2*multiplier;
        ey = spessore;
        WALL_UP = new Rectangle(bordersX+multiplier, bordersY+multiplier, ex, ey);
        g.fillRect(WALL_UP.getX(), WALL_UP.getY(), ex, ey);                                 // upper wall
        // ---------------------------------------------------------------------------------------------------------
        WALL_LOW = new Rectangle(bordersX+multiplier, height-bordersY-spessore-multiplier, ex, ey);
        g.fillRect(WALL_LOW.getX(), WALL_LOW.getY(), ex, ey);                               // upper wall
        // ---------------------------------------------------------------------------------------------------------
        ex = spessore;
        ey = height-bordersY*2 - 2*multiplier;
        WALL_LEFT = new Rectangle(bordersX+multiplier, bordersY+multiplier, ex, ey);
        g.fillRect(WALL_LEFT.getX(), WALL_LEFT.getY(), ex, ey);                             // left wall
        // ---------------------------------------------------------------------------------------------------------
        WALL_RIGHT = new Rectangle(width-bordersX-spessore-multiplier, bordersY+multiplier, ex, ey);
        g.fillRect(WALL_RIGHT.getX(), WALL_RIGHT.getY(), ex, ey);                           // right wall
        
        
        
        // setup walls ArrayList
        setWallsList();
    }
    
    private void setWallsList(){
        walls = new ArrayList<>();
        walls.add(WALL_UP);
        walls.add(WALL_LOW);
        walls.add(WALL_LEFT);
        walls.add(WALL_RIGHT);
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
