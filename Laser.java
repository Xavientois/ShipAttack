package shipattack;

import java.awt.Rectangle;

/**
 * Shots fired by ships
 */
public class Laser extends Entity
{

    /**
     * Speed of lasers in pixels per frame
     */
    public static final int laserSpeed = 25;

    /**Constructor for Laser class.
     * @param type Type of ship from enumeration.
     * @param x x position
     * @param y y position
     */
    public Laser(Ship type, int x, int y){
        //Initialize values
        super(type);
        
        isLaser = true; //This is a laser

        //Set sprite of laser and direction of movement
        if(type == Ship.PLAYER){
            setSprite("Sprites/Laser.png");
        }else{
            setSprite("Sprites/Laser_1.png");
        }

        //Initialize position with centering and offset
        this.x = x - sprite.getWidth()/2;   //Centers sprite
        this.y = y;

        //Set movement speed
        xspeed = 0; //Lasers always go straight
        yspeed = laserSpeed;
    }

    /**Updates game, collisions, movement, etc. before rendering*/
    public void tick(){
        //Move y
         if(type == Ship.PLAYER){
            y -= yspeed;
        }else{
            y += yspeed;
        }
        

        if(y > MainGame.HEIGHT){
            kill();
        }

        //Checks for collision with a hostile entity and hits if any
        if(checkCollision() != null && this.type == Ship.PLAYER){ 
            checkCollision().hit();
            kill();
        }
    }

    /**Called when hits player ship.
     * @param player Player instance on which to apply hit*/
    public void hitPlayer(Player player){
        player.hit();
        kill();
    }

    /**Checks for any collision with a dangerous entity
     * @return Entity with which this collided, or null if no collision.*/
    public Entity checkCollision(){
        Rectangle thisHitBox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());  //Creates a hitbox for this entity to check against other entities
        Rectangle otherHitBox;  //Hitbox to check against
        //Cycles through all entities to check for collision
        for(Entity entity : MainGame.entities){
            if(entity.type != Ship.PLAYER){ //Only check player
                otherHitBox = new Rectangle(entity.x, entity.y, entity.sprite.getWidth(), entity.sprite.getHeight());   //Creates hitbox of entity to check for collision
                //Checks for rectangle overlap
                if(thisHitBox.x < otherHitBox.x + otherHitBox.width && thisHitBox.x + thisHitBox.width > otherHitBox.x && y < otherHitBox.y + otherHitBox.height && thisHitBox.y + thisHitBox.height > otherHitBox.y){
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public void hit() {
    }
}
