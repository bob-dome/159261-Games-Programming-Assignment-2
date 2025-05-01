// 24006168 Jordan Burmeister, 24003491 Wiremu Loader, 24002464 Aimee Gaskin Fryer

public class Game extends GameEngine
{
    // Variable Creation

    // Window Size & Grid
    private static int gameHeight;
    private static int gameWidth;
    private static int gridSize;


    // Initialize Variables runs only when the program has been run
    @Override
    public void init()
    {
        // Windows Size and Grid Size
        gameHeight = 750;
        gameWidth = 500;
        gridSize = 10;

        setWindowSize(gameWidth, gameHeight);
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

    }

    public static void main(String[] args)
    {
        Game game = new Game();
        GameEngine.createGame(game, 10);
    }
}
