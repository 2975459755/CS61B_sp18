package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class WG {
    static int WIDTH = Game.WIDTH;
    static int HEIGHT = Game.HEIGHT;
    TETile[][] world;
    boolean[][] isVisible;
    TETile[][] visible;
    Random rand;

    static Pos doorPos;
    static Pos keyPos;
    private static final int minLamps = 2;
    private static final int maxLamps = Math.max(Math.floorDiv(WIDTH * HEIGHT, 450), minLamps);
    static Pos[] Lamps = new Pos[maxLamps];
    static int numLamps = 0;

    /*
    MovingThings:
     */
    static Player player;
    static final int maxNumRoMo = 1;
    static RockMonster[] RoMos = new RockMonster[maxNumRoMo];
    static int numRoMo = 0;

    static MovingThings[] MTs = new MovingThings[100]; // Keep track of all MovingThings
    static int moving = 0; // the number of existing MovingThings

    /*
    For special uses:
     */
    static int startWIDTH = 0;
    static int startHEIGHT = 0;
    private static final double minFLOORCount = WIDTH * HEIGHT / 2.5;
    private static final double keyDoorDis = (WIDTH + HEIGHT) / 4;
    private static final double lampDis = (WIDTH + HEIGHT) / 10;

    WG(long seed) {
        world = new TETile[WIDTH][HEIGHT];
        visible = new TETile[WIDTH][HEIGHT];

        rand = new Random(seed);

        randomWorld();
    }
    WG() {
        world = new TETile[WIDTH][HEIGHT];
        visible = new TETile[WIDTH][HEIGHT];

        rand = new Random();

        randomWorld();
    }
    public TETile[][] getVisible() {
        return visible;
    }

    /**
     * Given that `isVisible` is properly updated,
     * update `Visible` in accordance to that;
     */
    void updateVisible() {
        for (int i = startWIDTH; i < WIDTH; i ++) {
            for (int j = startHEIGHT; j < HEIGHT; j ++) {
                if (isVisible[i][j]) {
                    visible[i][j] = world[i][j];
                } else {
                    visible[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Iterate through all luminators in the world,
     * and luminate;
     */
    void luminateAll() {
        isVisible = new boolean[WIDTH][HEIGHT]; // reset isVisible to all false;

        for (int x = startWIDTH; x < WIDTH; x ++) {
            for (int y = startHEIGHT; y < HEIGHT; y ++) {
                Pos pos = new Pos(x, y);
                if (pos.isLuminator(world) > 0) {
                    pos.luminate(this);
                }
            }
        }

        updateVisible();
    }

    /**
     * Use MTs array to update the intervals of all existing MovingThings;
     */
    void update() {
        for (int i = 0; i < moving; i ++) {
            MTs[i].update();
        }
    }

    /**
     * Add a new MovingThings to array MTs, so that we can track it;
     * Increment `moving`;
     */
    void updateMTs(MovingThings m) {
        MTs[moving] = m;
        moving ++;
    }

    /**
     * Fill the `world` with NOTHING;
     */
    private void emptyWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    /**
     * Pseudo-randomly generate a world;
     */
    void randomWorld() {
        renew();

        Pos start = new Pos
                (Math.floorDiv(WIDTH, 2), Math.floorDiv(HEIGHT, 2)); // start point is at the center

        fillWithFLOOR(start); // First step: Randomly fill with FLOOR tiles;
        addWALL();
        doorPos = addDOOR();
        keyPos = addKEY();
        player = addPLAYER();

        /*
        Generate a random number of lamps;
         */
        numLamps = 0;
        for (int i = 0; i < rand.nextInt(minLamps, maxLamps + 1); i++) {
            Lamps[i] = addLAMP();
        }

        RoMos[0] = addRoMo();

        replaceAll(Tileset.NOTHING, Tileset.NOTHING);

        luminateAll();
    }

    /**
     * Method for renewing a WG world;
     */
    void renew() {
        isVisible = new boolean[WIDTH][HEIGHT];

        emptyWorld(world);
        emptyWorld(visible);

        /*
        Renew MTs;
         */
        int players = 0;
        for (int i = 0; i < moving; i ++) {
            if (MTs[i] instanceof Player) {
                MTs[players] = MTs[i];
                players ++;
            }
            if (i != players - 1) {
                MTs[i] = null;
            }
        }
        moving = players;
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

    private Pos searchFLOOR() {
        return searchTile(Tileset.FLOOR);
    }
    private Pos searchTile(TETile tile) {
        Pos pos;
        do {
            pos = new Pos(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (! pos.isTile(world, tile));
        return pos;
    }
    /**
     * Replace a random FLOOR with KEY;
     * @return Position object of the KEY;
     */
    private Pos addKEY() {
        Pos pos;
        do {
            pos = searchFLOOR();
        } while (! validKEY(pos));
        world[pos.x][pos.y] = Tileset.FLOWER;
        return pos;
    }
    private boolean validKEY(Pos pos) {
        return pos.distance(doorPos) >= keyDoorDis;
    }
    private Player addPLAYER() {
        Pos pos = searchFLOOR();
        world[pos.x][pos.y] = Tileset.PLAYER;

        Player player = new Player(this, pos);
        updateMTs(player);

        return player;
    }
    private Pos addLAMP() {
        Pos pos;
        do {
            pos = searchFLOOR();
        } while (! validLamp(pos));
        world[pos.x][pos.y] = Tileset.LAMP_UNLIT;

        numLamps ++;
        return pos;
    }
    private boolean validLamp(Pos pos) {
        // lamps should not be too close to each other
        for (int i = 0; i < numLamps; i ++) {
            if (pos.LDistance(Lamps[i]) < lampDis) {
                return false;
            }
        }
        return true;
    }
    private RockMonster addRoMo() {
        Pos pos = searchFLOOR();
        world[pos.x][pos.y] = Tileset.MOUNTAIN;

        RockMonster rm = new RockMonster(this, pos);
        updateMTs(rm);

        return rm;
    }
}

