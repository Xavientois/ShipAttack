package shipattack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * MainGame is the main class to handle the running of the game.
 * All the specs of the frame, the game variables, and enemies are
 * hard coded into this class definition. This includes:
 * 
 * <ul>
 * <li> Frame Specs:
 * <ul>
 * <li> <i>WIDTH</i> - The width of the game frame, 480 by default
 * <li> <i>HEIGHT</i> - The height of the game frame, 4/3 times WIDTH by default
 * <li> <i>NAME</i> - Name of the game frame, "Ship Attack" by default
 * <li> <i>frame</i> - The game frame itself
 * </ul>
 * <li> <i>game</i> - Instance of game itself for calling functions (I know this is poor
 *  design, but I was new to programming at the time) </li>
 * <li> Game Variables:
 * <ul>
 * <li> <i>running</i> - Whether the game is running
 * <li> <i>score</i> - Total player score for this instance of the game
 * <li> <i>wave</i> - Current wave of enemies
 * <li> <i>enemies</i> - Number of enemies currently on screen
 * <li> <i>help</i> - Whether to display the game instructions
 * <li> <i>frameRate</i> - Max frames per second of game, 30 by default
 * <li> <i>instructions</i> - Array containing lines of instructional text
 * </ul>
 * <li> Game Objects:
 * <ul>
 * <li> <i>bgSpace</i> - Scrolling background of game
 * <li> <i>entities</i> - List of all entities in game
 * <li> <i>waves</i> - Possible waves of enemies are hard-coded into here
 * </ul>
 * </li>
 * </ul>
 * This class runs the rendering of the graphics, drawing all entities
 * on screen and also updating the game by the frame rate. It is a JPanel
 * itself and draws onto itself.
 * 
 * @author Joshua Rampersad
 */
public class MainGame extends JPanel implements Runnable, KeyListener{
    //Specs of Frame

    /**The width of the game frame, 480 by default.*/
    public static final int WIDTH = 480;    //Width of JFrame

    /**The height of the game frame, 4/3 times WIDTH by default.*/
    public static final int HEIGHT = WIDTH/3*4; //Width to height ratio

    /** Name of the game frame, "Ship Attack" by default.*/
    public static final String NAME  = "Ship Attack";  //Name of JFrame

    /** Frame in which the game JPanel is held.*/
    public JFrame frame;   //Frame that holds the JPanel
    
    /** The game instance itself.*/
    public static MainGame game;    //Game

    //Game variables

    /** Whether the game is running.*/
    public static boolean running = false; //Is true when game is running, used to start and stop game

    /**Total player score for this instance of the game.*/
    public static int score = 0;    //Player score

    /** Current wave number of enemies.*/
    public static int wave = 0; //Current wave of enemies

    /** Number of enemies currently on screen.*/
    public static int enemies = 0;  //# of enemies onscreen

    /** Whether to display the game instructions.*/
    public static boolean help = true; //Displays instructions

    /** Max frames per second of game, 30 by default*/
    public static final double frameRate = 30;  //Game updates per second

    /** Array containing lines of instructional text.*/
    public static final String[] instructions = {"     How to play:", "-Move with arrow keys", "-Shoot laser with spacebar", "-Press H to view instructions"};    //Game instructions

    //Game Objects

    /** Scrolling background of game.*/
    public Background bgSpace;  //Scrolling background to create the illusion of movement

    /** List of all entities in game.*/
    public static ArrayList<Entity> entities;   //All onscreen entities to update

    /** Possible waves of enemies are hard-coded into here.*/
    public static final Enemy[][][] waves = {
            {    //Easy wave
                {new Enemy(Ship.ENEMY_1, WIDTH/3, HEIGHT/4), new Enemy(Ship.ENEMY_1, WIDTH*2/3, HEIGHT/4)}, //Wave 1
                {new Enemy(Ship.ENEMY_1, WIDTH/4, HEIGHT/4), new Enemy(Ship.ENEMY_2, WIDTH/2, HEIGHT/3), new Enemy(Ship.ENEMY_1, WIDTH*3/4, HEIGHT/4)},  //Wave 2
                {new Enemy(Ship.ENEMY_2, WIDTH/4, HEIGHT/4), new Enemy(Ship.ENEMY_1, WIDTH/2, HEIGHT/3), new Enemy(Ship.ENEMY_2, WIDTH*3/4, HEIGHT/4)},  //Wave 3
                {new Enemy(Ship.ENEMY_2, WIDTH/3, HEIGHT/4), new Enemy(Ship.ENEMY_2, WIDTH/3*2, HEIGHT/4)},  //Wave 4
                {new Enemy(Ship.ENEMY_3, WIDTH/2, HEIGHT/4)}  //Wave 5
            },
            {   //Hard wave
                {new Enemy(Ship.ENEMY_2, WIDTH/4, HEIGHT/5), new Enemy(Ship.ENEMY_2, WIDTH/2, HEIGHT/3), new Enemy(Ship.ENEMY_2, WIDTH*3/4, HEIGHT/5)},  //Wave 1
                {new Enemy(Ship.ENEMY_2, WIDTH/4, HEIGHT/5), new Enemy(Ship.ENEMY_3, WIDTH/2, HEIGHT/3), new Enemy(Ship.ENEMY_2, WIDTH*3/4, HEIGHT/5)},  //Wave 2
                {new Enemy(Ship.ENEMY_1, WIDTH/4, HEIGHT/5), new Enemy(Ship.ENEMY_4, WIDTH/2, -HEIGHT/5), new Enemy(Ship.ENEMY_1, WIDTH*3/4, HEIGHT/5)},  //Wave 3
                {new Enemy(Ship.ENEMY_1, WIDTH/4, HEIGHT/5), new Enemy(Ship.ENEMY_3, WIDTH/2, HEIGHT/3), new Enemy(Ship.ENEMY_2, WIDTH*3/4, HEIGHT/5)},  //Wave 4
                {new Enemy(Ship.ENEMY_4, WIDTH/2, -HEIGHT/4)},  //Wave 5
                {new Enemy(Ship.ENEMY_4, WIDTH/3, -HEIGHT/5), new Enemy(Ship.ENEMY_3, WIDTH/3*2, HEIGHT/4)}  //Wave 6

            }
        }; //All waves of enemies

