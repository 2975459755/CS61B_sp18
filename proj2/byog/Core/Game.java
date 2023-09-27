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

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        setCanvas();

        WG world = null;
        char initInput = solicitInitialInput();
        if (initInput == 'n') {
            long seed = solicitSeed();
            world = new WG(seed);
        } else if(initInput == 'l') {
            world = load();
        } else {
            // input is 'q'
            System.exit(0);
        }

        assert world != null;
        playGame(world);
    }

    private void playGame(WG world) {
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(world.getWorld());

        while (true) {
            clearKeyQueue();

            char input = solicitInputChar();
            world.player.move(String.valueOf(input));
            renderer.renderFrame(world.getWorld());

            StdDraw.pause(150);
        }
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
        finalWorldFrame = world.getWorld();
        return finalWorldFrame;
    }

    private WG load () {
        return null;
    }

    /**
     * Clear the key queue in StdDraw;
     */
    private void clearKeyQueue() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }
    private char solicitInitialInput() {
        while(true) {
            char key = solicitInputChar();
            if (key == 'n' || key == 'l' || key == 'q') {
                return key;
            }
        }
    }
    private char solicitInputChar() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                return key;
            }
        }
    }

    /**
     * Some code I CVed from GitHub;
     */
    private void setCanvas() {
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

    /**
     * Some code I CVed from GitHub;
     */
    private long solicitSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(320, 320, "Enter seed. Press S to end.");
        StdDraw.show();

        StringBuilder builder = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (key == 's') {
                    break;
                }

                builder.append(key);
                StdDraw.clear(Color.BLACK);
                StdDraw.text(320, 320, "Enter seed. Press S to end.");
                StdDraw.text(320, 300, "Your input: " + builder);
                StdDraw.show();
            }
        }

        return Long.parseLong(builder.toString());
    }


    public static void main (String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Game g = new Game();
        TETile[][] world = g.playWithInputString("n01234567s");

        ter.renderFrame(world);
    }
}
