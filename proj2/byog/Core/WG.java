package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class WG {
    private static final int WIDTH = Game.WIDTH;
    private static final int HEIGHT = Game.HEIGHT;
    private TETile[][] world;
    private Random rand;

    public WG(long seed) {
        world = new TETile[WIDTH][HEIGHT];
        buildWorld();
        rand = new Random(seed);
        randomWorld();

    }
    public TETile[][] getWorld() {
        return world;
    }
    /**
     * Return a world filled with NOTHING;
     */
    private void buildWorld() {
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

        fillWithFLOOR(x, y, 0);
        addWALL();
        addDOOR();
        replaceAll(Tileset.NOTHING, Tileset.NOTHING);
    }
    private void fillWithFLOOR(int x, int y, int n) {
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
            fillWithFLOOR(x, y, n); // this is what `n` is for
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
     */
    private void addDOOR() {
        int x, y;
        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (! (world[x][y] == Tileset.WALL
                && hasNext(world, x, y, 4, Tileset.FLOOR)));
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
     * Determine whether there is a NOTHING tile next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    public static boolean hasNext(TETile[][] world, int x, int y, int border) {
        return hasNext(world, x, y, border, Tileset.NOTHING);
    }

    /**
     * Upgraded `hasNext`, specifying TETile;
     */
    public static boolean hasNext(TETile[][] world, int x, int y, int border, TETile tile) {
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

        return fillable(world, c0[0], c0[1], f, tile)
                || fillable(world, c1[0], c1[1], f, tile)
                || fillable(world, c2[0], c2[1], f, tile)
                || fillable(world, c3[0], c3[1], f, tile)
                || fillable(world, c4[0], c4[1], f, tile)
                || fillable(world, c5[0], c5[1], f, tile)
                || fillable(world, c6[0], c6[1], f, tile)
                || fillable(world, c7[0], c7[1], f, tile);
    }

    /**
     * Determine whether a tile is NOTHING(available for filling);
     * @param f: set to `true` to narrow out the four edges (for filling FLOOR purposes);
     */
    public static boolean fillable(TETile[][] world, int x, int y, boolean f) {
        return fillable(world, x, y, f, Tileset.NOTHING);
    }

    /**
     * Upgraded `fillable`, specifies TETile;
     */
    public static boolean fillable(TETile[][] world, int x, int y, boolean f, TETile tile) {
        return inBound(x, y, f) && world[x][y] == tile;
    }

    /**
     * Determine whether a point is in the boundaries;
     * Usually use `fillable` instead;
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
}