    /**
     * Initializes all the game objects and frame.
     * Frame has fixed size and starts out centered on screen.
     */
    public MainGame(){
        //Initialize objects
        bgSpace = new Background("Sprites/bg_space.png", 1);
        entities = new ArrayList<Entity>();
        entities.add(new Player(Ship.PLAYER));

        //Set range of dimension possibilities for JPanel for frame packing
        setMinimumSize(new Dimension(WIDTH, HEIGHT));   //Sets minimum possible size when adjusting size
        setMaximumSize(new Dimension(WIDTH, HEIGHT));   //Sets maximum possible size when adjusting size
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); //Sets preferred sizing of JPanel

        //Set up frame to contain JPanel
        frame = new JFrame(NAME);   //Create frame with game as header

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Frame is closeable when X is pressed
        frame.setLayout(new BorderLayout());    //Uses border layout to center JPanel

        frame.add(this, BorderLayout.CENTER);   //Puts JPanel in center of frame
        frame.pack();   //Adjusts sizing

        //Uses frame for inputs
        frame.addKeyListener(this);

        frame.setResizable(false); //User cannot resize frame manually
        frame.setLocationRelativeTo(null);  //Centers frame on the screen
        frame.setVisible(true); //Shows frame
    }

    /**
     * Starts running game.
     * Sets running to true and starts game Thread.
     */
    public synchronized void start(){
        running = true; //Starts running game
        new Thread(this).start();   //Starts a new thread to run game on
    }

    /**Stops running game*/
    public synchronized void stop(){
        running = false;    //Stops running game

        for(int i = entities.size() - 1; i >= 0; i--){
            if(i == entities.size()) i--;   //Bug fix

            entities.remove(i);
        }

        repaint();
    }

    /**
     * Runs game itself.
     * While running game, every thirtieth of a second, ticks game.
     * Keeps track of frame rate as game runs.
     */
    @Override
    public void run(){
        //Updates game constantly while running
        long lastTime = System.nanoTime();  //Previous time tick was checked
        double nsPerTick = 1000000000D/frameRate; //Nanoseconds per tick

        int ticks = 0;  //Updates per second
        int frames = 0; //Frames per second

        long lastTimer = System.currentTimeMillis();  //When previous second has occured
        double elapsed = 0; //Time elapsed since last update

        //Updates game when running
        while(running){
            long now = System.nanoTime();   //Current time
            elapsed += (now - lastTime)/nsPerTick;  //Gets time since last check to determine whether to tick
            lastTime = now; //Current time becomes last time for next check
            boolean render = false; //Determines if program should update screen

            //Updates game if tick time has elapsed
            while(elapsed >= 1){    //If tick time has elapsed perform a game update (a tick)
                ticks++;    //Adds to updates per second counter
                tick();     //Updates game
                elapsed -= 1;   //Lowers the time elapsed counter so that only frameRate ticks are performed per second
                render = true;  //If enough time had elapsed, will render graphics
            }

            //Create a small delay by sleeping so as not to overload the system
            try{
                Thread.sleep(2);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            //If enough time had elapsed, update screen
            if(render){
                frames++;   //Adds to frame counter after updates are performed
                render();   //Updates the screen with changes in game
            }

            //Resets "per second" values every second
            if(System.currentTimeMillis() - lastTimer >= 1000){ //If a second has elapsed
                lastTimer += 1000;  //Adds a second
                //System.out.println(frames + " frames, " + ticks + " ticks");    //For debugging purposes
                frames = 0; //Resets frames per second counter
                ticks = 0;  //Resets ticks per second counter
            }
        }
    }

    /**
     * Draws the game.
     * Draws background, the UI, then the entities.
     */
    public void paintComponent(Graphics g){
        //Create painting object
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        bgSpace.draw(g2); //Draws scrolling background

        if(running){
            //Draws all entity sprites
            for(int i = entities.size() - 1; i >= 0; i--){
                if(i == entities.size()) i--;   //Bug fix

                Entity entity = (Entity) entities.get(i);
                entity.draw(g2);
            }

            //Draw score and any other UI items
            String text = "Score: " + score;
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.PLAIN, 24));
            g2.drawString(text, 20, 30);

            //Draw instructions
            if(help){
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Comic Sans", Font.PLAIN, 24));
                for(int i = 0; i < instructions.length; i++){
                    text = instructions[i];
                    g2.drawString(text ,WIDTH/2 - 150, HEIGHT/2 + (i * 30));
                }
            }
        }else{  //Display score at end
            //Draw score and any other UI items
            String text = "Score: " + score;
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.PLAIN, 32));
            g2.drawString(text, WIDTH/4, HEIGHT/2);
        }
    }

    /**
     * Updates game every tick.
     * Sends in waves and updates the state of all entities.
     */
    public void tick(){
        //Resets lineup
        //lineup = new ArrayList<Entity>();   //For entities waiting to be added so as not to call concurrent mod. except.
        //Checks for help call
        if(wave > 0){
            help = h.isPressed();   //Sets to help if h is pressed
        }

        if(!help && wave > 0){
            //Sends in waves when 0 enemies left
            if (enemies < 1){
                wave(); //Sends in a new wave of enemies
            }

            //Updates all entities
            for(int i = entities.size() - 1; i >= 0; i--){
                if(i == entities.size()) i--;   //Bug fix

                Entity entity = (Entity) entities.get(i);
                entity.tick();
            }
        }

    }

    /**Renders graphics every tick with updated info*/
    public void render(){
        repaint();  //Draws graphics
    }

    /**Sends in a new wave of enemies*/
    public void wave(){
        int level = 0;  //Determines difficulty set of waves to choose from
        if(wave < 15 || score < 5000){
            level = 0;
        }else if(wave < 30 || score < 10000){
            level = (int)(Math.random()*waves.length);
        }else{
            level = waves.length - 1;
        }

        int rWave = (int)(Math.random()*waves[level].length);  //Randomizes waves that appear

        for(int i = 0; i < waves[level][rWave].length; i++){
            Enemy base = waves[level][rWave][i];
            entities.add(new Enemy(base.type, base.x, base.stopY));
            enemies++;
        }

        wave++; //Next wave
    }

    /**Main method for running game
     * @param args Arguments passed when game is run*/
    public static void main(String[] args){
        game = new MainGame();
        game.start();
    }

    /**Handles user input*/
    //Keys to monitor the state of for the game
    public static Key up = new Key();  //Up arrow

    /**
     *
     */
    public static Key down = new Key();    //Down arrow

    /**
     *
     */
    public static Key left = new Key();    //Left arrow

    /**
     *
     */
    public static Key right = new Key();   //Right arrow

    /**
     *
     */
    public static Key space = new Key();   //Space bar

    /**
     *
     */
    public static Key h = new Key();   //H key

    /**Triggered when key is pressed
     * @param e Key Press that triggers method call*/
    public void keyPressed(KeyEvent e){
        toggleKey(e.getKeyCode(), true); //sets whatever key is pressed to the pressed state
    }

    /**Triggered when key is released
     * @param e Key Release that triggers method call*/
    public void keyReleased(KeyEvent e){
        toggleKey(e.getKeyCode(), false); //sets whatever key is pressed to the not-pressed state
    }

    /**Triggered when key is pressed then released
     * @param e Key press and release that triggers method call*/
    public void keyTyped(KeyEvent e){
        /*if(e.getKeyCode() == KeyEvent.VK_H){
        help = true;
        }*/
    }

    /**Toggles key states when triggered
     * @param keyCode Identifier of which key is pressed
     * @param isPressed value to toggle to*/
    public void toggleKey(int keyCode, boolean isPressed){
        //Start game
        if(wave == 0 && keyCode != 17) wave++;

        //Toggles key to correct state
        switch(keyCode){
            case KeyEvent.VK_UP:
            up.toggle(isPressed);
            break;

            case KeyEvent.VK_DOWN:
            down.toggle(isPressed);
            break;

            case KeyEvent.VK_LEFT:
            left.toggle(isPressed);
            break;

            case KeyEvent.VK_RIGHT:
            right.toggle(isPressed);
            break;

            case KeyEvent.VK_SPACE:
            space.toggle(isPressed);
            break;

            case KeyEvent.VK_H:
            h.toggle(isPressed);
            break;
        }

    }
}
