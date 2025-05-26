public class Player {
    // Variables for Player
    double posX;
    double posY;
    int spriteID;
    //Gravity/Acceleration factor/jump height between sprite models/individual players?
    //Maybe character select feature?
    int acceleration;
    double speed;
    double fallSpeed;
    boolean valid = false;

    // Constructor
    public Player(double posX, double posY, int spriteID, int acceleration, double speed, double fallSpeed, boolean valid) {
        this.posX = posX;
        this.posY = posY;
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

    public void jump() {
        for(int i = 10; i > 0; i--) {
            fallSpeed = -200;
        }
        fallSpeed = 10;
    }
}