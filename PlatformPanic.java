// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer, <19028995 Ralph Ingley

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class PlatformPanic extends GameEngine {
    // Variable Creation
    int[] Gx,Gy;
    int units;
    String direction = "down";

    // Main Menu & Pause
    private static boolean gamePaused;
    private static boolean menu;
    Player player;
    StartPlatform startPlatform;

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
    int platformAmount = 10;

    // Grid Variables
    int gridColumns;
    int gridWidth;

    //Images
    Image Logo;
    Image Background;
    Image Toucan;



    // Initialize Variables runs only when the program has been run
    @Override
    public void init() 
    {
        // Set Window Size 
        setWindowSize(800, 500);

        // Score & Highscore
        score = 0;
        highscore = loadHighscore();

        //player position

        // Set Gamemodes to false so that the user can press a button for which game
        // they want
        singlePlayerStarted = false;
        multiplayerStarted = false;

        // Create new Platforms Array List
        platforms = new ArrayList<>();

        // Set Grid Variables | Amount of columns and width of each column
        gridColumns = 10;
        gridWidth = mWidth / gridColumns;

        // Audio Loading

        // Game Menu & Pause
        gamePaused = false;
        menu = true;
        
        // Sprite Load

        Logo = loadImage("resources/logo.png");
        Toucan = loadImage("resources/toucan.png");
        Background = loadImage("resources/background.png");

        // Debug
        System.out.println("Platform Panic Initialized");
        System.out.println("Grid Columns: " + gridColumns);
        System.out.println("Grid Width: " + gridWidth);
    }

    // Used to update game logic such as movement
    @Override
    public void update(double dt) {
        if (singlePlayerStarted) {
            platformMovement(dt);
            playermovement();
            playergravity(dt);
            playerPlatformCollision();
        }
    }

    // Used to render background and graphics
    @Override
    public void paintComponent() {

        // Create Single Player background etc
        if (singlePlayerStarted) 
        {
            changeBackgroundColor(Color.BLACK);
            ///////////////////////////////////////////////////////////////////// AIMEE TO DO
            // Change Background to single player background and clear
            // drawRectangle(0, 0, mWidth, mHeight);
            //Coordinates of background image
            // drawImage(Background, 0,0);
            /// /////////////////////////////////////////////////////////////////////

            clearBackground(mWidth, mHeight);

            // Player
            changeColor(Color.green);
            drawRectangle((int) player.getPosX(), (int) player.getPosY(), 30, 30);

            //Start Platform/s spawn
            drawRectangle((int) startPlatform.getPosX(), (int) startPlatform.getPosY(), startPlatform.length, startPlatform.width);
            
            // Draw Platforms using a for loop to go through each platform
            for (Platform platform : platforms) {
                changeColor(red);
                drawSolidRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }
        }

        // Create Multiplayer background etc
        else if (multiplayerStarted) {
            // Change Background to multiplayer background and clear
            changeBackgroundColor(Color.cyan);
            clearBackground(mWidth, mHeight);
        }

        // Menu Background
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
                startSinglePlayer();
            }

            // If the player presses 1 it starts the game in mutliplayer mode
            else if (keyCode == KeyEvent.VK_2) {
                multiplayerStarted = true;
            }
        }
        if(singlePlayerStarted || multiplayerStarted)  {
            if (keyCode == KeyEvent.VK_LEFT ) {
                System.out.println("Left pressed");
                direction = "left";

            }
            if (keyCode == KeyEvent.VK_RIGHT ) {
                System.out.println("Right pressed");
                direction = "right";

            }
            if (keyCode == KeyEvent.VK_UP ) {
                System.out.println("Up pressed");
                direction = "up";
            }
            if (keyCode == KeyEvent.VK_DOWN ) {
                System.out.println("Down pressed");
                direction = "down";
            }
        }
    }

    public void keyReleased(KeyEvent event) {
        if(singlePlayerStarted || multiplayerStarted) {
            direction = "stop";
        }
    }

    public void playermovement() {
        if (direction.equals("left")) {
            player.setPosX(player.getPosX() - player.getSpeed());
        }
        if (direction.equals("right")) {
            player.setPosX(player.getPosX() + player.getSpeed());
        }
        if (direction.equals("up")) {
            player.setPosY(player.getPosY() - player.getSpeed());
            player.jump();
        }
        if (direction.equals("down")) {
            player.setPosY(player.getPosY() + player.getSpeed());
        }

        if (direction.equals("stop")) {
            player.setPosY(player.getPosY());
            player.setPosX(player.getPosX());
        }
    }


    public static void main(String[] args) {
        PlatformPanic platformPanic = new PlatformPanic();
        GameEngine.createGame(platformPanic, 60);
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
        createPlayer();
        // Create Start platform
        startPlatform();
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

        // Occupied Array checks if a boolean can spawn within the grid
        boolean[] occupied = new boolean[gridColumns];

        // For loop to create each platform
        //R: Why a for loop? Could we while this until gameover condition?
        for (int i = 0; i < platformAmount; i++) 
        {
            // Variables used for creating the platforms
            double posX;
            double posY = 0;
            double length = gridWidth - 10;
            double width = 5;
            double fallSpeed;
            int column;

            // Do while loop to make sure platforms spawn in valid columns
            do 
            {
                // Get a random column from the grid
                column = random.nextInt(gridColumns);

            } while (occupied[column] == true);
            occupied[column] = true;

            // Set the posistion of the platform to be within the grid
            posX = gridWidth * column;

            // Random fall speed between 2 and 10
            fallSpeed = 2 + random.nextDouble() * 8;


            // Add the platform to the array list
            platforms.add(new Platform(posX, posY, length, width, fallSpeed));
            //add a variable wait to platform spawns?
        }
    }

    // Platforms Movement
    public void platformMovement(double dt) {
        // For loop to go through each platform and trigger it's movement speed
        for (Platform platform : platforms) {
            // Set the pos y to fall
            platform.setPosY(platform.getPosY() + platform.getFallSpeed() * dt);
        }

        // Remove the platform when it reaches the bottom of the screen height
        platforms.removeIf(platform -> platform.getPosY() > mHeight);

        // Create a new random for randomizing speed and column of the platforms | Same as createPlatforms except whilst the code is running
        Random random = new Random();
        int added = 0;
        boolean[] occupied = new boolean[gridColumns];

        // Check if there are platforms that are too close to the top and make it so platforms cannot spawn there
        for (Platform platform : platforms)
        {
            // Get Platform position on the Y axis | This will check whether we should spawn a new platform in this column or not
            if (platform.getPosY() < mHeight / 2)
            {
                // Get Column of the platform by reversing the creation of the platform in the column
                int column = (int)platform.getPosX() / gridWidth;
                
                // The platform is too close to the top so platforms spawned in this column will overlap with the current platform
                occupied[column] = true;
            }
        }

        // While loop to create 5 new platforms 
        while (added < platformAmount)
        {
            // Randomize the column and check that it's not occupuied
            int column = random.nextInt(gridColumns);

            // If the column is not occupied make a platform to occupy it
            if (!occupied[column])
            {
                occupied[column] = true;

                // Variables used for creating the platforms
                double posX = column * gridWidth;
                double posY = 0;
                double length = gridWidth - 10;
                double width = 5;
                double fallSpeed = 2 + random.nextDouble() * 8;

                // Add the platform to the list
                platforms.add(new Platform(posX, posY, length, width, fallSpeed));

                // Increase added to indicate that a platform has been added
                added++;
            }

            // If there are no valid columns left to prevent an infinite loop break out of the while loop
            boolean invalidSpawn = true;
            for (boolean occupiedColumn : occupied)
            {
                if (!occupiedColumn)
                {
                    invalidSpawn = false;
                }
            }

            // Break out of the loop
            if (invalidSpawn)
            {
                break;
            }
        }
    }

    // Player Platform Collision Detection
    public void playerPlatformCollision()
    {
        // Variable that shows if the player is on a platform or not
        boolean onPlatform = false;

        // Check Play Collision for ALL platforms
        for (Platform platform : platforms)
        {
            // Check if the player is on the platform
            if (player.getPosX() > platform.getPosX() && player.getPosX() < platform.getPosX() + platform.getLength() &&
                player.getPosY() + 30 >= platform.getPosY() && player.getPosY() + 30 <= platform.getPosY() + platform.getWidth()) 
            {
                // Make sure the player doesn't clip through the platform
                player.setPosY(platform.getPosY() - 30);

                // Change fall speed to 0 so player doesn't fall through platform
                player.setFallSpeed(0.0);

                // Set onPlatform to true
                onPlatform = true;
                break;
            }

            // If the player is not on a platform set onPlatform to false
            else
            {
                onPlatform = false;
            }
        }

        // If the player is not actively on a platform set their gravity to value
        if (!onPlatform)
        {
            player.setFallSpeed(50.0);
        }
    }

    //Player creation:
    public void createPlayer() {
        double startX = mWidth / 4;
        double startY = mHeight / 2 ;
        int spriteID = 0;
        int acceleration = 1;
        double speed = 5.0;
        double fallSpeed = 10.0;
        boolean valid = true;

        player = new Player(startX, startY, spriteID, acceleration, speed, fallSpeed, valid);
    }

    // Player Movement
    public void playergravity(double dt) {
            player.setPosY(player.getPosY() + player.getFallSpeed() * dt);
    }

    //Start Platform
    public void startPlatform(){
        double posX = mWidth / 2;
        double posY = mHeight / 2;
        double length = 50;
        double width = 10;
        double fallSpeed = 0.0;

        startPlatform = new StartPlatform(posX,posY,length,width,fallSpeed);
    }
    // Reset the game
    public void resetGame() {

    }



}
