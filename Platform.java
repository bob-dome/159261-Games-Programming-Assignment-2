public class Platform {
    // Class Variables
    double posX;
    double posY;
    double length;
    double fallSpeed;

    // Constructor
    public Platform(double posX, double posY, double length, double fallSpeed) {
        // Set the variables of the platform object to the variables
        this.posX = posX;
        this.posY = posY;
        this.length = length;
        this.fallSpeed = fallSpeed;
    }

    // Getters used for pulling the data from each object
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getLength() {
        return length;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }
}
