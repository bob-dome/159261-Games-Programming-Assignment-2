// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer, 19028995 Ralph Ingley

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlatformPanic extends GameEngine {
    // Variable Creation

    // Player Movement
    Player player;
    String direction = "stop";

    // Main Menu & Pause
    private boolean gamePaused;
    private boolean menu;
    private boolean gameOver;
    private boolean instructions1;
    private boolean instructions2;
    private boolean victory;
    private boolean resetscores;
    // Score & Highscore
    private int score;
    private int highscore;
    boolean startScore;

    int winP1;
    int winP2;

    // Single Player Variables
    private boolean singlePlayerStarted;

    // Multiplayer Variables
    private boolean multiplayerStarted;

    // Platforms Array List holds every platform so it can be cleared and reused on
    // game end | Start Platform is where the player spawns
    Platform startPlatform;
    ArrayList<Platform> platforms;
    int platformAmount = 10;
    boolean playerOnStart = true;

    // Multiplayer Platforms
    Platform startPlatform1;
    Platform startPlatform2;
    boolean player1OnStart = true;
    boolean player2OnStart = true;

    // Multiplayer players list
    ArrayList<Player> players;

    // Grid Variables
    int gridColumns;
    int gridWidth;

    // Images
    Image Logo;
    Image Background;
    Image Toucan;
    Image ToucanFlipped;
    Image ToucanJump;
    Image ToucanWalk;
    Image ToucanWalkFlipped;
    Image Parrot;
    Image ParrotFlipped;
    Image ParrotJump;
    Image ParrotWalk;
    Image ParrotWalkFlipped;

    // To find users last direction
    String lastDirection;

    // Audio
    AudioClip menuMusic;
    boolean menuMusicPlaying;

    // Initialize Variables runs only when the program has been run
    @Override
    public void init() {
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
        ParrotFlipped = loadImage("resources/parrot_flipped.png");
        ParrotWalkFlipped = loadImage("resources/parrot_flipped.png");

        // String to find the users last direction
        lastDirection = "left";

        // Debug
        System.out.println("Platform Panic Initialized");
        System.out.println("Grid Columns: " + gridColumns);
        System.out.println("Grid Width: " + gridWidth);
    }

    // Used to update game logic such as movement
    @Override
    public void update(double dt) {
        // Single Player
        if (singlePlayerStarted) {
            if (!gamePaused) {
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
        if (multiplayerStarted) {
            if (!gamePaused) {
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
    public void paintComponent() {
        // Create Single Player background etc
        if (singlePlayerStarted) {
            // Background
            clearBackground(mWidth, mHeight);
            drawImage(Background, 0, 0, mWidth, mHeight);

            // Player animations (jumping, left, right, idle)
            if (!player.jump) {
                drawImage(ToucanJump, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            } else if (direction.equals("left")) {
                lastDirection = "left";
                drawImage(ToucanWalk, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            } else if (direction.equals("right")) {
                lastDirection = "right";
                drawImage(ToucanWalkFlipped, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            } else {
                // If the player last moved a certain direction, the idle player will be facing
                // that direction.
                if (lastDirection.equals("left")) {
                    drawImage(Toucan, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
                } else {
                    drawImage(ToucanFlipped, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
                }
            }

            // Start Platform/s spawn
            if (playerOnStart) {
                drawSolidRectangle((int) startPlatform.getPosX(), (int) startPlatform.getPosY(), startPlatform.length,
                        startPlatform.width);
            }

            // Draw Platforms using a for loop to go through each platform
            for (Platform platform : platforms) {
                changeColor(red);
                drawSolidRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }

            // Display Score
            changeColor(white);
            drawText(mWidth / mWidth + 10, mHeight / mHeight + 50, "Score:" + score, "Arial", 20);

            // Display the Highscore
            drawText(mWidth / mWidth + 10, mHeight / mHeight + 75, "HighScore: " + highscore, "Arial", 20);
        }

        // Create Multiplayer background etc
        else if (multiplayerStarted) {

            clearBackground(mWidth, mHeight);
            drawImage(Background, 0, 0, mWidth, mHeight);

            // Start Platform for Player 1 spawn
            if (player1OnStart) {
                drawSolidRectangle((int) startPlatform1.getPosX(), (int) startPlatform1.getPosY(),
                        startPlatform1.length, startPlatform1.width);
            }

            // Start Platform for Player 2 spawn
            if (player2OnStart) {
                drawSolidRectangle((int) startPlatform2.getPosX(), (int) startPlatform2.getPosY(),
                        startPlatform2.length, startPlatform2.width);
            }

            // Draw Players
            for (Player player : players) {
                // Player animations (jumping, left, right, idle)
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                if (player == player1) {
                    if (!player1.jump) {
                        drawImage(ToucanJump, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                                player1.getHeight());
                    } else if (player1.direction.equals("left")) {
                        player1.setLastDirection("left");
                        drawImage(ToucanWalk, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                                player1.getHeight());
                    } else if (player1.direction.equals("right")) {
                        player1.setLastDirection("right");
                        drawImage(ToucanWalkFlipped, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                                player1.getHeight());
                    } else {
                        // If the player last moved a certain direction, the idle player will be facing
                        // that direction.
                        if (player1.getLastDirection().equals("left")) {
                            drawImage(Toucan, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                                    player1.getHeight());
                        } else if (player1.getLastDirection().equals("right")) {
                            drawImage(ToucanFlipped, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                                    player1.getHeight());
                        }
                    }
                }
                if (player == player2) {
                    if (!player2.jump) {
                        drawImage(ParrotJump, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                                player2.getHeight());
                    } else if (player2.direction.equals("left")) {
                        player2.setLastDirection("left");
                        drawImage(ParrotWalk, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                                player2.getHeight());
                    } else if (player2.direction.equals("right")) {
                        player2.setLastDirection("right");
                        drawImage(ParrotWalkFlipped, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                                player2.getHeight());
                    } else {
                        // If the player last moved a certain direction, the idle player will be facing
                        // that direction.
                        if (player2.getLastDirection().equals("left")) {
                            drawImage(Parrot, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                                    player2.getHeight());
                        } else if (player2.getLastDirection().equals("right")) {
                            drawImage(ParrotFlipped, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                                    player2.getHeight());
                        }
                    }
                }

            }

            // Falling platforms
            for (Platform platform : platforms) {
                changeColor(red);
                drawSolidRectangle(platform.getPosX(), platform.getPosY(), platform.getLength(), platform.getWidth());
            }

            // Win Count P1
            changeColor(white);
            drawText(mWidth / mWidth + 10, mHeight / mHeight + 50, "P1 Wins:" + winP1, "Arial", 20);

            // Win Count P2
            drawText(mWidth - 110, mHeight / mHeight + 50, "P2 Wins: " + winP2, "Arial", 20);
        }

        // Menu Background
        else if (menu) {
            if (!menuMusicPlaying) {
                // Play Menu Music
                playAudio(menuMusic);
                startAudioLoop(menuMusic, 1);
                menuMusicPlaying = true;
            }

            clearBackground(mWidth, mHeight);
            // Background
            drawImage(Background, 0, 0, 800, 500);

            // Logo
            drawImage(Logo, 175, -50, 472, 328);

            // Change Text Colour
            changeColor(Color.BLACK);

            // Draw Single Player Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(150, 250, "     Single Player  (1)", "Arial", 50);

            // Draw Multiplayer Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(150, 325, "       Multiplayer  (2)", "Arial", 50);
            // Draw Instructions Label, this can be changed later on to a button or we can
            // check if the user presses on this area
            drawText(150, 400, "       Instructions (3)", "Arial", 50);

            drawText(150, 475, "          Quit (Esc)", "Arial", 50);

        }

        if (gameOver) {
            changeBackgroundColor(Color.BLACK);
            changeColor(Color.RED);
            drawText(250, 200, "GAME OVER!", "Arial", 50);
            drawText(270, 300, "Press SPACE to Retry", "Arial", 30);
            drawText(290, 350, "Press ESC to Menu", "Arial", 30);

        }

        if (gamePaused) {
            if (victory) {
                changeColor(Color.RED);
                drawText(250, 200, "GAME OVER!", "Arial", 40);
                changeColor(Color.GREEN);
                if (winP2 > winP1) {
                    drawText(250, 260, "Player 2 Wins! ", "Arial", 40);
                } else {
                    drawText(250, 260, "Player 1 Wins! ", "Arial", 40);
                }
            } else {
                changeColor(Color.BLACK);
                drawText(mWidth / 2 - 150, mHeight / 2 - 50, "Game Paused", "Arial", 50);
                drawText(mWidth / 2 - 110, mHeight / 2 + 50, "Press P to Resume", "Arial", 30);
                drawText(mWidth / 2 - 110, mHeight / 2 + 100, "Press Esc to Menu", "Arial", 30);
            }
        }

        if (instructions1) {
            System.out.println("Instructions1");
            clearBackground(mWidth, mHeight);
            // Background
            drawImage(Background, 0, 0, 800, 500);
            // Logo
            drawImage(Logo, 175, -50, 472, 328);
            changeColor(Color.BLACK);
            drawText(290, 225, "Singleplayer:", "Arial", 40);
            drawText(170, 275, "Move the player using the arrow keys", "Arial", 30);
            drawText(230, 325, "onto the falling platforms.", "Arial", 30);
            drawText(195, 375, "Your aim to survive as long as", "Arial", 30);
            drawText(250, 425, "possible without falling.", "Arial", 30);
            drawText(180, 475, "(Next: Right arrowkey , Menu: Esc)", "Arial", 30);

        }
        if (instructions2) {
            System.out.println("Instructions2");
            clearBackground(mWidth, mHeight);
            // Background
            drawImage(Background, 0, 0, 800, 500);
            // Logo
            drawImage(Logo, 175, -50, 472, 328);
            changeColor(Color.BLACK);
            drawText(300, 225, "Multiplayer:", "Arial", 40);
            drawText(170, 275, "Move player 1 using the arrow keys", "Arial", 30);
            drawText(230, 325, "and player 2 using WASD.", "Arial", 30);
            drawText(220, 375, "If the other player falls first, ", "Arial", 30);
            drawText(200, 425, "you get a point. First to 5 wins!", "Arial", 30);
            drawText(190, 475, "(Back: Left arrowkey , Menu: Esc)", "Arial", 30);

        }
        if (victory) {
            changeColor(Color.RED);
            drawText(250, 200, "GAME OVER!", "Arial", 40);
            changeColor(Color.GREEN);
            if (winP2 > winP1) {
                drawText(250, 260, "Player 2 Wins! ", "Arial", 40);
            } else {
                drawText(250, 260, "Player 1 Wins! ", "Arial", 40);
            }
            changeColor(Color.BLACK);
            drawText(150, 350, "Press Space for Rematch", "Arial", 40);
            drawText(200, 400, "Press Esc to Menu", "Arial", 40);

        }
    }

    // Scoring System when player is no longer using the starting platform start
    // incrementing score
    private void loadScore() {
        // Single Player score | Survival Time
        if (singlePlayerStarted) {
            // Check that the starting platform is gone
            if (!playerOnStart && !gameOver) {
                score += 1;
            }
        }

        // Multiplayer Wins | Whoever survives add 1 to win
        if (multiplayerStarted) {

            // If Player 1 falls add 1 to Player 2 wins
            if (players.get(0).getPosY() > mHeight) {
                if (!gameOver) {
                    winP2++;
                }
            }

            // If player 2 falls add 1 to Player 1 Wins
            if (players.get(1).getPosY() > mHeight) {
                if (!gameOver) {
                    winP1++;
                }
            }
        }
    }

    // Call this funtion in the game over function
    // Save the Single Player Highscore to a txt file
    private void saveHighscore() {
        // Change txt file path to proper path
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/highscore.txt"))) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/highscore.txt"))) {
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
                startMultiplayer();
            } else if (keyCode == KeyEvent.VK_3) {
                instructions1 = true;
                menu = false;

            }
        }
        if (instructions1) {
            if (keyCode == KeyEvent.VK_RIGHT) {
                // System.out.println("RIGHT pressed in instr1");
                instructions1 = false;
                instructions2 = true;
            }
        }
        if (instructions2) {
            if (keyCode == KeyEvent.VK_LEFT) {
                // System.out.println("LEFT pressed in instr2");
                instructions2 = false;
                instructions1 = true;

            }
        }
        // When the player presses the escape key it pauses the game
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gamePaused = false;
            if (menu) {
                System.exit(0);
            }
            if (instructions1) {
                instructions1 = false;
                menu = true;
            }
            if (instructions2) {
                instructions2 = false;
                menu = true;
            }
            if (singlePlayerStarted || multiplayerStarted) {
                if (singlePlayerStarted) {
                    platforms.clear();
                    gameOver = false;
                    singlePlayerStarted = false;
                    playerOnStart = true;
                }
                if (multiplayerStarted) {
                    gameOver = false;
                    platforms.clear();
                    winP1 = 0;
                    winP2 = 0;
                    multiplayerStarted = false;
                    player1OnStart = true;
                    player2OnStart = true;

                }
                menu = true;
            }

        }
        if (keyCode == KeyEvent.VK_P) {
            if (!gamePaused && (singlePlayerStarted || multiplayerStarted)) {
                // If the game is not paused then pause it
                System.out.println("Game Paused");
                gamePaused = true;
            } else {
                // If the game is paused then unpause it
                System.out.println("Game Resumed");
                gamePaused = false;
            }
        }

        // Only allow player movement when the game is not paused
        if (!gamePaused) {
            // Player movement for when the game has started
            if (singlePlayerStarted) {
                if (keyCode == KeyEvent.VK_LEFT) {
                    System.out.println("Left pressed");
                    direction = "left";

                }

                if (keyCode == KeyEvent.VK_RIGHT) {
                    System.out.println("Right pressed");
                    direction = "right";

                }

                if (keyCode == KeyEvent.VK_UP) {
                    System.out.println("Up pressed");

                    // Lets the player jump and double jump
                    player.jump();

                    drawImage(ToucanJump, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
                }
            }

            // Multiplayer Player Movement
            if (multiplayerStarted) {
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                // Jump Movement
                if (keyCode == KeyEvent.VK_UP) {
                    // Lets the player jump and double jump
                    player1.jump();

                    drawImage(ToucanJump, player1.getPosX(), player1.getPosY(), player1.getWidth(),
                            player1.getHeight());
                }

                if (keyCode == KeyEvent.VK_W) {
                    // Lets the player jump and double jump
                    player2.jump();

                    drawImage(ParrotJump, player2.getPosX(), player2.getPosY(), player2.getWidth(),
                            player2.getHeight());
                }

                // Left Movement
                if (keyCode == KeyEvent.VK_LEFT) {
                    player1.setDirection("left");
                }

                if (keyCode == KeyEvent.VK_A) {
                    // Player 1 jump
                    player2.setDirection("left");
                }

                // Right Movement
                if (keyCode == KeyEvent.VK_RIGHT) {
                    // Player 2 jump
                    player1.setDirection("right");
                }

                if (keyCode == KeyEvent.VK_D) {
                    // Player 1 jump
                    player2.setDirection("right");
                }
            }
        }
        if (victory) {
            if (keyCode == KeyEvent.VK_SPACE) {
                gamePaused = false;
                resetscores = true;
                resetGame();
                victory = false;
                startMultiplayer();
            }
            if (keyCode == KeyEvent.VK_ESCAPE) {
                victory = false;
                multiplayerStarted = false;
                menu = true;
            }
        }

        // To reset the game back to single player hit enter or space
        if (gameOver) {
            if (keyCode == KeyEvent.VK_SPACE) {
                if (singlePlayerStarted) {
                    gameOver = false;
                    resetGame();
                    startSinglePlayer();
                }

                if (multiplayerStarted) {
                    resetGame();
                    startMultiplayer();
                }
            }
        }
    }

    // Key Released for Player Movement
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (singlePlayerStarted) {
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                direction = "stop";
            }
        }

        // If the game is in multiplayer and either player has stopped holding a key
        // down
        if (multiplayerStarted) {
            Player player1 = players.get(0);
            Player player2 = players.get(1);
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                // Stop player moving
                player1.setDirection("stop");
            }

            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
                // Stop player moving
                player2.setDirection("stop");
            }
        }
    }

    // Player Movement Function
    public void playermovement() {
        // Player Movement for Single Player
        if (singlePlayerStarted) {

            if (direction.equals("left")) {
                player.setPosX(player.getPosX() - player.getSpeed());
            }

            if (direction.equals("right")) {
                player.setPosX(player.getPosX() + player.getSpeed());
            }

            if (direction.equals("stop")) {
                player.setPosY(player.getPosY());
                player.setPosX(player.getPosX());
            }
        }

        // Player Movement for Multiplayer
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Player Movement for Multiplayer
            Player player1 = players.get(0);
            Player player2 = players.get(1);

            if (player1.getDirection().equals("left")) {
                player1.setPosX(player1.getPosX() - player1.getSpeed());
            }

            if (player1.getDirection().equals("right")) {
                player1.setPosX(player1.getPosX() + player1.getSpeed());
            }

            if (player1.getDirection().equals("stop")) {
                player1.setPosY(player1.getPosY());
                player1.setPosX(player1.getPosX());
            }

            if (player2.getDirection().equals("left")) {
                player2.setPosX(player2.getPosX() - player2.getSpeed());
            }

            if (player2.getDirection().equals("right")) {
                player2.setPosX(player2.getPosX() + player2.getSpeed());
            }

            if (player2.getDirection().equals("stop")) {
                player2.setPosY(player2.getPosY());
                player2.setPosX(player2.getPosX());
            }
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

        // Create the Platforms
        createPlatforms();

        // Create Players
        createPlayer();

        // Create Start platform
        startPlatform();
    }

    // Create Platform Objects
    public void createPlatforms() {
        // Create Random to randomize different aspects of the platforms
        Random random = new Random();

        // Occupied Array checks if a boolean can spawn within the grid
        boolean[] occupied = new boolean[gridColumns];

        // For loop to create each platform
        // R: Why a for loop? Could we while this until gameover condition?
        for (int i = 0; i < platformAmount; i++) {
            // Variables used for creating the platforms
            double posX;
            double posY = 0;
            double length = gridWidth - 40;
            double width = 5;
            double fallSpeed;
            int column;

            // Do while loop to make sure platforms spawn in valid columns
            do {
                // Get a random column from the grid
                column = random.nextInt(gridColumns);

            } while (occupied[column] == true);
            occupied[column] = true;

            // Set the posistion of the platform to be within the grid
            posX = gridWidth * column;

            // Random fall speed between 5 and 13
            fallSpeed = 25 + random.nextDouble() * 15;

            // Add the platform to the array list
            platforms.add(new Platform(posX, posY, length, width, fallSpeed));
            // add a variable wait to platform spawns?
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

        // Create a new random for randomizing speed and column of the platforms | Same
        // as createPlatforms except whilst the code is running
        Random random = new Random();
        int added = 15;
        boolean[] occupied = new boolean[gridColumns];

        // Check if there are platforms that are too close to the top and make it so
        // platforms cannot spawn there
        for (Platform platform : platforms) {
            // Get Platform position on the Y axis | This will check whether we should spawn
            // a new platform in this column or not
            if (platform.getPosY() < mHeight / 2) {
                // Get Column of the platform by reversing the creation of the platform in the
                // column
                int column = (int) platform.getPosX() / gridWidth;

                // The platform is too close to the top so platforms spawned in this column will
                // overlap with the current platform
                occupied[column] = true;
            }
        }

        // Create an Array List to find all the current free columns
        if (platforms.size() < added) {
            ArrayList<Integer> freeColumns = new ArrayList<>();

            // For loop to get all free columns into the ArrayList
            for (int column = 0; column < gridColumns; column++) {
                // If the column is not occupied
                if (!occupied[column]) {
                    // Add the column to the ArrayList
                    freeColumns.add(column);
                }
            }

            // Randomize the free columns and spawn a set amount in this new batch
            Collections.shuffle(freeColumns);
            int platformsToSpawn = Math.min(5, freeColumns.size());

            // For loop to randomly spawn the set amount of platforms
            for (int i = 0; i < platformsToSpawn; i++) {
                // Randomize the column and check that it's not occupuied
                int column = freeColumns.get(i);

                // Variables used for creating the platforms
                double posX = column * gridWidth;
                double posY = 0;
                double length = gridWidth - 40;
                double width = 5;
                double fallSpeed = 25 + random.nextDouble() * 15;

                // Add the platform to the list
                platforms.add(new Platform(posX, posY, length, width, fallSpeed));
            }
        }
    }

    // Player Platform Collision Detection
    public void playerPlatformCollision() {
        if (singlePlayerStarted) {
            // Check if the player is on the starting platform
            if (playerOnStart) {
                // Variables that show the top of the starting platform, and the players
                // previous position and current position
                double platformTop = startPlatform.getPosY();
                double playerYPrev = player.getPrevPosY() + player.getHeight();
                double playerYNow = player.getPosY() + player.getHeight();

                // Check if player crosses the starting platform because the fall speed was too
                // large and made them go through
                boolean crossedPlatform = (playerYPrev <= platformTop) &&
                        (playerYNow >= platformTop) &&
                        (player.getPosX() + player.getWidth() > startPlatform.getPosX()) &&
                        (player.getPosX() < startPlatform.getPosX() + startPlatform.getLength()) &&
                        (player.getFallSpeed() > 0);

                // If the player crossed the starting platform then make sure they land on it
                if (crossedPlatform) {
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
            for (Platform platform : platforms) {
                // Variables that show the top of the platform, and the players previous
                // position and current position
                double platformTop = platform.getPosY();
                double playerYPrev = player.getPrevPosY() + player.getHeight();
                double playerYNow = player.getPosY() + player.getHeight();

                // Check if player cross the platform because the fall speed was too large and
                // made them go through
                boolean crossedPlatform = (playerYPrev <= platformTop) &&
                        (playerYNow >= platformTop) &&
                        (player.getPosX() + player.getWidth() > platform.getPosX()) &&
                        (player.getPosX() < platform.getPosX() + platform.getLength()) &&
                        (player.getFallSpeed() > 0);

                // Check if the player is on the platform / crossed the platform due to too much
                // speed
                if (crossedPlatform) {
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
                else {
                    player.onPlatform(false);
                }
            }
        }

        // Not sure if this is the best way to do this
        if (multiplayerStarted) {
            for (int i = 0; i < players.size(); i++) {
                // Get the player that it's checking
                Player player = players.get(i);

                // Starting Platform for Player and Boolean Check if they're on it
                Platform sPlatform = null;
                boolean onSPlatform = false;

                // Get the starting platform for Player 1 and if they're on it
                if (i == 0) {
                    sPlatform = startPlatform1;
                    onSPlatform = player1OnStart;
                }

                // Get the starting platform for Player 2 and if they're on it
                else if (i == 1) {
                    sPlatform = startPlatform2;
                    onSPlatform = player2OnStart;
                }

                // Check if the player is on their starting platform
                if (onSPlatform) {
                    // Variables that show the top of the starting platform, and the players
                    // previous position and current position
                    double platformTop = sPlatform.getPosY();
                    double playerYPrev = player.getPrevPosY() + player.getHeight();
                    double playerYNow = player.getPosY() + player.getHeight();

                    // Check if player crosses the starting platform because the fall speed was too
                    // large and made them go through
                    boolean crossedPlatform = (playerYPrev <= platformTop) &&
                            (playerYNow >= platformTop) &&
                            (player.getPosX() + player.getWidth() > sPlatform.getPosX()) &&
                            (player.getPosX() < sPlatform.getPosX() + sPlatform.getLength()) &&
                            (player.getFallSpeed() > 0);

                    // If the player crossed the starting platform then make sure they land on it
                    if (crossedPlatform) {
                        // Make sure the player doesn't clip through the platform
                        player.setPosY(sPlatform.getPosY() - player.getHeight());

                        // Change fall speed to 0 so player doesn't fall through platform
                        player.setFallSpeed(0.0);

                        // Set onPlatform to true
                        player.onPlatform(true);

                        // Set player's ability to jump to true
                        player.canJump(true);

                        // Set player's ability to double jump to true
                        player.canDoubleJump(true);

                        // Don't check the red platforms if they're on the starting one
                        continue;
                    }
                }

                // If the player is not on the statting platform set onPlatform to false
                // Check Play Collision for ALL platforms
                for (Platform platform : platforms) {
                    // Variables that show the top of the platform, and the players previous
                    // position and current position
                    double platformTop = platform.getPosY();
                    double playerYPrev = player.getPrevPosY() + player.getHeight();
                    double playerYNow = player.getPosY() + player.getHeight();

                    // Check if player cross the platform because the fall speed was too large and
                    // made them go through
                    boolean crossedPlatform = (playerYPrev <= platformTop) &&
                            (playerYNow >= platformTop) &&
                            (player.getPosX() + player.getWidth() > platform.getPosX()) &&
                            (player.getPosX() < platform.getPosX() + platform.getLength()) &&
                            (player.getFallSpeed() > 0);

                    // Check if the player is on the platform / crossed the platform due to too much
                    // speed
                    if (crossedPlatform) {
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
                        if (i == 0)
                            player1OnStart = false;
                        else if (i == 1)
                            player2OnStart = false;

                        break;
                    }

                    // If the player is not on a platform set onPlatform to false
                    else {
                        player.onPlatform(false);
                    }
                }
            }
        }
    }

    // Player creation:
    public void createPlayer() {
        // Create 1 player in the middle if it's a single player game
        if (singlePlayerStarted) {
            double startX = mWidth / 2;
            double startY = mHeight / 2 - 60;
            int acceleration = 1;
            double speed = 5.0;
            double fallSpeed = 10.0;
            boolean valid = true;

            player = new Player(startX, startY, 50, 50, acceleration, speed, fallSpeed, valid);
        }

        // Create 2 players one on the left one on the right if it's a multiplayer game
        if (multiplayerStarted) {
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
            player1.setDirection("stop");

            // Create Player 2
            Player player2 = new Player(startX2, startY2, 50, 50, acceleration, speed, fallSpeed, valid);
            player2.setDirection("stop");

            // Add the players to the players array list
            players.add(player1);
            players.add(player2);
        }
    }

    // Player Movement
    public void playergravity(double dt) {
        if (singlePlayerStarted) {
            // Set Gravity
            double gravity = 0.5;

            // Get Previous Position
            player.setPrevPosY(player.getPosY());

            // Set Fallspeed of Player
            player.setFallSpeed(player.getFallSpeed() + gravity);

            // Set Player's Fall
            player.setPosY(player.getPosY() + player.getFallSpeed());
        }
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Set Gravity for Player 1
            double gravity = 0.5;
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

    // Start Platform
    public void startPlatform() {
        if (singlePlayerStarted) {
            double posX = mWidth / 2;
            double posY = mHeight / 2;
            double length = 50;
            double width = 10;
            double fallSpeed = 0.0;

            startPlatform = new Platform(posX, posY, length, width, fallSpeed);
        }

        if (multiplayerStarted) {
            // Create two starting platforms for multiplayer
            double posX1 = mWidth / 4;
            double posY1 = mHeight / 2;
            double posX2 = mWidth * 3 / 4;
            double posY2 = mHeight / 2;
            double length = 50;
            double width = 10;
            double fallSpeed = 0.0;

            // Create Player 1's Start Platform
            startPlatform1 = new Platform(posX1, posY1, length, width, fallSpeed);

            // Create Player 2's Start Platform
            startPlatform2 = new Platform(posX2, posY2, length, width, fallSpeed);
        }
    }

    // Gameover (Works, needs paint / resets on key?)
    public void gameover() {
        // Single Player Game Over
        if (singlePlayerStarted) {
            if (player.posY >= mHeight) {
                // Game Over
                System.out.println("Gameover");
                gameOver = true;

                // Save Highscore if the highscore is greater than the score
                if (score > highscore) {
                    // Since the score goes up in such fast succession there is a difference of 1 on
                    // recorded score
                    highscore = score - 1;
                    saveHighscore();
                }
            }
        }

        // Multiplayer Game Over
        if (multiplayerStarted) {
            boolean anyPlayerOut = false;
            for (Player p : players) {
                if (p != null && p.getPosY() >= mHeight) {
                    anyPlayerOut = true;
                    break;
                }
            }
            if (anyPlayerOut) {
                if (winP1 == 5 || winP2 == 5) {
                    victory = true;
                    gamePaused = true;
                }
                resetGame();
                startMultiplayer();
            }
        }
    }

    // CheckBounds (used to keep player onscreen)
    public void checkbounds() {
        if (singlePlayerStarted) {
            if (player.getPosX() < -15) {
                player.setPosX(-15);
            }
            if (player.getPosX() > 763) {
                player.setPosX(763);
            }
        }
        if (multiplayerStarted && players != null && players.size() >= 2) {
            // Check Player 1 Bounds
            Player player1 = players.get(0);
            if (player1.getPosX() < 15) {
                player1.setPosX(15);
            } else if (player1.getPosX() > 763) {
                player1.setPosX(763);
            }

            // Check Player 2 Bounds
            Player player2 = players.get(1);
            if (player2.getPosX() < 15) {
                player2.setPosX(15);
            } else if (player2.getPosX() > 763) {
                player2.setPosX(763);
            }
        }
    }

    // Rest platforms
    public void resetGame() {
        System.out.println("Reset Game");
        // Reset Score to 0
        score = 0;

        // Clear the platform array
        platforms.clear();

        // Set gameover to false
        gameOver = false;
        if (singlePlayerStarted) {
            player.setPosX(startPlatform.posX);
            player.setPosY(startPlatform.posY - 50);
        }

        if (multiplayerStarted) {
            Player player1 = players.get(0);
            player1.setPosX(startPlatform1.posX);
            player1.setPosY(startPlatform1.posY);
            Player player2 = players.get(1);
            player2.setPosX(startPlatform2.posX);
            player2.setPosY(startPlatform2.posY);
            if (resetscores) {
                winP2 = 0;
                winP1 = 0;
                resetscores = false;
            }
        }

        // Set Player to be on Start
        playerOnStart = true;
        player1OnStart = true;
        player2OnStart = true;

        // Clear the players
        players.clear();
    }

}
