package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class WG {
    private static int WIDTH = Game.WIDTH;
    private static int startWIDTH = 0;
    private static int HEIGHT = Game.HEIGHT;
    private static int startHEIGHT = 0;
    private TETile[][] world;
    private Random rand;

    public WG(long seed) {
        world = new TETile[WIDTH][HEIGHT];
        newWorld();
        rand = new Random(seed);
        randomWorld();
    }
    public TETile[][] getWorld() {
        return world;
    }
    /**
     * Return a world filled with NOTHING;
     */
    private void newWorld() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    /**
     * Pseudo-randomly generate a world;
     */
    private void randomWorld() {
        int x = Math.floorDiv(WIDTH, 2);
        int y = Math.floorDiv(HEIGHT, 2);

        fillWithFLOOR(x, y);
        addWALL();
        addDOOR();
        replaceAll(Tileset.NOTHING, Tileset.NOTHING);
    }
    /**
     * First step: generating random FLOOR tiles.
     */
    private void fillWithFLOOR(int x, int y) {
        fillWithFLOOR(x, y, 0);
    }
    private void fillWithFLOOR(int x, int y, int n) {
        /*
        rule out the four edges;
        because the FLOOR must not be in the edges
        for aesthetic reasons;
         */
        startWIDTH ++;
        WIDTH --;
        startHEIGHT ++;
        HEIGHT --;
        /*
        fill with FLOORs;
         */
        fillWithFLOORHelper(x, y, n);
        /*
        restore the size of the world;
         */
        startWIDTH --;
        WIDTH ++;
        startHEIGHT --;
        HEIGHT ++;
    }
    private void fillWithFLOORHelper(int x, int y, int n) {
        // n: store the number of filled tiles;
        int[] coor;
        do {
            coor = searchNext(world, x, y, rand, 4); // find the next tile to fill;
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
                // reset starting point;
                x = rand.nextInt(WIDTH);
                y = rand.nextInt(HEIGHT);
            }
            fillWithFLOORHelper(x, y, n); // this is what `n` is for;
        }
    }

    /**
     * Clear the four edges;
     */
    private void clearEdges() {
        for (int x = 0; x < WIDTH; x ++) {
            world[x][0] = Tileset.NOTHING;
            world[x][HEIGHT - 1] = Tileset.NOTHING;
        }
        for (int y = 1; y < HEIGHT - 1; y ++) {
            world[0][y] = Tileset.NOTHING;
            world[WIDTH - 1][y] = Tileset.NOTHING;
        }
    }

    /**
     * Surround the FLOORs with WALLs (in 8 directions);
     */
    private void addWALL() {
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
     * Replace a random WALL with Door;
     * A Door must have at least one surrounding WALL (4-directioned)
     * for aesthetic purpose;
     * A Door also must be near to a FLOOR;
     */
    private void addDOOR() {
        int x, y;
        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (! (world[x][y] == Tileset.WALL
                && hasNext(world, x, y, 4, Tileset.FLOOR)
                && hasNext(world, x, y, 4, Tileset.WALL)));
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    /**
     * Replace every `original` in `world` with `tile`;
     * @param original: tile to be replaced;
     * @param tile: new tile to fill it;
     */
    private void replaceAll(TETile original, TETile tile) {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                if (world[x][y] == original) {
                    world[x][y] = tile;
                }
            }
        }
    }
    /**
     * Search for the NOTHING tile next to (x, y);
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     * @return coordinate (x, y) in type of int[2];
     */
    public static int[] searchNext(TETile[][] world, int x, int y, Random rand, int border) {
        return searchNext(world, x, y, rand, border, Tileset.NOTHING);
    }
    /**
     * Search for the `tile` next to (x, y);
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     * @return coordinate (x, y) in type of int[2];
     */
    public static int[] searchNext(TETile[][] world, int x, int y, Random rand, int border, TETile tile) {
        int[] coor;
        do {
            coor = next(x, y, rand.nextInt(border));
        } while (!fillable(world, coor[0], coor[1], tile));
        return coor;
    }
    /**
     * Get the coordinate of a tile next to (x, y) relative to `r`;
     * @param r: int in range(0, 8);
     * @return coordinate (x, y) in int[2];
     */
    public static int[] next(int x, int y, int r) {
        int[] coor = new int[2];
        switch (r) {
            case 0: x ++; break; // right
            case 1: x --; break; // left
            case 2: y ++; break; // up
            case 3: y --; break; // down
            case 4: x ++; y ++; break; // up right
            case 5: x ++; y --; break; // down right
            case 6: x --; y --; break; // down left
            case 7: x --; y ++; break; // up left
        }
        coor[0] = x;
        coor[1] = y;
        return coor;
    }

    /**
     * Determine whether there is a NOTHING tile next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    public static boolean hasNext(TETile[][] world, int x, int y, int border) {
        return hasNext(world, x, y, border, Tileset.NOTHING);
    }

    /**
     * Determine whether there is a `tile` next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     * @param tile: any tile object;
     */
    public static boolean hasNext(TETile[][] world, int x, int y, int border, TETile tile) {

        /*
        When border == 4: the last four of the loop will be identical
        to the first four;
         */
        for (int i = 0; i < 8; i ++) {
            if (hasDirec(world, x, y, i % border, tile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether (x, y) has a 'tile' next to it, in the specified direction;
     * @param direction: 0: right; 1: left; 2: up; 3: down;
     *                 4: up right; 5: down right; 6: down left; 7: up left;
     * @param tile: any tile object;
     * @return
     */
    public static boolean hasDirec(TETile[][] world, int x, int y, int direction, TETile tile) {
        int[] coor = next(x, y, direction);
        return fillable(world, coor[0], coor[1], tile);
    }
    /**
     * Determine whether a tile is NOTHING(available for filling);
     */
    public static boolean fillable(TETile[][] world, int x, int y) {
        return fillable(world, x, y, Tileset.NOTHING);
    }

    /**
     * Determine whether a tile is the specified `tile`;
     * @param tile: any tile object;
     */
    public static boolean fillable(TETile[][] world, int x, int y, TETile tile) {
        return inBound(x, y) && world[x][y] == tile;
    }

    /**
     * Determine whether a point is in the boundaries;
     */
    public static boolean inBound(int x, int y) {
        return x >= startWIDTH && x < WIDTH
                && y >= startHEIGHT && y < HEIGHT;
    }
}

