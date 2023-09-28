package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.*;
import java.util.Random;
import edu.princeton.cs.introcs.StdDraw;

public class Game {
    TERenderer renderer = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    WG wg = null;

    public static final int interval = 150;
    public static final int keyInterval = 70; // interval between two key presses;
    public static final int miniInterval = 10;
    static final Character[] validInputs = {'w', 'a', 's', 'd', 'k', 'o'}; // TODO
    static final String[] combos = {"wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
                              "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd", // j with directions
                                };

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        setCanvas();

        char initInput = Input.solicitInitialInput();
        if (initInput == 'n') {
            long seed = Input.solicitSeed();
            if (seed == -1) {
                wg = new WG();
            } else {
                wg = new WG(seed);
            }
        } else if(initInput == 'l') {
            wg = load();
        } else {
            // input is 'q'
            System.exit(0);
        }

        assert wg != null;
        playGame(wg);
    }

    void playGame(WG wg) {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(wg.getVisible());

        while (true) {
            Input.clearKeyQueue();

            StdDraw.pause(interval - keyInterval);

            String input = Input.solicitInputString(2);
            if (input.equals("o")) {
                cheat(); // TODO
            }
            wg.player.act(input);
            renderer.renderFrame(wg.getVisible());
        }
    }
    void cheat() {
        for (int x = wg.startWIDTH; x < wg.WIDTH; x ++) {
            for (int y = wg.startHEIGHT; y < wg.HEIGHT; y ++) {
                wg.isVisible[x][y] = true;
            }
        }
        wg.updateVisible();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        char start = input.charAt(0);
        long seed = Long.parseLong(input.substring(1, input.indexOf('s')));

        TETile[][] finalWorldFrame;
        WG world;
        if (start == 'n') {
            world = new WG(seed);
        } else if (start == 'l') {
            world = load();
        } else {
            throw new RuntimeException("Type 'N' or 'L' to start !");
        }
        finalWorldFrame = world.getVisible();
        return finalWorldFrame;
    }

    private WG load () {
        return null;
    }

    /**
     * Some code I CVed from GitHub;
     */
    private static void setCanvas() {
        StdDraw.setCanvasSize(640, 640);
        StdDraw.setXscale(0, 640);
        StdDraw.setYscale(0, 640);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
        StdDraw.text(320, 480, "THE GAME");

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
        StdDraw.text(320, 345, "New Game (N)");
        StdDraw.text(320, 320, "Load Game (L)");
        StdDraw.text(320, 295, "Quit (Q)");

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }


    public static void main (String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Game g = new Game();
        TETile[][] world = g.playWithInputString("n01234567s");

        ter.renderFrame(world);
    }
}
