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
    private final double FPS = 10;
    private final double ANIMATION_DURATION = 0.5;
    
    private final double multiplier = 4.25;
    private final double dimensions[] = getDimensions();
    private final double bordersX = 70 * multiplier;
    private final double bordersY = 5 * multiplier;
    private final double speed = 30 * multiplier;
    
    private Canvas canvas;
    private final double width = dimensions[0] + bordersX*2;
    private final double height = dimensions[1] + bordersY*2;
    
    // coordinata (Y) delle due uscite, da cui pacman si teletrasporta
    private final double exitX = bordersX;
    private final double exitY = 102 * multiplier + bordersY;
    private final double vBoxDim = speed*1.5;
    private int vboxDir;
    
    
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
        double pixSize = 9.5;
        
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction RIGHT
        Image[] pSprites_RIGHT = new Image[3];
        pSprites_RIGHT[0] = new Image("progettolabirynt/images/pacman1.png", pixSize*multiplier, pixSize*multiplier, false, false);
        pSprites_RIGHT[1] = new Image("progettolabirynt/images/pacman2_right.png", pixSize*multiplier, pixSize*multiplier, false, false);
        pSprites_RIGHT[2] = new Image("progettolabirynt/images/pacman3_right.png", pixSize*multiplier, pixSize*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction LEFT
        Image[] pSprites_LEFT = new Image[3];
        pSprites_LEFT[0] = pSprites_RIGHT[0];
        pSprites_LEFT[1] = new Image("progettolabirynt/images/pacman2_left.png", pixSize*multiplier, pixSize*multiplier, false, false);
        pSprites_LEFT[2] = new Image("progettolabirynt/images/pacman3_left.png", pixSize*multiplier, pixSize*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction UP
        Image[] pSprites_UP = new Image[3];
        pSprites_UP[0] = pSprites_RIGHT[0];
        pSprites_UP[1] = new Image("progettolabirynt/images/pacman2_up.png", pixSize*multiplier, pixSize*multiplier, false, false);
        pSprites_UP[2] = new Image("progettolabirynt/images/pacman3_up.png", pixSize*multiplier, pixSize*multiplier, false, false);
        // ------------------------------------------------------------------------------------------------------------------------------------ sprites for direction LOW
        Image[] pSprites_LOW = new Image[3];
        pSprites_LOW[0] = pSprites_RIGHT[0];
        pSprites_LOW[1] = new Image("progettolabirynt/images/pacman2_low.png", pixSize*multiplier, pixSize*multiplier, false, false);
        pSprites_LOW[2] = new Image("progettolabirynt/images/pacman3_low.png", pixSize*multiplier, pixSize*multiplier, false, false);
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
        LongValue lastNanoTime = new LongValue(System.nanoTime());
        
        
        // create all the collision boxes (players and walls)
        createCollisionBoxes(gc);
        VectorBox vectBox = new VectorBox(pacman.getPositionX(), pacman.getPositionY(), pacman.getWidth(), pacman.getHeight());

        
        new AnimationTimer()
        {
            
            // when pacman is not able to eat ghosts
            boolean isPacmanWeak = true;
            
            // check if pacman was eaten from the ghosts
            boolean isPacmanAlive = true;
            
            
            @Override
            public void handle(long currentNanoTime)
            {
                // Firstly, update the VBox
                updateVBox(vectBox, pacman);
                
                if(!isPacmanAlive){
                    System.exit(0);
                }
                    
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                
                if (input.contains("LEFT")){
                    if(keyWait.get() == true){
                        pacman.setVelocity(-speed, 0);
                        pacman.setMultipleImage(pSprites_LEFT);
                        keyWait.set(false);
                    }
                    vboxDir = 0;
                }
                    
                if (input.contains("RIGHT")){
                    if(keyWait.get() == true){
                        pacman.setVelocity(speed, 0);
                        pacman.setMultipleImage(pSprites_RIGHT);
                        keyWait.set(false);
                    }
                    vboxDir = 1;
                }
                    
                if (input.contains("UP")){
                    if(keyWait.get() == true){
                        pacman.setVelocity(0,-speed);
                        pacman.setMultipleImage(pSprites_UP);
                        keyWait.set(false);
                    }
                    vboxDir = 2;
                }
                   
                if (input.contains("DOWN")){
                    if(keyWait.get() == true){
                        pacman.setVelocity(0,speed);
                        pacman.setMultipleImage(pSprites_LOW);
                        keyWait.set(false);
                    }
                    vboxDir = 3;
                }
                
                
                switch(vboxDir){
                    case 0:
                        vectBox.setPosition(pacman.getPositionX()-pacman.getWidth(), pacman.getPositionY());
                        break;
                    case 1:
                        vectBox.setPosition(pacman.getPositionX()+pacman.getWidth(), pacman.getPositionY());
                        break;
                    case 2:
                        vectBox.setPosition(pacman.getPositionX(), pacman.getPositionY()-pacman.getHeight());
                        break;
                    case 3:
                        vectBox.setPosition(pacman.getPositionX(), pacman.getPositionY()+pacman.getHeight());
                        break;
                    default:
                        break;
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
                if(pacman.getPositionX() < exitX && pacman.getPositionX() > exitX+bg.getWidth()){
                    
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
    
    private void updateVBox(VectorBox vb, Sprite a){
        double tmp[] = {a.getPositionX(), a.getPositionY()};
        vb.setRecord(tmp);
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
                a.setPosition(a.getPositionX()-1, a.getPositionY());
                break; // case: right
            case 1:
                a.setPosition(a.getPositionX()+1, a.getPositionY());
                break; // case: left
            case 2:
                a.setPosition(a.getPositionX(), a.getPositionY()+1);
                break; // case: up
            case 3:
                a.setPosition(a.getPositionX(), a.getPositionY()-1);
                break; // case: bottom
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
        Rectangle player = new Rectangle(a.getPositionX(), a.getPositionY(), a.getWidth(), a.getHeight());
        
        for (Rectangle wall: walls) {
            if (isColliding(player, wall)) {
                return whereCollision(player, wall);
            }
        }

        return -1;
    }
    
    private int whereCollision(Rectangle a, Rectangle wall){
        
        double playerPos[] = {a.getX(), a.getY(), a.getX()+a.getWidth(), a.getY()+a.getHeight()}; // startX, startY, endX, endY
        double wallPos[] = {wall.getX(), wall.getY(), wall.getX()+wall.getWidth(), wall.getY()+wall.getHeight()}; // startX, startY, endX, endY
        
        // check on which side the player is colliding
        if(playerPos[2] > wallPos[0] && vboxDir == 1){
            return 0;
        }else if(playerPos[0] < wallPos[2] && vboxDir == 0){
            return 1;
        }else if(playerPos[1] < wallPos[3] && vboxDir == 2){
            return 2;
        }else if(playerPos[3] > wallPos[1] && vboxDir == 3){
            return 3;
        }
        
        // r, l, u, b
        return -1;
    }
    
    
    // create all the walls collision boxes (rectangles)
    private Rectangle WALL_UP;
    private Rectangle WALL_LOW;
    private Rectangle WALL_LEFT_UP;
    private Rectangle WALL_RIGHT_UP;
    private Rectangle WALL_LEFT_DOWN;
    private Rectangle WALL_RIGHT_DOWN;
    
    private Rectangle WALL_1;
    private Rectangle WALL_2;
    private Rectangle WALL_3;
    private Rectangle WALL_4;
    private Rectangle WALL_5;
    private Rectangle WALL_6;
    private Rectangle WALL_7;
    private Rectangle WALL_8;
    private Rectangle WALL_9;
    private Rectangle WALL_10;
    private Rectangle WALL_11;
    private Rectangle WALL_12;
    private Rectangle WALL_13;
    private Rectangle WALL_14;
    private Rectangle WALL_15;
    private Rectangle WALL_16;
    private Rectangle WALL_17;
    private Rectangle WALL_18;
    private Rectangle WALL_19;
    private Rectangle WALL_20;
    private Rectangle WALL_21;
    private Rectangle WALL_22;
    private Rectangle WALL_23;
    private Rectangle WALL_24;
    private Rectangle WALL_25;
    private Rectangle WALL_26;
    private Rectangle WALL_27;
    private Rectangle WALL_28;
    private Rectangle WALL_29;

    
    private ArrayList<Rectangle> walls = null;
    
    private void createCollisionBoxes(GraphicsContext g){
        
        WallsData data = new WallsData(multiplier);
        double wd[][] = data.getDataFromMatrix(); // get the walls width and height
        
        double pathWidth = 10 * multiplier;
        double ex, ey;
        double spessore = 6*multiplier; // spessore del muro in pixel
                                                                                    /*      Rectangle rect = new Rectangle(cornerUpLeftX, cornerUpLeftY, width, height)     */
        
        // set the walls' color
        g.setFill(Color.RED);
        
        // create walls' collision boxes
        ex = width-bordersX*2 - 2*multiplier;
        ey = spessore;
        WALL_UP = new Rectangle(
                bordersX+multiplier, 
                bordersY+multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_UP.getX(), WALL_UP.getY(), ex, ey);                                 // upper wall
        // ---------------------------------------------------------------------------------------------------------
        WALL_LOW = new Rectangle(
                bordersX+multiplier, 
                height-bordersY-spessore-multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_LOW.getX(), WALL_LOW.getY(), ex, ey);                               // upper wall
        // ---------------------------------------------------------------------------------------------------------
        ex = spessore;
        ey = 77*multiplier;
        WALL_LEFT_UP = new Rectangle(
                bordersX + multiplier, 
                bordersY + multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_LEFT_UP.getX(), WALL_LEFT_UP.getY(), ex, ey);                       // left wall up
        // ---------------------------------------------------------------------------------------------------------
        WALL_RIGHT_UP = new Rectangle(
                width - bordersX - spessore - multiplier, 
                bordersY + multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_RIGHT_UP.getX(), WALL_RIGHT_UP.getY(), ex, ey);                     // right wall up
        // ---------------------------------------------------------------------------------------------------------
        WALL_LEFT_DOWN = new Rectangle(
                bordersX + multiplier, 
                142*multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_LEFT_DOWN.getX(), WALL_LEFT_DOWN.getY(), ex, ey);                   // left wall down
        // ---------------------------------------------------------------------------------------------------------
        WALL_RIGHT_DOWN = new Rectangle(
                width - bordersX - spessore - multiplier, 
                142*multiplier, 
                ex, 
                ey
                );
        g.fillRect(WALL_RIGHT_DOWN.getX(), WALL_RIGHT_DOWN.getY(), ex, ey);                 // right wall down
        
        
        
        // --------------------------------------------------------------------------------------------------------- inner walls (using inner walls data)
        
        double offsetX = bordersX + multiplier;
        double offsetY = bordersY + multiplier;
        double sideX = WALL_LEFT_UP.getWidth();
        double sideY = WALL_UP.getHeight();
        
        WALL_1 = new Rectangle(
            offsetX + pathWidth + sideX, 
            offsetY + pathWidth + sideY, 
            wd[0][0], 
            wd[0][1]
            );
        g.fillRect(WALL_1.getX(), WALL_1.getY(), WALL_1.getWidth(), WALL_1.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_2 = new Rectangle(
            offsetX + pathWidth*2 + sideX + WALL_1.getWidth(), 
            offsetY + pathWidth + sideY, 
            wd[0][0], 
            wd[0][1]
            );
        g.fillRect(WALL_2.getX(), WALL_2.getY(), WALL_2.getWidth(), WALL_2.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_3 = new Rectangle(
            offsetX + pathWidth*3 + sideX + WALL_1.getWidth()*2, 
            offsetY + sideY, 
            wd[5][0], 
            wd[5][1]
            );
        g.fillRect(WALL_3.getX(), WALL_3.getY(), WALL_3.getWidth(), WALL_3.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_4 = new Rectangle(
            offsetX + pathWidth*4 + sideX + WALL_1.getWidth()*2 + WALL_3.getWidth(), 
            offsetY + pathWidth + sideY,
            wd[0][0], 
            wd[0][1]
            );
        g.fillRect(WALL_4.getX(), WALL_4.getY(), WALL_4.getWidth(), WALL_4.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_5 = new Rectangle(
            offsetX + pathWidth*5 + sideX + WALL_1.getWidth()*3 + WALL_3.getWidth(), 
            offsetY + pathWidth + sideY, 
            wd[0][0], 
            wd[0][1]
            );
        g.fillRect(WALL_5.getX(), WALL_5.getY(), WALL_5.getWidth(), WALL_5.getHeight());
        
        
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        
        
        WALL_6 = new Rectangle(
            offsetX + pathWidth + sideX, 
            offsetY + pathWidth*2 + sideY + WALL_1.getHeight(), 
            wd[1][0],
            wd[1][1]
            );
        g.fillRect(WALL_6.getX(), WALL_6.getY(), WALL_6.getWidth(), WALL_6.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_7 = new Rectangle(
            offsetX + pathWidth*2 + sideX + WALL_6.getWidth(),
            offsetY + pathWidth*2 + sideY + WALL_1.getHeight(), 
            wd[6][0], 
            wd[6][1]
            );
        g.fillRect(WALL_7.getX(), WALL_7.getY(), WALL_7.getWidth(), WALL_7.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_8 = new Rectangle(
            offsetX + pathWidth*3 + sideX + WALL_6.getWidth() + WALL_7.getWidth(), 
            offsetY + pathWidth*2 + sideY + WALL_1.getHeight(), 
            wd[2][0], 
            wd[2][1]
            );
        g.fillRect(WALL_8.getX(), WALL_8.getY(), WALL_8.getWidth(), WALL_8.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_9 = new Rectangle(
            offsetX + pathWidth*4 + sideX + WALL_6.getWidth() + WALL_7.getWidth() + WALL_8.getWidth(), 
            offsetY + pathWidth*2 + sideY + WALL_1.getHeight(), 
            wd[6][0], 
            wd[6][1]
            );
        g.fillRect(WALL_9.getX(), WALL_9.getY(), WALL_9.getWidth(), WALL_9.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_10 = new Rectangle(
            offsetX + pathWidth*5 + sideX + WALL_1.getWidth()*3 + WALL_3.getWidth(), 
            offsetY + pathWidth*2 + sideY + WALL_1.getHeight(), 
            wd[1][0], 
            wd[1][1]
            );
        g.fillRect(WALL_10.getX(), WALL_10.getY(), WALL_10.getWidth(), WALL_10.getHeight());
        
        
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        
        WALL_11 = new Rectangle(
            offsetX, 
            offsetY + pathWidth*3 + sideY + WALL_1.getHeight() + WALL_6.getHeight(), 
            wd[4][0],
            wd[4][1]
            );
        g.fillRect(WALL_11.getX(), WALL_11.getY(), WALL_11.getWidth(), WALL_11.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_12 = new Rectangle(
            offsetX + WALL_11.getWidth() + pathWidth + WALL_7.getWidth(), 
            offsetY + sideY + pathWidth*3 + WALL_2.getHeight() + WALL_8.getHeight(), 
            wd[7][0], 
            wd[7][1]
            );
        g.fillRect(WALL_12.getX(), WALL_12.getY(), WALL_12.getWidth(), WALL_12.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_13 = new Rectangle(
            offsetX + pathWidth*2 + WALL_11.getWidth() + WALL_7.getWidth() + WALL_12.getWidth(), 
            offsetY + sideY + pathWidth + WALL_3.getHeight() + WALL_8.getHeight(), 
            wd[3][0], 
            wd[3][1]
            );
        g.fillRect(WALL_13.getX(), WALL_13.getY(), WALL_13.getWidth(), WALL_13.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_14 = new Rectangle(
            offsetX + pathWidth*3 + WALL_11.getWidth() + WALL_7.getWidth() + WALL_12.getWidth() + WALL_13.getWidth() + 2*multiplier, 
            offsetY + sideY + pathWidth*3 + WALL_2.getHeight() + WALL_8.getHeight(), 
            wd[7][0], 
            wd[7][1]
            );
        g.fillRect(WALL_14.getX(), WALL_14.getY(), WALL_14.getWidth(), WALL_14.getHeight());
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        WALL_15 = new Rectangle(
            offsetX + pathWidth*5 + sideX + WALL_1.getWidth()*3 + WALL_3.getWidth(), 
            offsetY + pathWidth*3 + sideY + WALL_1.getHeight() + WALL_6.getHeight(), 
            wd[4][0], 
            wd[4][1]
            );
        g.fillRect(WALL_15.getX(), WALL_15.getY(), WALL_15.getWidth(), WALL_15.getHeight());
        
        
        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        
        
        //new walls
        
        
        
        
        // setup walls ArrayList
        setWallsList();
    }
    
    private void setWallsList(){
        walls = new ArrayList<>();
        walls.add(WALL_UP);
        walls.add(WALL_LOW);
        walls.add(WALL_LEFT_UP);
        walls.add(WALL_RIGHT_UP);
        walls.add(WALL_LEFT_DOWN);
        walls.add(WALL_RIGHT_DOWN);
        walls.add(WALL_1);
        walls.add(WALL_2);
        walls.add(WALL_3);
        walls.add(WALL_4);
        walls.add(WALL_5);
        walls.add(WALL_6);
        walls.add(WALL_7);
        walls.add(WALL_8);
        walls.add(WALL_9);
        walls.add(WALL_10);
        walls.add(WALL_11);
        walls.add(WALL_12);
        walls.add(WALL_13);
        walls.add(WALL_14);
        walls.add(WALL_15);
        /*walls.add(WALL_16);
        walls.add(WALL_17);
        walls.add(WALL_18);
        walls.add(WALL_19);
        walls.add(WALL_20);
        walls.add(WALL_21);
        walls.add(WALL_22);
        walls.add(WALL_23);
        walls.add(WALL_24);
        walls.add(WALL_25);
        walls.add(WALL_26);
        walls.add(WALL_27);
        walls.add(WALL_28);
        walls.add(WALL_29);*/
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
