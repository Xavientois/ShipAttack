package shipattack;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Ship controlled by the player
 */
public class Player extends Entity
{
    public static int wait = 4;//Wait time between shots
    
    private int limit;  //Speed limit for the player
    private int health; //Keeps track of how many hits player can recieve before losing
    private int grace;  //Counter for grace period after ship gets hit
    private int laserWait;  //Counter for delay between laser shots

    /**Constructor for Player class*/
    public Player(Ship type){
        //Initialize values
        super(type);
        health = type.health(); //Player gets 3 health points
        grace = 0;  //Grace period starts at 0 frames
        laserWait = 0; //Laser hasn't fired, so no delay

        //Initialize position
        x = MainGame.WIDTH/2 - sprite.getWidth()/2;  //X position at center of screen
        y = MainGame.HEIGHT - sprite.getHeight()*2;   //Y position almost at bottom

        //Initialize movement speeds
        xspeed = 0; //No horizontal movement
        yspeed = 0; //No vertical movement
        limit = 15; //xspeed or yspeed may not surpass the limit
    }
    
    /**Draws ship and lives*/
     public void draw(Graphics2D g2){
        //Draw sprite
        super.draw(g2);
        
        //Draw lives
        //Get sprite
        //Read image file
        BufferedImage heart = null;
        try{
            heart = ImageIO.read(getClass().getResourceAsStream("Sprites/Heart.png"));  //Reads file to get image
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
        //Draw
        for(int i = health; i > 0; i--){
            g2.drawImage(heart, MainGame.WIDTH - ((heart.getWidth() * i) + (10 * i)), 10, null);
        }
    }

    /**Updates game, collisions, movement, etc. before rendering*/
    public void tick(){
        //Determine yspeed
        if(!(MainGame.up.isPressed() && MainGame.down.isPressed())){
            if(MainGame.up.isPressed()){
                yspeed += 2;
            }else if(MainGame.down.isPressed()){
                yspeed -= 2;
            }else if(yspeed > 0){
                yspeed--;
            }else if(yspeed < 0){
                yspeed++;
            }
        }else if(yspeed > 0){
            yspeed--;
        }else if(yspeed < 0){
            yspeed++;
        }
        //Ensure speed limit is respected
        if(yspeed > limit){
            yspeed = limit;
        }else if(yspeed < -limit){
            yspeed = -limit;
        }

        //Move y
        y -= yspeed;

        //Ensure that it fits in screen
        if(y < 0) y = 0;
        if(y > MainGame.HEIGHT - sprite.getHeight()) y = MainGame.HEIGHT - sprite.getHeight();

        //Determine xspeed
        if(!(MainGame.left.isPressed() && MainGame.right.isPressed())){
            if(MainGame.left.isPressed()){
                xspeed += 2;
            }else if(MainGame.right.isPressed()){
                xspeed -= 2;
            }else if(xspeed > 0){
                xspeed--;
            }else if(xspeed < 0){
                xspeed++;
            }
        }else if(xspeed > 0){
            xspeed--;
        }else if(xspeed < 0){
            xspeed++;
        }
        //Ensure speed limit is respected
        if(xspeed > limit){
            xspeed = limit;
        }else if(xspeed < -limit){
            xspeed = -limit;
        }

        //Move x
        x -= xspeed;

        //Ensure that it fits in screen
        if(x < 0) x = 0;
        if(x > MainGame.WIDTH - sprite.getWidth()) x = MainGame.WIDTH - sprite.getWidth();

        //Fire laser if space is pressed
        if(MainGame.space.isPressed()){
            if(laserWait > 0){   //Only fires if delay is up
                laserWait--;    //Counts down to recharge
            }else{
                fire(x + sprite.getWidth()/2, y); //Fires laser from position
                laserWait = wait; //Allows 6 shots/second
            }
        }

        setSprite("Sprites/Player_1.png");   //Resets sprite from grace period
        //If grace period, count down, otherwise, check for collision
        if(grace > 0){
            grace--;    //Count down one frame of grace period
            setSprite("Sprites/Player_1_Red.png");   //Grace period is indicated by red sprite
        }else if(checkCollision() != null){ //Checks for collision with a hostile entity and hits if any
            checkCollision().hitPlayer(this);
        }
        //System.out.println("checked");
    }

    /**Checks for any collision with a dangerous entity*/
    public Entity checkCollision(){
        Rectangle thisHitBox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());  //Creates a hitbox for this entity to check against other entities
        Rectangle otherHitBox;  //Hitbox to check against
        //Cycles through all entities to check for collision
        ListIterator it = MainGame.entities.listIterator();
        while(it.hasNext()){
            Entity entity = (Entity) it.next();
            if(entity.type != Ship.PLAYER){ //Only check non-player ships and lasers
                otherHitBox = new Rectangle(entity.x, entity.y, entity.sprite.getWidth(), entity.sprite.getHeight());   //Creates hitbox of entity to check for collision
                //Checks for rectangle overlap
                if(thisHitBox.x < otherHitBox.x + otherHitBox.width && thisHitBox.x + thisHitBox.width > otherHitBox.x && thisHitBox.y < otherHitBox.y + otherHitBox.height && thisHitBox.y + thisHitBox.height > otherHitBox.y){
                    return entity;
                }
            }
        }
        return null;
    }

    /**Called when is hit by hostile entity*/
    public void hit(){
        health--;   //Lose health each time ship is hit
        //Kills or graces
        if(health > 0){
            grace = 90; //Begin 3 second grace period (30 fps * 3 = 90)
        }else{
            kill();
        }
    }
    
    /**Ends game*/
    public void kill(){
        MainGame.game.stop();
    }

    /**Fires Laser*/
    public void fire(int xFire, int yFire){
        MainGame.entities.add(new Laser(this.type, xFire, yFire));  //Adds fired laser to entity list
        //System.out.println("fired");
    }
}