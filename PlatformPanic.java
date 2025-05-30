// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer, 19028995 Ralph Ingley

import java.awt.*;
import java.awt.RenderingHints.Key;
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

    // Multiplayer players list
    ArrayList<Player> players;

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
        // Single Player
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

        // Multiplayer
        if (multiplayerStarted)
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
            drawImage(Background, 0, 0, mWidth, mHeight);

            if (playerOnStart && players != null && players.size() >= 2) 
            {
                // Draw Start Platforms for both players
                drawSolidRectangle((int) startPlatform.getPosX(), (int) startPlatform.getPosY(), startPlatform.getLength(), startPlatform.getWidth());
                drawSolidRectangle((int) (startPlatform.getPosX() + mWidth / 2), (int) startPlatform.getPosY(), startPlatform.getLength(), startPlatform.getWidth());
            }

            for (Platform platform : platforms) 
            {
                changeColor(red);
                drawSolidRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }

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
        
        // Only allow player movement when the game is not paused
        if (!gamePaused)
        {
            // Player movement for when the game has started
            if(singlePlayerStarted)  
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
    
                if (keyCode == KeyEvent.VK_UP ) 
                {
                    System.out.println("Up pressed");

                    // Lets the player jump and double jump
                    player.jump();

                    drawImage(ToucanJump, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
                }

                // Multiplayer Player Movement
                if (multiplayerStarted) 
                {
                    if (keyCode == KeyEvent.VK_W) {
                        // Player 2 jump
                        players.get(0).setDirection("up");
                        System.out.println("Player 2 Jump");
                    }
                    if (keyCode == KeyEvent.VK_UP) {
                        // Player 1 jump
                        players.get(1).setDirection("up");
                        System.out.println("Player 1 Jump");
                    }
                    
                }
            }
        }

        // To reset the game back to single player hit enter or space
        if (gameOver)
        {
            if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) 
            {
                resetGame();
                startSinglePlayer();
                gameOver = false;
            }
        }
    }

    // Key Released for Player Movement
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

    // Player Movement Function
    public void playermovement() 
    {
        // Player Movement for Single Player
        if (singlePlayerStarted) {

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
        // Player Movement for Multiplayer
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Player Movement for Multiplayer
            Player player1 = players.get(0);
            Player player2 = players.get(1);

            if (player1.getDirection().equals("left")) 
            {
                player1.setPosX(player1.getPosX() - player1.getSpeed());
            }

            if (player1.getDirection().equals("right")) 
            {
                player1.setPosX(player1.getPosX() + player1.getSpeed());
            }

            if (player1.getDirection().equals("stop")) 
            {
                player1.setPosY(player1.getPosY());
                player1.setPosX(player1.getPosX());
            }

            if (player2.getDirection().equals("left")) 
            {
                player2.setPosX(player2.getPosX() - player2.getSpeed());
            }

            if (player2.getDirection().equals("right")) 
            {
                player2.setPosX(player2.getPosX() + player2.getSpeed());
            }

            if (player2.getDirection().equals("stop")) 
            {
                player2.setPosY(player2.getPosY());
                player2.setPosX(player2.getPosX());
                }
                    
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

        // Create the Platforms
        createPlatforms();

        // Create Players
        createPlayer();

        // Create Start platform
        startPlatform();




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
        if (singlePlayerStarted) 
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
        // Not sure if this is the best way to do this
        if (multiplayerStarted) {
            // Ensure players list is initialized and has players
            if (players != null && !players.isEmpty()) {
                // Check if the players are on the starting platform
                for (Player player : players) 
                {
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
            }
        }
    }

    // Player creation:
    public void createPlayer() 
    {
        // Create 1 player in the middle if it's a single player game
        if (singlePlayerStarted)
        {
            double startX = mWidth / 2;
            double startY = mHeight / 2 - 60;
            int acceleration = 1;
            double speed = 5.0;
            double fallSpeed = 10.0;
            boolean valid = true;
    
            player = new Player(startX, startY, 50, 50, acceleration, speed, fallSpeed, valid);
        }

        // Create 2 players one on the left one on the right if it's a multiplayer game
        if (multiplayerStarted)
        {
            // Player array list to hold the players
            players = new ArrayList<>();
            double startX1 = mWidth / 4;
            double startY1 = mHeight / 2 - 60;
            double startX2 = mWidth * 3 / 4;
            double startY2 = mHeight / 2 - 60;
            int acceleration = 1;
            double speed = 5.0;
            double fallSpeed = 10.0;
            boolean valid = true;

            // Create Player 1
            Player player1 = new Player(startX1, startY1, 50, 50, acceleration, speed, fallSpeed, valid);
            // Create Player 2
            Player player2 = new Player(startX2, startY2, 50, 50, acceleration, speed, fallSpeed, valid);

            // Add the players to the players array list
            players.add(player1);
            players.add(player2);

        }
    }

    // Player Movement
    public void playergravity(double dt) 
    {
        if (singlePlayerStarted) {
        // Set Gravity
        double gravity = 1;

        // Get Previous Position
        player.setPrevPosY(player.getPosY());

        // Set Fallspeed of Player
        player.setFallSpeed(player.getFallSpeed() + gravity);

        // Set Player's Fall
        player.setPosY(player.getPosY() + player.getFallSpeed());
        }
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Set Gravity for Player 1
            double gravity = 1;
            Player player1 = players.get(0);
            player1.setPrevPosY(player1.getPosY());
            player1.setFallSpeed(player1.getFallSpeed() + gravity);
            player1.setPosY(player1.getPosY() + player1.getFallSpeed());

            // Set Gravity for Player 2
            Player player2 = players.get(1);
            player2.setPrevPosY(player2.getPosY());
            player2.setFallSpeed(player2.getFallSpeed() + gravity);
            player2.setPosY(player2.getPosY() + player2.getFallSpeed());
        }
    }

    //Start Platform
    public void startPlatform()
    {
        if (singlePlayerStarted) {
        double posX = mWidth / 2;
        double posY = mHeight / 2;
        double length = 50;
        double width = 10;
        double fallSpeed = 0.0;

        startPlatform = new Platform(posX,posY,length,width,fallSpeed);
        }

        if (multiplayerStarted) {
            // Create two starting platforms for multiplayer
            double posX1 = mWidth / 4;
            double posY1 = mHeight / 2;
            double posX2 = mWidth * 3 / 4;
            double posY2 = mHeight / 2;
            double length1 = 50;
            double length2 = 50;
            double width1 = 10;
            double width2 = 10;
            double fallSpeed1 = 0.0;
            double fallSpeed2 = 0.0;

            // Create Player 1's Start Platform
            Platform startPlatform1 = new Platform(posX1, posY1, length1, width1, fallSpeed1);
            // Create Player 2's Start Platform
            Platform startPlatform2 = new Platform(posX2, posY2, length2, width2, fallSpeed2);
            // Add the start platforms to the platforms array list
            platforms.add(startPlatform1);
            platforms.add(startPlatform2);


        
    }

    }
    //Gameover (Works, needs paint / resets on key?)
    public void gameover()
    {
        // Single Player Game Over
        if (singlePlayerStarted && player != null)
        {
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
        // Multiplayer Game Over
        if (multiplayerStarted && players != null && !players.isEmpty())
        {
            boolean anyPlayerOut = false;
            for (Player p : players) {
                if (p != null && p.getPosY() >= mHeight) {
                    anyPlayerOut = true;
                    break;
                }
            }
            if (anyPlayerOut) {
                multiplayerStarted = false;
                gameOver = true;
            }
        }
    }

    //CheckBounds (used to keep player onscreen)
    public void checkbounds()
    {
        if (singlePlayerStarted) {
        if (player.getPosX() < 0)
        {
            player.setPosX(0);
        }

        else if (player.getPosX() > mWidth - 5)
        {
            player.setPosX(mWidth - 5);
        }
    }
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Check Player 1 Bounds
            Player player1 = players.get(0);
            if (player1.getPosX() < 0) 
            {
                player1.setPosX(0);
            } 
            else if (player1.getPosX() > mWidth - 5) 
            {
                player1.setPosX(mWidth - 5);
            }

            // Check Player 2 Bounds
            Player player2 = players.get(1);
            if (player2.getPosX() < 0) 
            {
                player2.setPosX(0);
            } 
            else if (player2.getPosX() > mWidth - 5) 
            {
                player2.setPosX(mWidth - 5);
            }
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
