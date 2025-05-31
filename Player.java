public class Player 
{
    // Variables for Player
    double posX;
    double posY;
    int width;
    int height;
    //Gravity/Acceleration factor/jump height between sprite models/individual players?
    //Maybe character select feature?
    int acceleration;
    double speed;
    double fallSpeed;
    boolean valid = false;

    // Used for checking if the player is on the platform
    boolean on;

    // Can the player jump
    boolean jump = true;
    
    // Can the player double jump
    boolean doubleJump = true;

    // Previous Player Pos Y This will be used to prevent Tunneling
    double prevPosY;

    // Direction of the player
    String direction;

    String lastDirection = "left";



    // Constructor
    public Player(double posX, double posY, int width, int height, int acceleration, double speed, double fallSpeed, boolean valid) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.acceleration = acceleration;
        this.fallSpeed = fallSpeed;
        this.valid = valid;
    }

    // Getters
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public double getSpeed() {
        return speed;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean getPlatformStatus()
    {
        return on;
    }

    public boolean jumping()
    {
        return jump;
    }


    public boolean doubleJumping()
    {
        return doubleJump;
    }
    
    public double getPrevPosY()
    {
        return prevPosY;
    }
    
    // Get Player's Direction
    public String getDirection()
    {
        return direction;
    }

    public String getLastDirection() {
        return lastDirection;
    }

    //Setters
    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setFallSpeed(double fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public void onPlatform(boolean on)
    {
        this.on = on;
    }


    public void jump() 
    {
        if (jump && on)
        {
            // Set jumping to false as character is jumping
            jump = false;
            fallSpeed = -10;
            on = false;

            // Debug
            System.out.println("Player Jump");
        } else if (doubleJump && !on) {
            doubleJump = false;
            fallSpeed = -10;

          // Debug
            System.out.println("Player Double Jump");
        }
    }
   
    public void canJump(boolean jump)
    {
        this.jump = jump;
    }

    public void canDoubleJump(boolean doubleJump)
    {
        this.doubleJump = doubleJump;
    }

    public void setPrevPosY(double prevPosY)
    {
        this.prevPosY = prevPosY;
    }

    // Set Direction of the player
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public void setLastDirection(String lastDirection) { this.lastDirection = lastDirection; }
}