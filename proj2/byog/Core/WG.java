package byog.Core;

import byog.Core.Objects.*;
import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Objects.Headers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.*;

public class WG extends Generator {
    /**
     * World generation is seperated into Generator class;
     * But the generation methods have to be called in WG class,
     * because those objects only allow WG instance as parameter
     * (we cast this to WG in Generator methods);
     */
    // This class is all about how WG _interacts_ with the game;
    void randomWorld() {
        randomWorld(true);
    }
    /**
     * Pseudo-randomly generate a world;
     * @param f First time generate, insert true; otherwise false;
     */
    public void randomWorld(boolean f) {
        renew(f);

        int xStart = Math.floorDiv(WIDTH, 2);
        int yStart = Math.floorDiv(HEIGHT, 2); // start point is at the center

        if (f) {
            clearEdges(Math.floorDiv(Game.HEIGHT, 5));
        }
        fillWithFloor(places[xStart][yStart]); // First step: Randomly fill with FLOOR tiles;
        if (f) {
            restoreEdges(Math.floorDiv(Game.HEIGHT, 5));
        }

        addWall();
        door = addDoor();
        key = addKey();
        addPlayer(f, numPlayers);

        /*
        Generate a random number of lamps;
         */
        for (int i = 0; i < rand.nextInt(minLamps, maxLamps + 1); i++) {
            addLamp();
        }

        for (int i = 0; i < rand.nextInt(3, maxRoMos + 1); i++) {
            addRoMo();
        }

        addBreakableWall(numBreakableWalls);

        replaceAll(new Nothing(), new Nothing());

        luminateAll();
    }
    public WG(long seed, int players) {
        rand = new Random(seed);
        numPlayers = players;

        randomWorld();
    }
    public WG(int players) {
        rand = new Random();
        numPlayers = players;

        randomWorld();
    }

    void save() {
        Splaces = places;
    }
    void load() {
        places = Splaces;
    }

    TETile[][] getVisible() {
        return visibleWorld;
    }

