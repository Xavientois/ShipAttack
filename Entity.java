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

    /**
     * Image used to represent the entity.
     */
    protected BufferedImage sprite;     

    //Instance variables

    /**
     * Position of entity's top left corner on screen.
     */
    protected int x, y;

    /**
     * Movement speed of entity on each axis.
     */
    protected int xspeed, yspeed;

    /**
     * Type of ship or origin of laser
     */
    protected Ship type;

    /**
     * For collision checking
     */
    protected boolean isLaser;

    /**Constructor for all entities.
     * @param type Type of entity for initializing values*/
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

    /**For drawing sprite on screen.
     * @param g2 Graphics interface with which to draw sprites.*/
    public void draw(Graphics2D g2){
        //Draw sprite
        g2.drawImage(sprite, x, y, null);
    }

    /**Updates game, collisions, movement, etc. before rendering*/
    public void tick(){

    }

    /**Sets sprite of entity
     * @param url Location of sprite PNG file.*/
    public void setSprite(String url){
        //Read image file
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream(url));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    /**Called when hit by player ship
     * @param player Player entity to hit.*/
    public abstract void hitPlayer(Player player);
    
    /**Called when hit by a laser from the player.*/
    public abstract void hit();
    
    /**Kills this entity*/
    public void kill(){
        MainGame.entities.remove(this);
    }
}
