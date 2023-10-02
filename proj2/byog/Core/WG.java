package byog.Core;

import byog.Core.Objects.*;
import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Objects.Headers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.*;

public class WG implements Serializable {
    public static int WIDTH = Game.WIDTH;
    public static int HEIGHT = Game.HEIGHT;
    public Random rand;

    /*
    Before saving, make a copy of changeable statics,
    because Serialization does not work for statics;
     */
    public static Place[][] places = new Place[WIDTH][HEIGHT];
    Place[][] Splaces;
    TETile[][] visibleWorld;

    /*
    What we keep track of:
     */
    public Door door;
    public Key key;
    public ArrayList<Player> players = new ArrayList <> ();
    public ArrayList <Thing> keepTrackOf = new ArrayList <Thing> (); // All Changeable, luminators;

    /*
    Set the number of things:
     */
    private static final double minFLOORCount = WIDTH * HEIGHT / 2.5;
    private static final int maxRoMos = 12;
    private static final int minLamps = 1;
    private static final int maxLamps = Math.max(Math.floorDiv(WIDTH * HEIGHT, 450), minLamps);
    private static final int numBreakableWalls = Math.floorDiv(WIDTH * HEIGHT, 400);

    /*
    For special uses:
     */
    public static int startWIDTH = 0;
    public static int startHEIGHT = 0;
    private static final double keyDoorDis = (WIDTH + HEIGHT) / 4;
    private static final double lampDis = (WIDTH + HEIGHT) / 10;

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

        fillWithFloor(places[xStart][yStart]); // First step: Randomly fill with FLOOR tiles;
        addWALL();
        door = addDOOR();
        key = addKEY();
        addPLAYER(f);

        /*
        Generate a random number of lamps;
         */
        for (int i = 0; i < rand.nextInt(minLamps, maxLamps + 1); i++) {
            addLAMP();
        }

        for (int i = 0; i < rand.nextInt(3, maxRoMos + 1); i++) {
            addRoMo();
        }

        addBreakableWall(numBreakableWalls);

        replaceAll(new Nothing(), new Nothing());

        luminateAll();
    }
    public WG(long seed) {
        rand = new Random(seed);

        randomWorld();
    }
    public WG() {
        rand = new Random();

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

    public void updTrack(Thing thing) {
        updArray(keepTrackOf, thing);
    }

    /**
     * When item not in array, add it, return 1;
     * When item is in array, remove it, return -1;
     * param currentCount We declare array size larger than we actually need,
     *                     this will prevent null-pointer exception;
     */
    public <T> void updArray(ArrayList <T> arr, T item) {
//        int f = contains(arr, item, currentCount);
//        if (f < 0) {
//            arr[currentCount] = item;
//            return 1;
//        } else {
//            if (f != currentCount - 1) {
//                arr[f] = arr[currentCount - 1];
//            }
//            arr[currentCount - 1] = null;
//            return -1;
//        }
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
     * Generate places;
     * Fill with NOTHING;
     */
    private void emptyWorld() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                places[x][y] = new Place(x, y, new Nothing());

            }
        }
    }
    /**
     * Method for renewing a WG world;
     */
    void renew(boolean f) {
        emptyWorld();
        if (f) {
            return; // First time generating: end here;
        }
        /*
        Renew arrays:
         */
        keepTrackOf = new ArrayList <Thing> ();
        keepTrackOf.addAll(players);
//        updTrack(player);
    }

    /**
     * Randomly fill with FLOOR tiles;
     * Number of FLOOR must be above minFLOORCount;
     */
    private void fillWithFloor(Place place) {
        fillWithFloor(place, 0);
    }
    private void fillWithFloor(Place place, int n) {
        /*
        rule out the four edges;
        because the FLOOR must not be in the edges
        for aesthetic reasons;
         */
        clearEdges();
        /*
        fill with FLOORs;
         */
        fillWithFloorHelper(place, n);
        /*
        restore the size of the world;
         */
        restoreEdges();
    }
    private void fillWithFloorHelper(Place place, int n) {
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
    }

    /**
     * Clear the four edges by narrowing the boundaries;
     */
    private void clearEdges() {
        startWIDTH ++;
        WIDTH --;
        startHEIGHT ++;
        HEIGHT --;
    }
    private void restoreEdges() {
        startWIDTH --;
        WIDTH ++;
        startHEIGHT --;
        HEIGHT ++;
    }
    /**
     * Replace every `original` in `world` with `tile`;
     * @param original: tile to be replaced;
     */
    private void replaceAll(Thing original, Thing replacer) {
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
    public Place randomSearchThing(Thing thing) {
        Place place;
        do {
            place = places[rand.nextInt(WIDTH)][rand.nextInt(HEIGHT)];
        } while (! place.nowIs(thing));
        return place;
    }

    /**
     * Surround the FLOORs with WALLs (in 8 directions);
     */
    private void addWALL() {
        addWALL(8);
    }
    private void addWALL(int sides) {
        Place place;
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {

                place = places[x][y];
                if (place.isFloor()) {
                    while (place.hasNextNothing(sides)) {
                        place.randomSearchNextNothing(rand, sides).fill(new Wall());
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
    private Door addDOOR() {
        Place place;
        do {
            place = places[rand.nextInt(WIDTH)][rand.nextInt(HEIGHT)];
        } while (! (place.nowIs(new Wall())
                && place.hasNextFloor(4)
                && place.hasNext(4, new Wall())));

        Door d = new Door(this, place);
        place.fill(d);

        return d;
    }
    /**
     * Replace a random FLOOR with KEY;
     * Use after the Door is added;
     * @return Position object of the KEY;
     */
    private Key addKEY() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validKEY(place));

        Key k = new Key(this, place);
        place.addNew(k);
        return k;
    }
    private boolean validKEY(Place pos) {
        return pos.distance(door.place) >= keyDoorDis;
    }
    private Player addPLAYER(boolean f) {
        Player p;
        Place place;
        if (f) {
            place = randomSearchFloor();
            p = new Player(this, place);
            place.addNew(p);
        } else { // use the existing player instance; update place status;
            for (Player player: players) {
                place = randomSearchFloor();
                player.place = place;
                place.addNew(player); // note that the renew method changed all places;
            }
            p = null;
        }

        return p;
    }
    private Lamp addLAMP() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validLamp(place));

        Lamp l = new Lamp(this, place);
        place.addNew(l);

        return l;
    }
    private boolean validLamp(Place place) {
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
    private RockMonster addRoMo() {
        Place place = randomSearchFloor();
        RockMonster rm = new RockMonster(this, place);
        place.addNew(rm);

        return rm;
    }

    private void addBreakableWall(int num) {
        assert num > 0;

        clearEdges(); // Breakable should not be in the edges;

        addBreakableHelper(num);

        restoreEdges();

    }
    private void addBreakableHelper(int num) {
        Place place;
        do {
            place = randomSearchThing(new Wall());
        } while (!
                (place.hasNext(4, new Wall()) && !place.hasNext(4, new Nothing()) &&
                        (place.hasNext(4, new Floor()) || place.hasNext(4, new BreakableWall()))
                )
        ); // should be next to a Wall, not next to Nothing, and should be next to a Floor or another Breakable;
        BreakableWall bw = new BreakableWall(this, place);

        place.fill(new Floor()); // when it's broken, it should be Floor, not Wall;
        place.addNew(bw);

        if (num > 1) {
            addBreakableWall(num - 1); // recursion, base case: num == 1;
        }
    }
}