    /**
     * Given that visible is properly updated,
     * update visible world in accordance to that;
     */
    void updateVisible() {
        visibleWorld = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i ++) {
            for (int j = 0; j < HEIGHT; j++) {

                visibleWorld[i][j] = places[i][j].getVisible();

            }
        }
    }

    /**
     * Iterate through all luminators in the world,
     * and luminate;
     */
    void luminateAll() {
        for (int i = 0; i < WIDTH; i ++) {
            for (int j = 0; j < HEIGHT; j++) { // reset visible to all false;

                places[i][j].visible = false;

            }
        }

        Thing[] a = keepTrackOf.toArray(new Thing[keepTrackOf.size()]);
        for (Thing t: a) {
            t.place.luminate();
        }

        updateVisible();
    }

    /**
     * Use MTs array to update the intervals of all existing Changeable;
     */
    void update() {
        update(Game.miniInterval);
    }
    <T> void update(T t) {
        Thing[] a = keepTrackOf.toArray(new Thing[keepTrackOf.size()]);
        for (Thing thing: a) {
            if (thing instanceof Changeable c) {
                c.update(t);
            }
        }
    }

    /**
     * When item not in array, add it, return 1;
     * When item is in array, remove it, return -1;
     * param currentCount We declare array size larger than we actually need,
     *                     this will prevent null-pointer exception;
     */
    @Deprecated
    public <T> void updArray(ArrayList <T> arr, T item) {
        if (arr.contains(item)) {
            arr.remove(item);
        } else {
            arr.add(item);
        }
    }
    /**
     * bound is exclusive;
     */
    @Deprecated
    static <T> int contains(T[] arr, T item, int bound) {
        for (int i = 0; i < bound; i ++) {
            if (arr[i].equals(item)) { // use .equals!
                return i;
            }
        }
        return -1;
    }

    boolean canPlayerAct() {
        for (Player p: players) {
            if (p.canAct()) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    void playerAct(String input) {
        Player[] a = players.toArray(new Player[players.size()]);
        for (Player p: a) {
            p.act(input);
        }
    }

    /**
     * All Changeable take change, if they do;
     */
    int checkChange() {
        int ret = 0;
        Thing[] a = keepTrackOf.toArray(new Thing[keepTrackOf.size()]);
        for (Thing thing: a) {
            if (thing instanceof Changeable c) {
                ret += c.change();
            }
        }
        return ret;
    }

    /**
     * When a lamp is interacted, check if all lamps are lit;
     * If so, reflesh the ghost;
     */
    public void checkAllLamps() {
        for (Thing t: keepTrackOf) {
            if ((t instanceof Lamp l) && (!l.isLitUp())) {
                return;
            }
        }
        // All lamps are lit;
        for (Player p: players) {
            if (p.ghosted() && !p.dead()) { // if it's dead, it is not a true ghost;
                p.reflesh();
            }
        }
    }

    /**
     * This method is invoked by player.change() in every game loop;
     */
    public void checkGameOver() {
        boolean f = false;
        for (Player p: players) {
            if (!p.ghosted()) {
                // at least one player is not a ghost;
                f = true;
                break;
            }
        }
        if (!f) {
            endGame();
        }
    }

    public void endGame() {
        System.exit(1);
    }

}

class World implements Serializable {
    // Store static data;
    public static int WIDTH = Game.WIDTH;
    public static int HEIGHT = Game.HEIGHT;
    public static int startWIDTH = 0;
    public static int startHEIGHT = 0;
    protected static double keyDoorDis = (WIDTH + HEIGHT) / 4;
    protected static double lampDis = (WIDTH + HEIGHT) / 10;

    // number of things:
    protected static double minFLOORCount = WIDTH * HEIGHT / 2.5;
    protected static int maxRoMos = 6;
    protected static final int minLamps = 1;
    protected static int maxLamps = Math.max(Math.floorDiv(WIDTH * HEIGHT, 450), minLamps);
    protected static int numBreakableWalls = Math.floorDiv(WIDTH * HEIGHT, 400);

}

class Generator extends World {
    public Random rand;

    /*
    Before saving, make a copy of changeable statics,
    because Serialization does not work for statics;
     */
    public static Place[][] places = new Place[WIDTH][HEIGHT];
    protected Place[][] Splaces;
    protected TETile[][] visibleWorld;

    protected int numFloors = 0;

    /*
    What we keep track of:
     */
    public Door door;
    public Key key;
    public Player p1;
    public Player p2;
    public int numPlayers;
    public ArrayList <Player> players = new ArrayList <> ();
    public ArrayList <Thing> keepTrackOf = new ArrayList <> (); // All Changeable, luminators;

    /**
     * Generate places;
     * Fill with NOTHING;
     */
    protected void emptyWorld() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                places[x][y] = new Place(x, y, new Nothing());

            }
        }
    }
    /**
     * Method for renewing a WG world;
     */
    protected void renew(boolean f) {
        emptyWorld();
        if (f) {
            return; // First time generating: end here;
        }
        /*
        Renew arrays:
         */
        keepTrackOf = new ArrayList <> ();
        keepTrackOf.addAll(players);
    }

    /**
     * Randomly fill with FLOOR tiles;
     * Number of FLOOR must be above minFLOORCount;
     */
    protected void fillWithFloor(Place place) {
        fillWithFloor(place, numFloors);
    }
    protected void fillWithFloor(Place place, int n) {
        /*
        rule out the four edges;
        because the FLOOR must not be in the edges
        for aesthetic reasons;
         */
        clearEdges(1);

        keyDoorDis = (WIDTH - startWIDTH + 1 + HEIGHT - startHEIGHT + 1) / 4;
        lampDis = (WIDTH - startWIDTH + 1 + HEIGHT - startHEIGHT + 1) / 10;
        minFLOORCount = (WIDTH - startWIDTH + 1) *  (HEIGHT - startHEIGHT + 1) / 2.5;
        maxLamps = Math.max(Math.floorDiv((WIDTH - startWIDTH + 1) *  (HEIGHT - startHEIGHT + 1), 450), minLamps);
        numBreakableWalls = Math.floorDiv((WIDTH - startWIDTH + 1) *  (HEIGHT - startHEIGHT + 1), 400);

        /*
        fill with FLOORs;
         */
        fillWithFloorHelper(place, n);
        /*
        restore the size of the world;
         */
        restoreEdges(1);
    }
    protected void fillWithFloorHelper(Place place, int n) {
        // n: store the number of filled tiles;
        do {
            place = place.randomSearchNextNothing(rand, 4); // find the next place to fill;
            place.fill(new Floor()); // fill the place with a new Floor instance;
            n ++;
        } while (place.hasNextNothing(4));
         /*
        Inadequate Floors: try filling again;
         */
        if (n < minFLOORCount) {
            while (! (place.hasNextNothing(4) && place.isFloor())) {
                // reset starting point;
                place = places[rand.nextInt(WIDTH)][rand.nextInt(HEIGHT)];
            }
            fillWithFloorHelper(place, n); // this is what `n` is for;
        }
        numFloors = n;
    }

    /**
     * Clear the four edges by narrowing the boundaries;
     */
    protected void clearEdges(int n) {
        startWIDTH += n;
        WIDTH -= n;
        startHEIGHT += n;
        HEIGHT -= n;
    }
    protected void restoreEdges(int n) {
        startWIDTH -= n;
        WIDTH += n;
        startHEIGHT -= n;
        HEIGHT += n;
    }
    /**
     * Replace every `original` in `world` with `tile`;
     * @param original: tile to be replaced;
     */
    protected void replaceAll(Thing original, Thing replacer) {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {

                if (places[x][y].nowIs(original)) {
                    places[x][y].fill(replacer);
                }

            }
        }
    }
    public Place randomSearchFloor() {
        return randomSearchThing(new Floor());
    }
    public  Place randomSearchThing(Thing thing) {
        Place place;
        do {
            place = places[rand.nextInt(WIDTH)][rand.nextInt(HEIGHT)];
        } while (! place.nowIs(thing));
        return place;
    }

    /**
     * Surround the FLOORs with WALLs (in 8 directions);
     */
    protected void addWall() {
        addWall(8);
    }
    protected void addWall(int sides) {
        Place place;
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {

                place = places[x][y];
                if (place.isFloor()) {
                    int direc;
                    while (true) {
                        direc = place.hasNext(sides, new Nothing());
                        if (direc < 0) {
                            break;
                        } else {
                            place.next(direc).fill(new Wall());
                        }
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
    protected Door addDoor() {
        Place place;
        do {
            place = places[rand.nextInt(WIDTH)][rand.nextInt(HEIGHT)];
        } while (! (place.nowIs(new Wall())
                && place.hasNextFloor(4)
                && place.hasNext(4, new Wall()) >= 0));

        Door d = new Door((WG)this, place);
        place.fill(d);

        return d;
    }
    /**
     * Replace a random FLOOR with KEY;
     * Use after the Door is added;
     * @return Position object of the KEY;
     */
    protected Key addKey() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validKey(place));

        Key k = new Key((WG)this, place);
        place.addNew(k);
        return k;
    }
    protected boolean validKey(Place pos) {
        return pos.distance(door.place) >= keyDoorDis;
    }
    protected Player addPlayer(boolean f, int number) {
        Player p;
        Place place;
        if (f) {
            players = new ArrayList<> ();
            // add first player;
            place = randomSearchFloor();
            p = new Player((WG)this, place);
            place.addNew(p);

            p1 = p;
            if (number > 1) {
                // add second player;
                place = randomSearchFloor();
                p = new Player((WG)this, place);
                place.addNew(p);

                p2 = p;
            }
        } else { // use the existing player instances; update status;
            for (Player player: players) {
                player.newWorld();
            }
            p = null;
        }

        return p;
    }
    protected Lamp addLamp() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validLamp(place));

        Lamp l = new Lamp((WG)this, place);
        place.addNew(l);

        return l;
    }
    protected boolean validLamp(Place place) {
        // lamps should not be too close to each other
        Thing[] a = keepTrackOf.toArray(new Thing[keepTrackOf.size()]);
        for (Thing lumi: a) {
            if (lumi instanceof Lamp l) {
                if (place.LDistance(l.place) < lampDis) {
                    return false;
                }
            }
        }
        return true;
    }
    protected RockMonster addRoMo() {
        Place place = randomSearchFloor();
        RockMonster rm = new RockMonster((WG)this, place);
        place.addNew(rm);

        return rm;
    }

    protected void addBreakableWall(int num) {
        assert num > 0;

        clearEdges(1); // Breakable should not be in the edges;

        addBreakableHelper(num);

        restoreEdges(1);

    }
    protected void addBreakableHelper(int num) {
        Place place;
        do {
            place = randomSearchThing(new Wall());
        } while (!
                (place.hasNext(4, new Wall()) >= 0 && !place.hasNextNothing(4) &&
                        (place.hasNextFloor(4) || place.hasNext(4, new BreakableWall()) >= 0)
                )
        ); // should be next to a Wall, not next to Nothing, and should be next to a Floor or another Breakable;
        BreakableWall bw = new BreakableWall((WG)this, place);

        place.fill(new Floor()); // when it's broken, it should be Floor, not Wall;
        place.addNew(bw);

        if (num > 1) {
            addBreakableWall(num - 1); // recursion, base case: num == 1;
        }
    }
}

