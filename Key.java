package shipattack;

/**Represents each key to keyp track of*/
    public class Key{
        public boolean pressed = false; //Determines if key is held down

        //Toggles between pressed states
        public void toggle(boolean isPressed){
            pressed = isPressed;
            //System.out.println("registered");
        }
        
        //Returns state
        public boolean isPressed(){
            return pressed;
        }
    }