package shipattack;

/**
 * For referencing type of ship
 * That entity is - for ships
 * That fired this - for lasers
 */
public enum Ship
{
    //TYPE(health, # of shots, # of points for killing)

    /**
     * Player ship type.
     */
    PLAYER(5, -1, -1, "Sprites/Player_1.png"),

    /**
     * Green Enemy
     */
    ENEMY_1(1, 90, 100, "Sprites/Enemy_1.png"),

    /**
     * Purple Enemy
     */
    ENEMY_2(3, 70, 200, "Sprites/Enemy_2.png"),

    /**
     * Red Enemy
     */
    ENEMY_3(5, 50, 300, "Sprites/Enemy_4.png"),

    /**
     * Yellow Big Enemy
     */
    ENEMY_4(20, 40, 500, "Sprites/Enemy_3.png");
    
    private int health, shots, points;
    private String url;
    
    Ship(int health, int shots, int points, String url){
        this.health = health;
        this.shots = shots; //Wait time between shots
        this.points = points;
        this.url = url;
    }
    
    /**Getter for sprite location
     * @return location of sprite PNG file*/
    public String url(){
        return url;
    }
    
    /**
     * Getter for health of ship type
     * @return health of ship
     */
    public int health(){
        return health;
    }
    
    /**
     * Wait time between shots
     * @return wait time of shots
     */
    public int shots(){
        return shots;
    }
    
    /**
     * Points for killing
     * @return number of points
     */
    public int points(){
        return points;
    }
}