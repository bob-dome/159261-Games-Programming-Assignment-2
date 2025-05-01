// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer

public class PlatformPanic extends GameEngine
{
    // Variable Creation

    // Window Size & Grid
    private static int gameHeight;
    private static int gameWidth;
    private static int gridSize;

    // Main Menu & Pause
    private static boolean gameMenu;
    private static boolean gamePaused;

    // Score & Highscore
    private static int score;
    private static int highscore;


    // Initialize Variables runs only when the program has been run
    @Override
    public void init()
    {
        // Windows Size and Grid Size
        gameHeight = 750;
        gameWidth = 500;
        gridSize = 10;
        setWindowSize(gameWidth, gameHeight);

        // Game Menu & Pause
        gamePaused = false;
        gameMenu = true;

        // Score & Highscore
        score = 0;
        highscore = loadHighscore();
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
        // Create Main Menu
        if (gameMenu)
        {
            // Change the Background Colour and Clear it
            changeBackgroundColor(white);
            clearBackground(gameWidth, gameHeight);

            // Menu Buttons
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
}
