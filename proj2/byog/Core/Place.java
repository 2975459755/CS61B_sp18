package byog.Core;

import byog.Core.Objects.SingleBlock.Floor;
import byog.Core.Objects.Headers.Interfaces.Collectable;
import byog.Core.Objects.SingleBlock.Nothing;
import byog.Core.Objects.Headers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static jdk.dynalink.linker.support.Guards.isInstance;


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

    TETile getVisible() {
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
    public void collect(Thing thing) {
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

                if (new Place(i, j).inMap()) {
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
    public boolean hasDirec(int direc, Thing thing) {
        Place next = next(direc);
        return next != null && next.nowIs(thing);
    }

    /**
     * @param direct 0: right; 1 : left; 2: up; 3: down;
     *               4: ↑→; 5: ↓←; 6: ↓→; 7: ↑←;
     */
    public Place next(int direct) {
        int xC = x;
        int yC = y;
        switch (direct) {
            case 0: xC ++; break; // right
            case 1: xC --; break; // left
            case 2: yC ++; break; // up
            case 3: yC --; break; // down
            case 4: xC ++; yC ++; break; // up right
            case 5: xC --; yC --; break; // down left
            case 6: xC ++; yC --; break; // down right
            case 7: xC --; yC ++; break; // up left
        }

        if (new Place(xC, yC).inMap()) {
            return WG.places[xC][yC];
        }
        return null;
    }
    public boolean isFloor() {
        return nowIs(new Floor(null));
    }
    public boolean originalWas(Thing thing) {
        return thing.getClass().isInstance(getIntrinsic());
    }
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
    public Thing getIntrinsic() {
        return layers.get(0);
    }
}
class Pos implements Serializable {
    protected int x;
    protected int y;
    public Pos() {}
    public Pos(int xC, int yC) {
        this.x = xC;
        this.y = yC;
    }
    public boolean inMap() {
        return x >= WG.startWIDTH && x < WG.WIDTH
                && y >= WG.startHEIGHT && y < WG.HEIGHT;
    }
    public double distance(Place des) {
        return Math.sqrt(Math.pow(this.x - des.x, 2)
                + Math.pow(this.y - des.y, 2));
    }
    /**
     * L-shaped distance;
     */
    public int LDistance(Place des) {
        return Math.abs(this.x - des.x) + Math.abs(this.y - des.y);
    }

}