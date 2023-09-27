package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class WG {
    static int WIDTH = Game.WIDTH;
    static int HEIGHT = Game.HEIGHT;
    private TETile[][] world;
    private Random rand;

    Pos doorPos;
    Pos keyPos;
    Player player;

    /*
    For special usages:
     */
    static int startWIDTH = 0;
    static int startHEIGHT = 0;
    private static double minFLOORCount = WIDTH * HEIGHT / 2.5;
    private static double keyDoorDis = (WIDTH + HEIGHT) / 4;

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

        fillWithFLOOR(start); // First step: Randomly fill with FLOOR tiles;
        addWALL();
        doorPos = addDOOR();
        keyPos = addKEY();
        player = addPLAYER();
        replaceAll(Tileset.NOTHING, Tileset.NOTHING);
    }
    /**
     * Randomly fill with FLOOR tiles;
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
            pos = pos.searchNextNOTHING(world, rand, 4); // find the next tile to fill;
            world[pos.x][pos.y] = Tileset.FLOOR;
            n ++;
        } while (pos.hasNextNOTHING(world, 4));
         /*
        Inadequate FLOORs: try filling again;
         */
        if (n < minFLOORCount) {
            while (! (pos.hasNextNOTHING(world, 4) && pos.isFLOOR(world))) {
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
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {

                Pos pos = new Pos(x, y);
                if (pos.isFLOOR(world)) {
                    while (pos.hasNextNOTHING(world, border)) {
                        Pos p = pos.searchNextNOTHING(world, new Random(), border);
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
     * @return Position object of the DOOR;
     */
    private Pos addDOOR() {
        Pos pos;
        do {
            pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (! (pos.isTile(world, Tileset.WALL)
                && pos.hasNextFLOOR(world, 4)
                && pos.hasNext(world, 4, Tileset.WALL)));
        world[pos.x][pos.y] = Tileset.LOCKED_DOOR;
        return pos;
    }

    /**
     * Replace every `original` in `world` with `tile`;
     * @param original: tile to be replaced;
     * @param tile: new tile to fill it;
     */
    private void replaceAll(TETile original, TETile tile) {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                if (new Pos(x, y).isTile(world, original)) {
                    world[x][y] = tile;
                }
            }
        }
    }

    /**
     * Replace a random FLOOR with KEY;
     * @return Position object of the KEY;
     */
    private Pos addKEY() {
        Pos pos;
        do {
            pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (! (pos.isFLOOR(world)
                && pos.distance(doorPos) > keyDoorDis));

        world[pos.x][pos.y] = Tileset.FLOWER;
        return pos;
    }
    private Player addPLAYER() {
        Pos pos;
        do {
            pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (! pos.isFLOOR(world));

        world[pos.x][pos.y] = Tileset.PLAYER;
        return new Player(world, pos);
    }
}

