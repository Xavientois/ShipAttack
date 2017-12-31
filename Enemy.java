package shipattack;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.*;
import java.io.*;

/**
 * Enemy
 */
public class Enemy extends Entity
{
    //public static final int limit = 10;//Speed limit
    private int limit;  //Limit for oscelation
    private int health; //Keeps track of how many hits player can recieve before losing
    private int shots;  //Counter for delay between laser shots
    private int points; //Points gained when killed
    protected int stopY;  //Y position to stop at when spawned
    protected int stopX;    //X point to shake around

    /**Constructor for Enemy class*/
    public Enemy(Ship type, int x, int stopY){
        //Initialize values
        super(type);
        health = type.health(); //Sets hits enemy can take before dying
        //System.out.println(health);
        shots = 20 + (int) (Math.random() * type.shots());   //Number of lasers to fire
        points = type.points(); //Points based on type
        this.stopY = stopY - 25 + (int) (Math.random() * (MainGame.HEIGHT*3)/5); //Will stop when reaches stopy
        this.stopX = x; //Oscelate around starting point
        limit = 10 + (int)(Math.random()*20);
        //System.out.println(limit);

        //Initialize position
        this.x = x;  //X position
        y = 0 - sprite.getHeight();   //Y position above frame

        //Initialize movement speeds
        xspeed = 5; //No horizontal movement
        yspeed = 0; //No vertical movement
    }

    /**Updates game, collisions, movement, etc. before rendering*/
    public void tick(){        
        //Set speeds
        if(y < stopY || y < 0){
            yspeed = 1 + (int)(Math.random() * 4);
        }else if(y > MainGame.WIDTH){
            stopY = MainGame.HEIGHT;    //Keeps going if it reaches too far
        }else{
            if(type == Ship.ENEMY_4){                
                if(y < MainGame.HEIGHT/2){
                    yspeed = 0; //Mega ship must be killed
                }else{
                    yspeed = -1;
                }
            }else{
                yspeed = 1;
            }
        }

        //Move y
        y += yspeed;

        //Kills if offscreen
        if(y > MainGame.HEIGHT){
            kill();
        }

        //Set speed
        if(type == Ship.ENEMY_4){
            //Move across the screen if enemy 4
            if(xspeed < 0 && x < 5){
                xspeed = 2;
            }else if(xspeed > 0 && x > MainGame.WIDTH - (sprite.getWidth() + 5)){
                xspeed = -2;
            }
        }else if(x > stopX + limit){
            xspeed--;
        }else if(x < stopX - limit){
            xspeed++;
        }

        //Move x
        x += xspeed;

        //Fire laser if timer is up
        if(shots > 0){   //Only fires if delay is up
            shots--;    //Counts down to recharge
        }else if(shots < 1){            
            fire(x + sprite.getWidth()/2, y + yspeed); //Fires laser from position
            shots = 20 + (int) (Math.random() * type.shots()); //Allows shots/30 shot/second
        }
    }

    /**Called when is hit by hostile entity*/
    public void hit(){
        health--;   //Lose health each time ship is hit
        //System.out.println(health);
        if(health < 1){
            kill();
            MainGame.score += this.points;
        }
    }

    /**Called when hits player ship*/
    public void hitPlayer(Player player){
        player.hit();
        hit();
    }

    /**Fires Laser*/
    public void fire(int xFire, int yFire){
        MainGame.entities.add(new Laser(this.type, xFire, yFire));  //Adds fired laser to entity list
    }

    /**Updates enemy count when killer*/
    public void kill(){
        super.kill();
        MainGame.enemies--;
    }
}