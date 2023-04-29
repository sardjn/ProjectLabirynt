/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettolabirynt;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 *
 * @author HP
 */
public class SoundPlayer {
    public MediaPlayer mediaPlayer = null;
    
    private Media chomp = null;
    private Media death = null;
    private Media fruit = null;
    private Media ghost = null;
    private Media intro = null;
    

    public SoundPlayer(){
        chomp = new Media(getClass().getResource("sounds/chomp.wav").toString());
        death = new Media(getClass().getResource("sounds/death.wav").toString());
        fruit = new Media(getClass().getResource("sounds/fruit.wav").toString());
        ghost = new Media(getClass().getResource("sounds/ghost.wav").toString());
        intro = new Media(getClass().getResource("sounds/intro.wav").toString());
    }
    
    public void chomp(){
        mediaPlayer = new MediaPlayer(chomp);
        mediaPlayer.play();
    }
    
    public void death(){
        mediaPlayer = new MediaPlayer(death);
        mediaPlayer.play();
    }
    
    public void fruit(){
        mediaPlayer = new MediaPlayer(fruit);
        mediaPlayer.play();
    }
    
    public void ghost(){
        mediaPlayer = new MediaPlayer(ghost);
        mediaPlayer.play();
    }
    
    public void intro(){
        mediaPlayer = new MediaPlayer(intro);
        mediaPlayer.play();
    }
}
