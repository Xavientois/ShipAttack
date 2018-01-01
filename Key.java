package shipattack;

/**Represents each key to keyp track of*/
    public class Key{

    /**
     * Current "pressed" state of this key.
     * True if key is currently pressed
     */
    public boolean pressed = false; //Determines if key is held down

        //Toggles between pressed states

    /**
     * Toggles pressed variable to true or false.
     * @param isPressed Value to which to toggle.
     */
        public void toggle(boolean isPressed){
            pressed = isPressed;
            //System.out.println("registered");
        }
        
        //Returns state

    /**
     * Getter for pressed value.
     * @return Whether key is pressed.
     */
        public boolean isPressed(){
            return pressed;
        }
    }