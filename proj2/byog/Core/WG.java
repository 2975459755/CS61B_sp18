package byog.Core;

import byog.Core.Objects.MultiBlock.MobileFlower;
import byog.Core.Objects.Supers.Interfaces.Changeable;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Objects.MultiBlock.Rectangle;
import byog.Core.Objects.MultiBlock.TwinRoMo;
import byog.Core.Objects.SingleBlock.*;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.*;

/**
 * World generation is seperated into Generator class;
 * But the generation methods have to be called in WG class,
 * because those objects only allow WG instance as parameter
 * (we cast this to WG in Generator methods);
 */
public class WG extends Generator {

    void randomWorld() {
        randomWorld(true);
    }
    /**
     * Pseudo-randomly generate a world;
     * @param firstWorld First time generate, insert true; otherwise false;
     */
    public void randomWorld(boolean firstWorld) {
        renew(firstWorld);

        // start point is at the center
        int xStart = Math.floorDiv(WIDTH, 2);
        int yStart = Math.floorDiv(HEIGHT, 2);

        // The first world is very small;
        int changeX = Math.floorDiv(Game.WIDTH * 25, 100);
        int changeY = Math.floorDiv(Game.HEIGHT * 25, 100);
        if (firstWorld) {
            changeWIDTH(- changeX);
            changeHEIGHT(- changeY);
        }
        setAll(); // set number of things based on current world size;

        fillWithFloor(places[xStart][yStart]); // First step: Randomly fill with FLOOR tiles;

        addTwinRoMo(); // TODO
        addMobileFlower(); // TODO

        addWall();
        door = addDoor();
        key = addKey();
        addPlayer(firstWorld, numPlayers);

        int numLamps = rand.nextInt(minLamps, maxLamps + 1);
        addLamp(numLamps);

        int numRoMos = rand.nextInt(minRoMos, maxRoMos + 1);
        addRoMo(numRoMos);

        addBreakableWall(numBreakableWalls);

        addFlower();

        // reset the world size;
        if (firstWorld) {
            changeWIDTH(changeX);
            changeHEIGHT(changeY);
        }

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
        placesForSave = places;

        WIDTH_FOR_SAVE = WIDTH;
        HEIGHT_FOR_SAVE = HEIGHT;
        startWIDTH_FOR_SAVE = startWIDTH;
        startHEIGHT_FOR_SAVE = startHEIGHT;
    }
    void load() {
        places = placesForSave;

        WIDTH = WIDTH_FOR_SAVE;
        HEIGHT = HEIGHT_FOR_SAVE;
        startWIDTH = startWIDTH_FOR_SAVE;
        startHEIGHT = startHEIGHT_FOR_SAVE;
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

        Thing[] a = luminators.toArray(new Thing[luminators.size()]);
        for (Thing t: a) {
            t.getPlace().luminate();
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
     * @return 0 if no changes;
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
        for (Thing t: luminators) {
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
        for (Player p: players) {
            if (!p.ghosted()) {
                // at least one player is not a ghost;
                return;
            }
        }

        endGame();
    }

    public void endGame() {
        StdDraw.pause(1200);
        System.exit(1);
    }

}

/**
 * Set data;
 */
class World implements Serializable {
    void getActualSize() {
        actualWIDTH = WIDTH - startWIDTH;
        actualHEIGHT = HEIGHT - startHEIGHT;
    }
    void setKeyDoorDis() {
        keyDoorDis = (int) Math.floorDiv(actualWIDTH + actualHEIGHT , 6);
    }
    void setLampDis() {
        lampDis = (int) Math.floorDiv(actualWIDTH + actualHEIGHT , 12);
    }
    void setMinFloorCount() {
        minFLOORCount = (int) Math.floorDiv(actualWIDTH * actualHEIGHT * 40, 100);
    }
    void setNumLamps() {
        minLamps = (int) Math.max(Math.floorDiv(actualWIDTH * actualHEIGHT , 1600), 1);
        maxLamps = (int) Math.max(Math.floorDiv(actualWIDTH * actualHEIGHT, 400), minLamps);
    }
    void setNumBreakable() {
        int minimun = 2;
        numBreakableWalls = (int) Math.max(Math.floorDiv(actualWIDTH * actualHEIGHT , 400), minimun);
    }
    void setNumRoMos() {
        minRoMos = (int) Math.max(Math.floorDiv(actualWIDTH * actualHEIGHT , 1000), 1);
        maxRoMos = (int) Math.max(Math.floorDiv(actualWIDTH * actualHEIGHT , 300), minRoMos);
    }
    World() {
        WIDTH = Game.WIDTH;
        HEIGHT = Game.HEIGHT;
        startWIDTH = 0;
        startHEIGHT = 0;

        setAll();
    }
    void setAll() {
        getActualSize();

        setKeyDoorDis();
        setLampDis();
        setMinFloorCount();
        setNumLamps();
        setNumBreakable();
        setNumRoMos();
    }
    public static int WIDTH;
    public static int HEIGHT;
    public static int startWIDTH;
    public static int startHEIGHT;
    protected int actualWIDTH;
    protected int actualHEIGHT;

    // For special uses:
    protected int keyDoorDis;
    protected int lampDis;
    protected int WIDTH_FOR_SAVE;
    protected int HEIGHT_FOR_SAVE;
    protected int startWIDTH_FOR_SAVE;
    protected int startHEIGHT_FOR_SAVE;

    // number of things:
    protected int minFLOORCount;
    protected int minRoMos;
    protected int maxRoMos;
    protected int minLamps;
    protected int maxLamps;
    protected int numBreakableWalls;


}

class Generator extends World {
    public Random rand;

    public Generator() {
        places = new Place[WIDTH][HEIGHT];
    }

    /*
    Before saving, make a copy of changeable statics,
    because Serialization does not work for statics;
     */
    public static Place[][] places;
    protected Place[][] placesForSave;
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
    public ArrayList <Changeable> keepTrackOf = new ArrayList <> (); // All Changeables;
    public ArrayList <Thing> luminators = new ArrayList<> ();

    /**
     * Generate places;
     * Fill with NOTHING;
     */
    protected void emptyWorld() {
        for (int x = 0; x < Game.WIDTH; x++) {
            for (int y = 0; y < Game.HEIGHT; y++) {

                places[x][y] = new Place(x, y, new Nothing());

            }
        }
    }
    /**
     * Method for renewing a world;
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
        luminators = new ArrayList<> ();
        luminators.addAll(players);
    }

    /**
     * Randomly fill with Floors;
     * Number of Floor must be above minFLOORCount;
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
                place = places[rand.nextInt(startWIDTH, WIDTH)][rand.nextInt(startHEIGHT, HEIGHT)];
            }
            fillWithFloorHelper(place, n);
        }
        numFloors = n;
    }

    /**
     * Clear the four edges by narrowing the boundaries;
     */
    protected void clearEdges(int n) {
        changeWIDTH(- n);
        changeHEIGHT(- n);
    }
    protected void restoreEdges(int n) {
        changeWIDTH(n);
        changeHEIGHT(n);
    }

    /**
     * n < 0: narrow; n > 0: broaden;
     */
    protected void changeWIDTH(int n) {
        startWIDTH -= n;
        WIDTH += n;
    }
    /**
     * n < 0: narrow; n > 0: broaden;
     */
    protected void changeHEIGHT(int n) {
        startHEIGHT -= n;
        HEIGHT += n;
    }
    /**
     * Replace every `original` in `with `replacer`;
     */
    protected void replaceAll(Thing original, Thing replacer) {
        for (int x = 0; x < Game.WIDTH; x ++) {
            for (int y = 0; y < Game.HEIGHT; y ++) {

                if (places[x][y].nowIs(original)) {
                    places[x][y].fill(replacer);  // TODO
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
            place = places[rand.nextInt(startWIDTH, WIDTH)][rand.nextInt(startHEIGHT, HEIGHT)];
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
        for (int x = startWIDTH; x < WIDTH; x ++) {
            for (int y = startHEIGHT; y < HEIGHT; y ++) {

                place = places[x][y];
                // Use originalWas: because tile might be covered with something;
                if (place.originalWas(new Floor())) {
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
     */
    protected Door addDoor() {
        Place place;
        do {
            place = places[rand.nextInt(startWIDTH, WIDTH)][rand.nextInt(startHEIGHT, HEIGHT)];
        } while (! (place.nowIs(new Wall())
                && place.hasNextFloor(4)
                && place.hasNext(4, new Wall()) >= 0));

        return new Door((WG)this, place);
    }
    /**
     * Use after the Door is added;
     */
    protected Key addKey() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validKey(place));

        return new Key((WG)this, place);
    }
    protected boolean validKey(Place pos) {
        return pos.LDistance(door.getPlace()) >= keyDoorDis;
    }
    protected Player addPlayer(boolean f, int number) {
        Player p;
        Place place;
        if (f) {
            players = new ArrayList<> ();
            // add first player;
            place = randomSearchFloor();
            p1 = new Player((WG)this, place);
            p = p1;

            if (number > 1) {
                // add second player;
                place = randomSearchFloor();
                p2 = new Player((WG)this, place);
            }
        } else { // use the existing player instances; update status;
            for (Player player: players) {
                player.newWorld();
            }
            p = null;
        }

        return p;
    }
    protected void addLamp(int n) {
        Place place;
        do {
            place = randomSearchFloor();
        } while (! validLamp(place));

        new Lamp((WG)this, place);

        if (n > 1) {
            addLamp(n - 1);
        }
    }
    protected boolean validLamp(Place place) {
        // lamps should not be too close to each other
        Thing[] a = luminators.toArray(new Thing[luminators.size()]);
        for (Thing lumi: a) {
            if (lumi instanceof Lamp l) {
                if (place.LDistance(l.getPlace()) < lampDis) {
                    return false;
                }
            }
        }
        return true;
    }
    protected void addRoMo(int n) {
        Place place = randomSearchFloor();
        new RockMonster((WG)this, place);

        if (n > 1) {
            addRoMo(n - 1);
        }
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

        place.fill(new Floor()); // when it's broken, it should be Floor, not Wall;
        new BreakableWall((WG)this, place);

        if (num > 1) {
            addBreakableWall(num - 1); // recursion, base case: num == 1;
        }
    }

    protected void addTwinRoMo() {
        Place start;
        Rectangle rect;
        do {
            start = randomSearchFloor();
            rect = new Rectangle(start, 2, 1);
        } while (!rect.valid());

        rect.fillWith(new Floor());
        new TwinRoMo((WG) this, rect);
    }

    protected void addFlower() {
        Place place;
        do {
            place = randomSearchFloor();
        } while (place.hasNext(4, new Wall()) != -1);
        new Flower((WG)this, place);
    }

    protected void addMobileFlower() {
        Place start;
        Rectangle rect;
        do {
            start = randomSearchFloor();
            rect = new Rectangle(start, 3, 3);
        } while (!rect.valid());

        rect.fillWith(new Floor());
        new MobileFlower((WG) this, rect);
    }
}

