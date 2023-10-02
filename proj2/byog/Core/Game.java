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
    Input inputCollector;
    boolean cheatMode = false;

    public static final int miniInterval = 15;

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
        } else if (initInput == 'l') {
            wg = load();
        } else {
            // input is 'q'
            System.exit(0);
        }

        assert wg != null;
        playGame();
    }

//    @Deprecated
//    void playGame(boolean flag) {
//        TERenderer renderer = new TERenderer();
//        renderer.initialize(WIDTH, HEIGHT);
//        renderer.renderFrame(wg.getVisible());
//
//        String input;
//        boolean f; // whether something has changed during a miniInterval
//        while (true) {
//            StdDraw.pause(miniInterval);
//            f = false;
//
//            /*
//            Process keyboard input:
//             */
//            boolean canPlayerAct = wg.canPlayerAct();
//            input = Input.tryValidCombo(Input.comboLength, canPlayerAct);
//            if (canPlayerAct && !input.equals("")) { // valid input, and player can act
//
//                if (input.equals("o")) {
//                    cheat();
//                } else if (input.equals("q")) {
//                    save();
//                } else {
//                    wg.playerAct(input);
//                    cheatMode = false;
//                }
//
//                wg.update(input); // update all MTs for the time Input spent soliciting combo input;
//                f = true;
//            }
//            Input.clearKeyQueue();
//
//            /*
//            Move all MovingThing;
//             */
//            if (wg.checkChange() > 0) {
//                f = true;
//            }
//
//            if (!cheatMode) {
//                wg.luminateAll();
//            }
//            renderer.renderFrame(wg.getVisible());
//
//            wg.update();
//        }
//    }
    void playGame() {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        renderer.renderFrame(wg.getVisible());

        String input;
        inputCollector = new Input();
        boolean f; // whether something has changed during a miniInterval
        while (true) {
            StdDraw.pause(miniInterval);
            f = false;

            /*
            Process keyboard input:
             */
            boolean canPlayerAct = wg.canPlayerAct();
            input = inputCollector.oneTurn();
            if (canPlayerAct && !input.equals("") && !input.equals("0")) {
                // valid input, and player can act
                if (input.equals("o")) {
                    cheat();
                } else if (input.equals("q")) {
                    save();
                } else {
                    wg.playerAct(input);
                    cheatMode = false;
                }

                f = true;
            }

            /*
            Move all MovingThing;
             */
            if (wg.checkChange() > 0) {
                f = true;
            }
            if (f) {
                if (!cheatMode) {
                    wg.luminateAll();
                }
                renderer.renderFrame(wg.getVisible());
            }

            wg.update();
        }
    }
    void cheat() {
        for (int x = 0; x < WG.WIDTH; x ++) {
            for (int y = 0; y < WG.HEIGHT; y ++) {
                WG.places[x][y].visible = true;
            }
        }
        wg.updateVisible();
        cheatMode = true;
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
        world.updateVisible();
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
        return new WG();
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
