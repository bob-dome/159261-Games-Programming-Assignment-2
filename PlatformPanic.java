// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer, 19028995 Ralph Ingley

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class PlatformPanic extends GameEngine 
{
    // Variable Creation

    // Player Movement
    Player player;
    String direction = "stop";

    // Main Menu & Pause
    private static boolean gamePaused;
    private static boolean menu;
    private static boolean gameOver;
    
    // Score & Highscore
    private static int score;
    private static int highscore;
    boolean startScore;
    
    // Single Player Variables
    private static boolean singlePlayerStarted;
    
    // Multiplayer Variables
    private static boolean multiplayerStarted;
    
    // Platforms Array List holds every platform so it can be cleared and reused on game end | Start Platform is where the player spawns
    Platform startPlatform;
    ArrayList<Platform> platforms;
    int platformAmount = 10;
    boolean playerOnStart = true;

    // Grid Variables
    int gridColumns;
    int gridWidth;

    //Images
    Image Logo;
    Image Background;
    Image Toucan;
    Image ToucanFlipped;
    Image ToucanJump;
    Image ToucanWalk;
    Image ToucanWalkFlipped;
    Image Parrot;
    Image ParrotJump;
    Image ParrotWalk;
    Image sprite;


    // Audio
    AudioClip menuMusic;
    boolean menuMusicPlaying;


    // Initialize Variables runs only when the program has been run
    @Override
    public void init() 
    {
        // Set Window Size 
        setWindowSize(800, 500);

        // Score & Highscore
        startScore = false;
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
        menuMusic = loadAudio("resources/MenuMusic.wav");
        menuMusicPlaying = false;

        // Game Menu & Pause
        gamePaused = false;
        menu = true;
        gameOver = false;
        
        // Sprite Load
        Logo = loadImage("resources/logo.png");
        Toucan = loadImage("resources/toucan.png");
        ToucanJump = loadImage("resources/toucan_jump.png");
        ToucanWalk = loadImage("resources/toucan_walk.png");
        Parrot = loadImage("resources/parrot.png");
        ParrotJump = loadImage("resources/parrot_jump.png");
        ParrotWalk = loadImage("resources/parrot_walk.png");
        Background = loadImage("resources/background.png");
        ToucanFlipped = loadImage("resources/toucan_flipped.png");
        ToucanWalkFlipped = loadImage("resources/toucan_walk_flipped.png");

        // Debug
        System.out.println("Platform Panic Initialized");
        System.out.println("Grid Columns: " + gridColumns);
        System.out.println("Grid Width: " + gridWidth);
    }

    // Used to update game logic such as movement
    @Override
    public void update(double dt) 
    {
        if (singlePlayerStarted) 
        {
            if (!gamePaused) 
            {
                playermovement();
                playergravity(dt);
                playerPlatformCollision();
                platformMovement(dt);
                checkbounds();
                loadScore();
                gameover();
            }
        } 
        else if (gamePaused) 
        {

        }
        
    }

    // Used to render background and graphics
    @Override
    public void paintComponent() 
    {
        // Create Single Player background etc
        if (singlePlayerStarted) 
        {
            //Background
            clearBackground(mWidth, mHeight);
            drawImage(Background, 0, 0, mWidth, mHeight);

            
            // Stop Menu Music
            stopAudioLoop(menuMusic);

            //Walking animations
            if (!player.jump){
                drawImage(ToucanJump, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            }
            else if (direction == "left") {
                drawImage(ToucanWalk, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            } else if (direction == "right"){
                drawImage(ToucanWalkFlipped, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            }
            else{
                drawImage(Toucan, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            }

            //Start Platform/s spawn
            if (playerOnStart)
            {
                drawSolidRectangle((int) startPlatform.getPosX(), (int) startPlatform.getPosY(), startPlatform.length, startPlatform.width);
            }
            
            // Draw Platforms using a for loop to go through each platform
            for (Platform platform : platforms) 
            {
                changeColor(red);
                drawSolidRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }

            // Display Score
            changeColor(white);
            drawText(mWidth / mWidth + 10, mHeight / mHeight + 50, "" + score, "Arial", 40);
        }
        
        // Create Multiplayer background etc
        else if (multiplayerStarted) 
        {
            
            clearBackground(mWidth, mHeight);
            //Background
            drawImage(Background, 0, 0, 800, 500);
            
            clearBackground(mWidth, mHeight);
        }
        
        // Menu Background
        else if (menu) 
        {
            if (!menuMusicPlaying)
            {
                // Play Menu Music
                playAudio(menuMusic);
                startAudioLoop(menuMusic, 1);
                menuMusicPlaying = true;
            }
            
            clearBackground(mWidth, mHeight);
            //Background
            drawImage(Background, 0, 0, 800, 500);
            
            //Logo
            drawImage(Logo, 175, -50, 472, 328);
            
            // Change Text Colour
            changeColor(Color.BLACK);
            
            // Draw Single Player Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(200, 300, "     Single Player", "Arial", 50);
            
            // Draw Multiplayer Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(200, 400, "       Multiplayer", "Arial", 50);
        }
        //else if (resetGame()){
            //}
            
            if (gameOver){
                changeBackgroundColor(Color.BLACK);
                changeColor(Color.RED);
                drawText(200, 200, "GAME OVER!", "Arial", 50);
            }
            
            if (gamePaused) 
            {
            changeColor(Color.BLACK);
            drawText(mWidth / 2 - 100, mHeight / 2 - 50, "Game Paused", "Arial", 50);
            drawText(mWidth / 2 - 150, mHeight / 2 + 50, "Press ESC to Resume", "Arial", 30);
        }
    }
    
    // Scoring System when player is no longer using the starting platform start incrementing score
    private void loadScore()
    {
        // Check that the starting platform is gone
        if (!playerOnStart)
        {
            score += 1;
        }
    }

    // Call this funtion in the game over function 
    // Save the Single Player Highscore to a txt file
    private void saveHighscore() 
    {
        // Change txt file path to proper path
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hightscore.txt"))) 
        {
            writer.write(String.valueOf(highscore));
        }
        // Catch any exceptions that may occur when writing to the file
        catch (Exception e) {
            System.out.println("Error saving highscore");
        }
        
    }
    // Load highscore function
    private int loadHighscore() {
        // Change txt file path to proper path
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String highScore = reader.readLine();

            // Check if there is a number in the highscore file
            if (highScore != null) {
                return Integer.parseInt(highScore);
            }
        }
        // Catch any exceptions that may occur when reading the file
        catch (Exception e) {
            // If there is an error loading the highscore, return 0
            System.out.println("Error loading highscore");
            return 0;

        }
        return highscore;
    }

    // Movement and Menu Management
    public void keyPressed(KeyEvent keyEvent) 
    {
        // Get Key Pressed
        int keyCode = keyEvent.getKeyCode();

        // If the game is on the menu the user can press keys to play a certain game
        if (menu) 
        {
            // If the player presses 1 it starts the game in single player mode
            if (keyCode == KeyEvent.VK_1) 
            {
                startSinglePlayer();
            }

            // If the player presses 1 it starts the game in mutliplayer mode
            else if (keyCode == KeyEvent.VK_2) 
            {
                multiplayerStarted = true;
            }
        }
        // When the player presses the escape key it pauses the game
        if (keyCode == KeyEvent.VK_ESCAPE) 
        {
            if (!gamePaused) 
            {
                // If the game is not paused then pause it
                System.out.println("Game Paused");
                gamePaused = true;
            } 
            else 
            {
                // If the game is paused then unpause it
                System.out.println("Game Resumed");
                gamePaused = false;
            }
        }
        

        // Player movement for when the game has started
        if(singlePlayerStarted || multiplayerStarted)  
        {
            if (keyCode == KeyEvent.VK_LEFT ) 
            {
                System.out.println("Left pressed");
                direction = "left";

            }

            if (keyCode == KeyEvent.VK_RIGHT ) 
            {
                System.out.println("Right pressed");
                direction = "right";

            }

            if (keyCode == KeyEvent.VK_UP ) {
                System.out.println("Up pressed");
                // Lets the player jump and double jump
                player.jump();
                drawImage(ToucanJump, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());

            }
            if (keyCode == KeyEvent.VK_DOWN ) {
                System.out.println("Down pressed");
                direction = "down";
            }

        }
        if (gameOver){
            if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                resetGame();
                startSinglePlayer();
                gameOver = false;
            }
        }
    }

    public void keyReleased(KeyEvent event) 
    {
        int keyCode = event.getKeyCode();

        if(singlePlayerStarted || multiplayerStarted) 
        {
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
            {
                direction = "stop";
            }
        }
    }

    public void playermovement() 
    {
        if (direction.equals("left")) 
        {
            player.setPosX(player.getPosX() - player.getSpeed());
        }

        if (direction.equals("right")) 
        {
            player.setPosX(player.getPosX() + player.getSpeed());
        }

        if (direction.equals("stop")) 
        {
            player.setPosY(player.getPosY());
            player.setPosX(player.getPosX());
        }
    }


    public static void main(String[] args) 
    {
        PlatformPanic platformPanic = new PlatformPanic();
        GameEngine.createGame(platformPanic, 60);
    }

    // Start Single Player Gamemode
    public void startSinglePlayer() 
    {
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
    public void startMultiplayer() 
    {
        // Set Variables for Multiplayer
        menu = false;
        multiplayerStarted = true;

        // Debug
        System.out.println("Multiplayer Started");
    }

    // Create Platform Objects
    public void createPlatforms() 
    {
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
    public void platformMovement(double dt) 
    {
        // For loop to go through each platform and trigger it's movement speed
        for (Platform platform : platforms) 
        {
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
        // Check if the player is on the starting platform
        if (playerOnStart)
        {
            // Variables that show the top of the starting platform, and the players previous position and current position
            double platformTop = startPlatform.getPosY();
            double playerYPrev = player.getPrevPosY() + player.getHeight();
            double playerYNow = player.getPosY() + player.getHeight();

            // Check if player crosses the starting platform because the fall speed was too large and made them go through
            boolean crossedPlatform = 
                (playerYPrev <= platformTop) &&
                (playerYNow >= platformTop) &&
                (player.getPosX() + player.getWidth() > startPlatform.getPosX()) &&
                (player.getPosX() < startPlatform.getPosX() + startPlatform.getLength()) &&
                (player.getFallSpeed() > 0);

            // If the player crossed the starting platform then make sure they land on it
            if (crossedPlatform)
            {
                // Make sure the player doesn't clip through the platform
                player.setPosY(startPlatform.getPosY() - player.getHeight());
    
                // Change fall speed to 0 so player doesn't fall through platform
                player.setFallSpeed(0.0);
                
                // Set onPlatform to true
                player.onPlatform(true);
    
                // Set player's ability to jump to true
                player.canJump(true);
    
                // Set player's ability to double jump to true
                player.canDoubleJump(true);

                // Return since the player is still on the starting platform
                return;
            }
        }

        // If the player is not on the statting platform set onPlatform to false
        // Check Play Collision for ALL platforms
        for (Platform platform : platforms)
        {
            // Variables that show the top of the platform, and the players previous position and current position
            double platformTop = platform.getPosY();
            double playerYPrev = player.getPrevPosY() + player.getHeight();
            double playerYNow = player.getPosY() + player.getHeight();

            // Check if player cross the platform because the fall speed was too large and made them go through
            boolean crossedPlatform = 
                (playerYPrev <= platformTop) &&
                (playerYNow >= platformTop) &&
                (player.getPosX() + player.getWidth() > platform.getPosX()) &&
                (player.getPosX() < platform.getPosX() + platform.getLength()) &&
                (player.getFallSpeed() > 0);

            // Check if the player is on the platform / crossed the platform due to too much speed
            if (crossedPlatform)
            {
                // Make sure the player doesn't clip through the platform
                player.setPosY(platform.getPosY() - player.getHeight());
                    
                // Change fall speed to 0 so player doesn't fall through platform
                player.setFallSpeed(0.0);
                    
                // Set onPlatform to true
                player.onPlatform(true);
    
                // Set player's ability to jump to true
                player.canJump(true);

                // Set player's ability to double jump to true
                player.canDoubleJump(true);

                // Player is no longer on the starting platform as this platform was touched
                playerOnStart = false;
                break;
            }
                
            // If the player is not on a platform set onPlatform to false
            else
            {
                player.onPlatform(false);
            }        
        }
    }

    //Player creation:
    public void createPlayer() 
    {
        double startX = mWidth / 2;
        double startY = mHeight / 2 - 60;
        int spriteID = 0;
        int acceleration = 1;
        double speed = 5.0;
        double fallSpeed = 10.0;
        boolean valid = true;

        player = new Player(startX, startY, 50, 50, spriteID, acceleration, speed, fallSpeed, valid);
    }

    // Player Movement
    public void playergravity(double dt) 
    {
        // Set Gravity
        double gravity = 1;

        // Get Previous Position
        player.setPrevPosY(player.getPosY());

        // Set Fallspeed of Player
        player.setFallSpeed(player.getFallSpeed() + gravity);

        // Set Player's Fall
        player.setPosY(player.getPosY() + player.getFallSpeed());
    }

    //Start Platform
    public void startPlatform()
    {
        double posX = mWidth / 2;
        double posY = mHeight / 2;
        double length = 50;
        double width = 10;
        double fallSpeed = 0.0;

        startPlatform = new Platform(posX,posY,length,width,fallSpeed);
    }
    //Gameover (Works, needs paint / resets on key?)
    public void gameover()
    {
        //System.out.println(player.posY);
        if (player.posY >= mHeight)
        {
            // Game Over
            System.out.println("Gameover");
            singlePlayerStarted = false;
            gameOver = true;

            // Save Highscore if the highscore is greater than the score
            if (score > highscore)
            {
                // Since the score goes up in such fast succession there is a difference of 1 on recorded score
                highscore = score - 1;
                saveHighscore();
            }
        }
    }

    //CheckBounds (used to keep player onscreen)
    public void checkbounds()
    {
        if (player.getPosX() < 0)
        {
            player.setPosX(0);
        }

        else if (player.getPosX() > mWidth - 5)
        {
            player.setPosX(mWidth - 5);
        }
    }

    //Rest platforms
    public void resetGame()
    {
        // Reset Score to 0
        score = 0;

        // Clear the platform array
        platforms.clear();

        // Set gameover to false
        gameOver = false;

        // Set Player to be on Start
        playerOnStart = true;
    }
}
