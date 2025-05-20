// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer, <ID> Ralph <Last name>

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class PlatformPanic extends GameEngine {
    // Variable Creation

    // Main Menu & Pause
    private static boolean gamePaused;
    private static boolean menu;

    // Score & Highscore
    private static int score;
    private static int highscore;

    // Single Player Variables
    private static boolean singlePlayerStarted;

    // Multiplayer Variables
    private static boolean multiplayerStarted;

    // Platforms Array List holds every platform so it can be cleared and reused on
    // game end
    ArrayList<Platform> platforms;

    // Initialize Variables runs only when the program has been run
    @Override
    public void init() {
        setWindowSize(750, 500);

        // Score & Highscore
        score = 0;
        highscore = loadHighscore();

        // Set Gamemodes to false so that the user can press a button for which game
        // they want
        singlePlayerStarted = false;
        multiplayerStarted = false;

        // Create new Platforms Array List
        platforms = new ArrayList<>();

        // Audio Loading

        // Game Menu & Pause
        gamePaused = false;
        menu = true;
    }

    // Used to update game logic such as movement
    @Override
    public void update(double dt) {
        while (singlePlayerStarted) {
            platformMovement(dt);
        }
    }

    // Used to render background and graphics
    @Override
    public void paintComponent() {
        // Create Single Player background etc
        if (singlePlayerStarted) {
            // Change Background to single player background and clear
            changeBackgroundColor(blue);
            clearBackground(mWidth, mHeight);

            // Draw Platforms using a for loop to go through each platform
            for (Platform platform : platforms) {
                changeColor(red);
                drawRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }
        }

        // Create Multiplayer background etc
        else if (multiplayerStarted) {
            // Change Background to multiplayer background and clear
            changeBackgroundColor(Color.cyan);
            clearBackground(mWidth, mHeight);
        }

        else if (menu) {
            // Change Background to single player background and clear
            changeBackgroundColor(Color.black);
            clearBackground(mWidth, mHeight);

            // Change Text Colour
            changeColor(white);

            // Draw Title (can be changed to img logo if added later)
            drawText(height() / 2 - 250, width() / 2 - 100, "Platform Panic", "Arial", 40);

            // Draw Single Player Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(height() / 2 - 250, width() / 2, "     Single Player", "Arial", 30);

            // Draw Multiplayer Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(height() / 2 - 250, width() / 2 + 50, "       Multiplayer", "Arial", 30);
        }
    }

    // Load the Single Player Highscore (there is no highscore for multiplayer as it
    // is a versus game while single player is not)
    private int loadHighscore() {
        return 0;
    }

    // Movement and Menu Management
    public void keyPressed(KeyEvent keyEvent) {
        // Get Key Pressed
        int keyCode = keyEvent.getKeyCode();

        // If the game is on the menu the user can press keys to play a certain game
        if (menu) {
            // If the player presses 1 it starts the game in single player mode
            if (keyCode == KeyEvent.VK_1) {
                menu = false;
                singlePlayerStarted = true;
            }

            // If the player presses 1 it starts the game in mutliplayer mode
            else if (keyCode == KeyEvent.VK_2) {
                menu = false;
                multiplayerStarted = true;
            }
        }
    }

    public static void main(String[] args) {
        PlatformPanic platformPanic = new PlatformPanic();
        GameEngine.createGame(platformPanic, 10);
    }

    // Start Single Player Gamemode
    public void startSinglePlayer() {
        // Set Variables
        menu = false;
        singlePlayerStarted = true;

        // Debug
        System.out.println("Single Player Started");

        // Create the Platforms
        createPlatforms();

        // Create Player

    }

    // Start Multiplayer Gamemode
    public void startMultiplayer() {
        // Set Variables for Multiplayer
        menu = false;
        multiplayerStarted = true;

        // Debug
        System.out.println("Multiplayer Started");
    }

    // Create Platform Objects
    public void createPlatforms() {
        // Create Random to randomize different aspects of the platforms
        Random random = new Random();

        // Variable that defines how many platforms can exist at any given point
        int platformAmount = 10;

        // For loop to create each platform
        for (int i = 0; i < platformAmount; i++) {
            // Variables used for creating the platforms
            double posX;
            double posY = 0;
            double length;
            double width = 5;
            double fallSpeed;
            boolean valid = false;

            // Check that the platforms have valid details
            while (!valid) {
                // Randomize the X axis position on where the platform spawns
                posX = random.nextDouble() * mWidth;
                length = random.nextDouble() * 20;
                fallSpeed = random.nextDouble() * 10;

                if (posX > 0 && posX < mWidth && length > 5 && length < 30 && fallSpeed > 1 && fallSpeed < 8) {
                    valid = true;

                    // Add the platform to the array list
                    platforms.add(new Platform(posX, posY, length, width, fallSpeed));
                }
            }
        }
    }

    // Platforms Movement
    public void platformMovement(double dt) {
        // For loop to go through each platform and trigger it's movement speed
        for (Platform platform : platforms) {
            // Set the pos y to fall
            platform.setPosY(platform.getPosY() + platform.getFallSpeed() * dt);
        }
    }

    // Create Player
    public void createPlayer() {

    }

    // Reset the game
    public void resetGame() {

    }
}
