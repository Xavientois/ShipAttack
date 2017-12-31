package shipattack;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.*;
import java.io.*;

/**
 * Abstract class for all entities on screen
 */
public abstract class Entity
{
    protected BufferedImage sprite;   //Image used to represent the entity    

    //Instance variables
    protected int x, y;   //Position of entity's top left corner on screen
    protected int xspeed, yspeed; //Movement speed of entity on each axis
    protected Ship type; //Type of ship or origin of laser
    protected boolean isLaser;  //For collision checking

    /**Constructor for all entities*/
    public Entity(Ship type){
        //Read image file
        try{          
            sprite = ImageIO.read(getClass().getResourceAsStream(type.url()));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        this.type = type;
        this.isLaser = false;
    }

    /**For drawing sprite on screen*/
    public void draw(Graphics2D g2){
        //Draw sprite
        g2.drawImage(sprite, x, y, null);
    }

    /**Updates game, collisions, movement, etc. before rendering*/
    public void tick(){

    }

    /**Sets sprite of entity*/
    public void setSprite(String url){
        //Read image file
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream(url));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    /**Called when hit by player ship*/
    public void hitPlayer(Player player){
        
    }
    
    /**Called when hit by a laser from the player*/
    public void hit(){
        
    }
    
    /**Kills this entity*/
    public void kill(){
        MainGame.entities.remove(this);
    }
}
