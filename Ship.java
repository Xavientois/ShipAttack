package shipattack;

/**
 * For referencing type of ship
 * That entity is - for ships
 * That fired this - for lasers
 */
public enum Ship
{
    //TYPE(health, # of shots, # of points for killing)
    PLAYER(5, -1, -1, "Sprites/Player_1.png"),
    ENEMY_1(1, 90, 100, "Sprites/Enemy_1.png"),
    ENEMY_2(3, 70, 200, "Sprites/Enemy_2.png"),
    ENEMY_3(5, 50, 300, "Sprites/Enemy_4.png"),
    ENEMY_4(20, 40, 500, "Sprites/Enemy_3.png");
    
    private int health, shots, points;
    private String url;
    
    Ship(int health, int shots, int points, String url){
        this.health = health;
        this.shots = shots; //Wait time between shots
        this.points = points;
        this.url = url;
    }
    
    /**Getters*/
    public String url(){
        return url;
    }
    
    public int health(){
        return health;
    }
    
    public int shots(){
        return shots;
    }
    
    public int points(){
        return points;
    }
}