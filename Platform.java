public class Platform {
    // Class Variables
    double posX;
    double posY;
    double length;
    double width;
    double fallSpeed;

    // Constructor
    public Platform(double posX, double posY, double length, double width, double fallSpeed) {
        // Set the variables of the platform object to the variables
        this.posX = posX;
        this.posY = posY;
        this.length = length;
        this.width = width;
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

    public double getWidth() {
        return width;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    // Setters used for stuff like updating the position of the platform so that it
    // is falling
    public void setPosY(double posY) {
        this.posY = posY;
    }
}
