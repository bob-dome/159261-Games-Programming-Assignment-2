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

    // Used for checking if the player is on the platform
    boolean on;

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

    public void jump() {
        for(int i = 10; i > 0; i--) {
            fallSpeed = -200;
            posY -= 10;
        }
        fallSpeed = 10;
    }
}