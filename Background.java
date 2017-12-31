package shipattack;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.*;
import java.io.*;

/**
 * A scrolling background for the game
 */
public class Background
{
    private BufferedImage image;    //Image used for background to sccroll

    //Instance variables
    private int speed;  //Rate at which the image scrolls pixels/frame
    private int x, y;   //Position on screen for drawing

    /**Constructor for background class*/
    public Background(String url, int speed){
        //Initialize variables
        this.speed = speed; //Initialize scrolling speed

        try{
            this.image =  ImageIO.read(getClass().getResourceAsStream(url));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        this.x = 0;  //Start at origin corner
        this.y = 0 - image.getHeight()/2;  //Starts in mid scroll
    }

    /**For drawing background on screen*/
    public void draw(Graphics2D g2){
        y += speed; //Shifts image origin down

        //Draw background
        g2.drawImage(image, x, y, null);
        g2.drawImage(image, x, y - image.getHeight(), null);

        //When scrolling reaches a certain point, jump back to top to ensure smooth scrolling
        if(y >= image.getHeight()){
            y -= image.getHeight();
        }
    }
    
    /**Getters and Setters*/
    //Speed setter
    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    //Image setter with url
    public void setImage(String url){
        try{
            this.image =  ImageIO.read(new File(url));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
