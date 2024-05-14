package byog.Core;

import byog.Core.Objects.SingleBlock.Floor;
import byog.Core.Objects.Supers.Interfaces.Collectable;
import byog.Core.Objects.SingleBlock.Nothing;
import byog.Core.Objects.Supers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static jdk.dynalink.linker.support.Guards.isInstance;
import static byog.Core.Utils.*;


public class Place extends Pos {

    public boolean visible;
    protected ArrayList<Thing> layers;

    public Place(int xCoor, int yCoor, Thing thing) {
        x = xCoor;
        y = yCoor;
        fill(thing);
    }
    public Place(int xCoor, int yCoor) {
        x = xCoor;
        y = yCoor;
    }
    public Place(Thing thing) {
        fill(thing);
    }

    /**
     * Make the place originalWas some Thing;
     */
    public void fill(Thing thing) {
        layers = new ArrayList<> ();
        layers.add(thing);
        layers.trimToSize();
    }
    /**
     * Make the place nowIs some Thing;
     */
    public void addNew(Thing thing) {
        layers.add(thing);
    }

    public boolean contains(Thing thing) {
        return layers.contains(thing);
    }

    /**
     * Restore the place back to what it originalWas;
     */
    @Deprecated
    public void restore() {
        layers.removeIf(t -> layers.indexOf(t) > 0); // remove all but the first element;
    }

    /**
     * Remove one element from layers;
     */
    public void remove(Thing thing) {
        layers.remove(thing);
    }

    public TETile getVisible() {
        if (visible) {
            return getPresent().avatar(); // get the top of layers, return the avatar;
        }
        return Nothing.avatar;
    }
    public boolean collectable() {
        return getPresent().isCollectable();
    }

    /**
     * Collect all collectables from top to bottom, until the first un-collectable;
     * Use after collectable();
     */
    public void collectBy(Thing thing) {
        while (layers.size() > 1) {
            if (getPresent() instanceof Collectable c) {
                c.collectedBy(thing);
            } else {
                break;
            }
        }
    }

    /**
     * A place is considered canEnter if the top is an obstacle;
     */
    public boolean canEnter() {
        return !getPresent().isObstacle();
    }

    /**
     * Touch the top layer Thing;
     */
    public void touchedBy(Thing thing) {
        getPresent().touchedBy(thing);
    }

    /**
     * Return the largest lumi-range among all layers;
     */
    public int isLuminator() {
        int res = -1;
        for (Thing t: layers) {
            if (t.isLuminator() > res) {
                res = t.isLuminator();
            }
        }
        return res;
    }

    /**
     * Luminate all surrounding places within lumi-range;
     */
    public void luminate() {
        luminate(isLuminator());
    }
    public void luminate(int radius) {
        if (radius < 0) {
            return;
        }
        for (int i = x - radius; i <= x + radius; i ++) {
            for (int j = y - radius; j <= y + radius; j ++) {

                if (new Pos(i, j).inMap()) {
                    WG.places[i][j].visible = true;
                }

            }
        }
    }

    public Place randomSearchNextNothing(Random rand, int sides) {
        return randomSearchNext(rand, sides, new Nothing());
    }
    public Place randomSearchNext(Random rand, int sides, Thing thing) {
        Place p;
        do {
            p = next(rand.nextInt(sides));
        } while (p == null || !p.nowIs(thing));
        return p;
    }
    public boolean hasNextNothing(int sides) {
        return hasNext(sides, new Nothing()) >= 0;
    }
    public boolean hasNextFloor(int sides) {
        return hasNext(sides, new Floor()) >= 0;
    }

    /**
     * If the place is next to a Thing, return the direction;
     * Otherwise return -1;
     */
    public int hasNext(int sides, Thing thing) {
        for (int i = 0; i < sides; i ++) {
            if (hasDirec(i, thing)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Whether a place has something next to it in the specified direction;
     */
    public boolean hasDirec(int direc, Thing thing) {
        Place next = next(direc);
        return next != null && next.nowIs(thing);
    }

    /**
     * @param direct 0: right; 1 : left; 2: up; 3: down;
     *               4: ↑→; 5: ↓←; 6: ↓→; 7: ↑←;
     * @return null if coordinate is out of bounds;
     */
    public Place next(int direct) {
        int xC = x;
        int yC = y;
        switch (direct) {
            case Right: xC ++; break; // right
            case Left: xC --; break; // left
            case Up: yC ++; break; // up
            case Down: yC --; break; // down
            case UpRight: xC ++; yC ++; break; // up right
            case DownLeft: xC --; yC --; break; // down left
            case DownRight: xC ++; yC --; break; // down right
            case UpLeft: xC --; yC ++; break; // up left
        }

        if (new Place(xC, yC).inMap()) {
            return WG.places[xC][yC];
        }
        return null;
    }
    /**
     * Determine whether the top layer is a Floor;
     */
    public boolean isFloor() {
        return nowIs(new Floor(null));
    }

    /**
     * Determine whether the bottom layer is a subtype instance of the specified thing;
     */
    public boolean originalWas(Thing thing) {
        return thing.getClass().isInstance(getIntrinsic());
    }

    /**
     * Determine whether the top layer is a subtype instance of the specified thing;
     */
    public boolean nowIs(Thing thing) {
        if (layers != null) {
            return thing.getClass().isInstance(getPresent());
        }
        return false;
    }
    /**
     *
     * @return the top of the layers;
     */
    public Thing getPresent() {
        return layers.get(layers.size() - 1);
    }

    /**
     *
     * @return the bottom of the layers;
     */
    public Thing getIntrinsic() {
        return layers.get(0);
    }
}

/**
 * A position object has only the two coordinates as its field,
 * access them via x() and y();
 */
class Pos implements Serializable {
    protected int x;
    protected int y;
    public Pos() {}
    public Pos(int xC, int yC) {
        this.x = xC;
        this.y = yC;
    }
    public int x() {
        return x;
    }
    public int y() {
        return y;
    }

    /**
     * Determine whether a point(x, y) is in the map;
     * notice this uses startWIDTH/startHEIGHT and WIDTH/HEIGHT in WG class;
     */
    public boolean inMap() {
        return x >= WG.startWIDTH && x < WG.WIDTH
                && y >= WG.startHEIGHT && y < WG.HEIGHT;
    }

    /**
     * Straight distance;
     */
    public double distance(Place des) {
        return Math.sqrt(Math.pow(this.x - des.x, 2)
                + Math.pow(this.y - des.y, 2));
    }
    /**
     * L-shaped distance;
     */
    public int LDistance(Place des) {
        return xDistance(des) + yDistance(des);
    }

    /**
     * The difference of y coordinate with the other point (absolute value);
     */
    public int yDistance(Place des) {
        return Math.abs(this.y - des.y);
    }
    /**
     * The difference of x coordinate with the other point (absolute value);
     */
    public int xDistance(Place des) {
        return Math.abs(this.x - des.x);
    }

}