// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer

import java.awt.Color;

public class PlatformPanic extends GameEngine
{
    // Variable Creation

    // Window Size & Grid
    private static int gameHeight;
    private static int gameWidth;
    private static int gridSize;

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

    // Initialize Variables runs only when the program has been run
    @Override
    public void init()
    {
        // Windows Size and Grid Size
        gameHeight = 750;
        gameWidth = 500;
        gridSize = 10;
        setWindowSize(gameWidth, gameHeight);
        
        // Score & Highscore
        score = 0;
        highscore = loadHighscore();
        
        // Set Gamemodes to false so that the user can press a button for which game they want
        singlePlayerStarted = false;
        multiplayerStarted = false;
        
        // Game Menu & Pause
        gamePaused = false;
        menu = true;
    }

    // Used to update game logic such as movement
    @Override
    public void update(double dt)
    {

    }

    // Used to render background and graphics
    @Override
    public void paintComponent()
    {
        // Create Single Player background etc
        if (singlePlayerStarted)
        {
            // Change Background to single player background and clear
            changeBackgroundColor(blue);
            clearBackground(gameWidth, gameHeight);
        }

        // Create Multiplayer background etc
        else if (multiplayerStarted)
        {
            // Change Background to multiplayer background and clear
            changeBackgroundColor(Color.cyan);
            clearBackground(gameWidth, gameHeight);
        }

        else if (menu)
        {
            // Change Background to single player background and clear
            changeBackgroundColor(Color.black);
            clearBackground(gameWidth, gameHeight);
            
            // Change Text Colour
            changeColor(white);

            // Draw Title (can be changed to img logo if added later)
            drawText(height() / 2 - 250, width() / 2 - 100, "Platform Panic", "Arial", 40);

            // Draw Single Player Label, this can be changed later on to a button or we can check if the user presses on this area
            drawText(height() / 2 - 250, width() / 2, "     Single Player", "Arial", 30);

            // Draw Multiplayer Label, this can be changed later on to a button or we can check if the user presses on this area
            drawText(height() / 2 - 250, width() / 2 + 50, "       Multiplayer", "Arial", 30);
        }
    }

    private int loadHighscore()
    {
        return 0;
    }

    public static void main(String[] args)
    {
        PlatformPanic platformPanic = new PlatformPanic();
        GameEngine.createGame(platformPanic, 10);
    }

    // Start Single Player Gamemode
    public void startSinglePlayer()
    {
        // Set Variables
        menu = false;
        singlePlayerStarted = true;

        // Debug
        System.out.println("Single Player Started");
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
}
