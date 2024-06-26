package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.*;
import java.io.*;

import edu.princeton.cs.introcs.StdDraw;

public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    WG wg = null;
    Input inputParser;
    Interval frameIn;
    public static final int frameInterval = 50; // AT LEAST 1000/50 = 20 frames per sec;

    public static final int miniInterval = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        setCanvas();

        char initInput = Input.solicitInitialInput();
        if (initInput == 'n') {
            long seed = Input.solicitSeed();

            int numPlayers = Input.solicitNumPlayers();

            if (seed == -1) {
                wg = new WG(numPlayers);
            } else {
                wg = new WG(seed, numPlayers);
            }
        } else if (initInput == 'l') {
            wg = load();
        } else {
            // input is 'q'
            System.exit(0);
        }

        playGame();
    }
    void playGame() {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(wg.getVisible());

        /*
         Prevent too long interval between frame refreshment;
         Without this, a damaged thing may not switch back to its original appearance
         if nothing else has changed in the game;
         */
        frameIn = new Interval(frameInterval);

        inputParser = new Input(this, wg.numPlayers);

        boolean f; // whether something has changed during a miniInterval
        while (true) {
            StdDraw.pause(miniInterval);
            f = false;

            /*
            Process keyboard input:
             */
            int input  = inputParser.oneTurn();
            if (input != 0) {
                // something happened
                f = true;
                if (input > 10) { // cheat mode;
                    wg.cheat();
                } else if (input < 0) { // quit and save;
                    save();
                } else { // player made an usual action;

                }
            }

            /*
            Everything changes over time;
             */
            if (wg.checkChange() > 0) {
                f = true;
            }

            /*
            If something changed in the game,
            or the previous frame refreshment is too long ago,
            get the appearance of the world, and render frame;
             */
            if (f || frameIn.ended()) {
                renderer.renderFrame(wg.getVisible());
                frameIn.renew(frameInterval);
            } else {
                frameIn.update();
            }

            /*
            Update time;
             */
            wg.update();
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
            world = new WG(seed, 1);
        } else if (start == 'l') {
            world = load();
        } else {
            throw new RuntimeException("Type 'N' or 'L' to start !");
        }
        //world.updateVisible();
        finalWorldFrame = world.getVisible();
        return finalWorldFrame;
    }

    private void save() {

        wg.save(); //

        File f = new File("./save.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(wg);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

        System.exit(0); //
    }
    private WG load () {
        File f = new File("./save.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                WG loadWorld = (WG) os.readObject();
                os.close();

                loadWorld.load(); //

                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we return a new one. */
        int numPlayers = Input.solicitNumPlayers();
        return new WG(numPlayers);
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

//    public static void main (String[] args) {
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//
//        Game g = new Game();
//        TETile[][] world = g.playWithInputString("n01234567s");
//
//        ter.renderFrame(world);
//    }
}
