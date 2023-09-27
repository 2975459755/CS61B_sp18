package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class WG {
    private static int WIDTH = Game.WIDTH;
    private static int HEIGHT = Game.HEIGHT;
    private TETile[][] world;
    private Random rand;

    /*
    For special uses:
     */
    private static int startWIDTH = 0;
    private static int startHEIGHT = 0;
    private static double minFLOORCount = WIDTH * HEIGHT / 2.5;


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
        Pos start = new Pos
                (Math.floorDiv(WIDTH, 2), Math.floorDiv(HEIGHT, 2));

        fillWithFLOOR(start);
        addWALL();
        addDOOR();
        replaceAll(Tileset.NOTHING, Tileset.NOTHING);
    }
    /**
     * First step: Randomly fill with FLOOR tiles;
     * Number of FLOOR must be above minFLOORCount;
     */
    private void fillWithFLOOR(Pos pos) {
        fillWithFLOOR(pos, 0);
    }
    private void fillWithFLOOR(Pos pos, int n) {
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
        fillWithFLOORHelper(pos, n);
        /*
        restore the size of the world;
         */
        startWIDTH --;
        WIDTH ++;
        startHEIGHT --;
        HEIGHT ++;
    }
    private void fillWithFLOORHelper(Pos pos, int n) {
        // n: store the number of filled tiles;
        do {
            pos = searchNextNOTHING(world, pos, rand, 4); // find the next tile to fill;
            world[pos.x][pos.y] = Tileset.FLOOR;
            n ++;
        } while (hasNextNOTHING(world, pos, 4));
         /*
        Inadequate FLOORs: try filling again;
         */
        if (n < minFLOORCount) {
            while (! (hasNextNOTHING(world, pos, 4) && isFLOOR(world, pos))) {
                // reset starting point;
                pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
            }
            fillWithFLOORHelper(pos, n); // this is what `n` is for;
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
        addWALL(8);
    }
    private void addWALL(int border) {
        Pos pos;
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                pos = new Pos(x, y);
                if (isFLOOR(world, pos)) {
                    while (hasNextNOTHING(world, pos, border)) {
                        Pos p = searchNextNOTHING(world, pos, new Random(), border);
                        world[p.x][p.y] = Tileset.WALL;
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
        Pos pos;
        do {
            pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (! (isTile(world, pos, Tileset.WALL)
                && hasNextFLOOR(world, pos, 4)
                && hasNext(world, pos, 4, Tileset.WALL)));
        world[pos.x][pos.y] = Tileset.LOCKED_DOOR;
    }

    /**
     * Replace every `original` in `world` with `tile`;
     * @param original: tile to be replaced;
     * @param tile: new tile to fill it;
     */
    private void replaceAll(TETile original, TETile tile) {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                if (isTile(world, new Pos(x, y), original)) {
                    world[x][y] = tile;
                }
            }
        }
    }
    /**
     * Search for a random NOTHING tile next to `pos`;
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     */
    public static Pos searchNextNOTHING(TETile[][] world, Pos pos, Random rand, int border) {
        return searchNext(world, pos, rand, border, Tileset.NOTHING);
    }
    /**
     * Search for a random `tile` next to `pos`;
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     */
    public static Pos searchNext(TETile[][] world, Pos pos, Random rand, int border, TETile tile) {
        Pos p;
        do {
            p = next(pos, rand.nextInt(border));
        } while (!isTile(world, p, tile));
        return p;
    }
    /**
     * Determine whether there is a NOTHING tile next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    public static boolean hasNextNOTHING(TETile[][] world, Pos pos, int border) {
        return hasNext(world, pos, border, Tileset.NOTHING);
    }
    /**
     * Determine whether there is a FLOOR tile next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    public static boolean hasNextFLOOR(TETile[][] world, Pos pos, int border) {
        return hasNext(world, pos, border, Tileset.FLOOR);
    }

    /**
     * Determine whether there is a `tile` next to (x, y);
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     * @param tile: any tile object;
     */
    public static boolean hasNext(TETile[][] world, Pos pos, int border, TETile tile) {

        /*
        When border == 4: the last four of the loop will be identical
        to the first four;
         */
        for (int i = 0; i < 8; i ++) {
            if (hasDirec(world, pos, i % border, tile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether 'pos' has a 'tile' next to it, in the specified direction;
     * @param direction: 0: right; 1: left; 2: up; 3: down;
     *                 4: up right; 5: down right; 6: down left; 7: up left;
     * @param tile: any tile object;
     */
    public static boolean hasDirec(TETile[][] world, Pos pos, int direction, TETile tile) {
        Pos p = next(pos, direction);
        return isTile(world, p, tile);
    }
    /**
     * Get the coordinate of a tile next to (x, y) relative to `r`;
     * @param r: int in range(0, 8);
     */
    public static Pos next(Pos pos, int r) {
        int x = pos.x;
        int y = pos.y;
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
        return new Pos(x, y);
    }
    public static boolean isFLOOR(TETile[][] world, Pos pos) {
        return isTile(world, pos, Tileset.FLOOR);
    }
    public static boolean isNOTHING(TETile[][] world, Pos pos) {
        return isTile(world, pos, Tileset.NOTHING);
    }

    /**
     * Determine whether 'pos' is the specified `tile`;
     * @param tile: any tile object;
     */
    public static boolean isTile(TETile[][] world, Pos pos, TETile tile) {
        return inMap(pos) && world[pos.x][pos.y] == tile;
    }

    /**
     * Determine whether a point is in the boundaries;
     */
    public static boolean inMap(Pos pos) {
        int x = pos.x;
        int y = pos.y;

        return x >= startWIDTH && x < WIDTH
                && y >= startHEIGHT && y < HEIGHT;
    }
}

