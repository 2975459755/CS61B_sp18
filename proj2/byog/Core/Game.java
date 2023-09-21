package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
        long seed = 9898989;
        TETile[][] finalWorldFrame = buildWorld();
        randomWorld(finalWorldFrame, seed);
        return finalWorldFrame;
    }

    /**
     * Pseudo-randomly generate a world with given `seed`;
     * @param seed: any positive integer up to 9,223,372,036,854,775,807;
     */
    public static void randomWorld(TETile[][] world, long seed) {
        Random rand = new Random(seed);
        int x = Math.floorDiv(WIDTH, 2);
        int y = Math.floorDiv(HEIGHT, 2);

        fillWithFLOOR(world, x, y, rand, 0);
        addWALL(world);
    }
    public static void fillWithFLOOR(TETile[][] world, int x, int y, Random rand, int n) {
        // n: store the number of filled tiles
        int[] coor;
         do {
            coor = searchNext(world, x, y, rand, 4); // find the next tile to fill
            x = coor[0];
            y = coor[1];
            world[x][y] = Tileset.FLOOR; // fill with FLOOR
            n ++;
        } while (hasNext(world, x, y, 4));
         /*
        Inadequate FLOORs: try filling again;
         */
        if (n < WIDTH * HEIGHT / 2.5) {
            while (! (hasNext(world, x, y, 4) && world[x][y] == Tileset.FLOOR)) {
                // reset starting point
                x = rand.nextInt(WIDTH);
                y = rand.nextInt(HEIGHT);
            }
            fillWithFLOOR(world, x, y, rand, n); // this is what `n` is for
        }
    }
    public static void clearEdges(TETile[][] world) {
        for (int x = 0; x < WIDTH; x ++) {
            world[x][0] = Tileset.NOTHING;
            world[x][HEIGHT - 1] = Tileset.NOTHING;
        }
        for (int y = 1; y < HEIGHT - 1; y ++) {
            world[0][y] = Tileset.NOTHING;
            world[WIDTH - 1][y] = Tileset.NOTHING;
        }
    }
    public static void addWALL(TETile[][] world) {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                if (world[x][y] == Tileset.FLOOR) {
                    while (hasNext(world, x, y, 8)) {
                        int[] coor = searchNext(world, x, y, new Random(), 8);
                        world[coor[0]][coor[1]] = Tileset.WALL;
                    }
                }
            }
        }
    }
    /**
     * Search for the not-NOTHING tile next to (x, y);
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     * @return coordinate (x, y) in int[2];
     */
    public static int[] searchNext(TETile[][] world, int x, int y, Random rand, int border) {
        int[] coor;
        do {
            coor = next(x, y, rand.nextInt(border));
        } while (!fillable(world, coor[0], coor[1], border == 4));
        return coor;
    }

    /**
     * Get the coordinate of a tile next to (x, y) relative to `r`;
     * @param r: int in range(0, 4);
     * @return coordinate (x, y) in int[2];
     */
    public static int[] next(int x, int y, int r) {
        int[] coor = new int[2];
        switch (r) {
            case 0: x ++; break;
            case 1: x --; break;
            case 2: y ++; break;
            case 3: y --; break;
            case 4: x ++; y ++; break;
            case 5: x ++; y --; break;
            case 6: x --; y --; break;
            case 7: x --; y ++; break;
        }
        coor[0] = x;
        coor[1] = y;
        return coor;
    }

    /**
     * Determine whether there is a not-NOTHING tile next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    public static boolean hasNext(TETile[][] world, int x, int y, int border) {
        boolean f = border == 4;

        int[] c0 = next(x, y, 0 % border);
        int[] c1 = next(x, y, 1 % border);
        int[] c2 = next(x, y, 2 % border);
        int[] c3 = next(x, y, 3 % border);
        /*
        When border == 4: the next four are identical with the ones above;
         */
        int[] c4 = next(x, y, 4 % border);
        int[] c5 = next(x, y, 5 % border);
        int[] c6 = next(x, y, 6 % border);
        int[] c7 = next(x, y, 7 % border);

        return fillable(world, c0[0], c0[1], f)
            || fillable(world, c1[0], c1[1], f)
            || fillable(world, c2[0], c2[1], f)
            || fillable(world, c3[0], c3[1], f)
            || fillable(world, c4[0], c4[1], f)
            || fillable(world, c5[0], c5[1], f)
            || fillable(world, c6[0], c6[1], f)
            || fillable(world, c7[0], c7[1], f);
    }

    /**
     *
     * @param f: set to `true` to narrow out the four edges (for filling FLOOR purposes);
     */
    public static boolean fillable(TETile[][] world, int x, int y, boolean f) {
        return inBound(x, y, f) && world[x][y] == Tileset.NOTHING;
    }

    /**
     * Determine whether a point is in the world;
     * @param f: set to `true` to narrow out the four edges (for filling FLOOR purposes);
     */
    public static boolean inBound(int x, int y, boolean f) {
        if (f) {
            return x > 0 && x < WIDTH - 1 && y > 0 && y < HEIGHT - 1;
        }
        return inBound(x, y);
    }
    public static boolean inBound(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
    /**
     * Return a world filled with NOTHING;
     */
    public static TETile[][] buildWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }
    public static void main (String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        long seed = 66666;
        TETile[][] w = buildWorld();
        randomWorld(w, seed);

        ter.renderFrame(w);
    }
}
