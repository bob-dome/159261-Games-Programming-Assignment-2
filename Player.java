public class Player {
    // Variables for Player
    double posX;
    double posY;
    int width;
    int height;
    int spriteID;
    //Gravity/Acceleration factor/jump height between sprite models/individual players?
    //Maybe character select feature?
    int acceleration;
    double speed;
    double fallSpeed;
    boolean valid = false;

    // Player Velocity
    double velocityY = 0;

    // Used for checking if the player is on the platform
    boolean on;

    // Can the player jump
    boolean jump = false;

    // Constructor
    public Player(double posX, double posY, int width, int height, int spriteID, int acceleration, double speed, double fallSpeed, boolean valid) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.spriteID = spriteID;
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

    public int getSpriteID() {
        return spriteID;
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
            velocityY = -20;
            on = false;

            // Debug
            System.out.println("Player Jump");
        }
    }

    public void canJump(boolean jump)
    {
        this.jump = jump;
    }
}