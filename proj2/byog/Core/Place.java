package byog.Core;

import byog.Core.Objects.Floor;
import byog.Core.Objects.Headers.Interfaces.Collectable;
import byog.Core.Objects.Nothing;
import byog.Core.Objects.Headers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static jdk.dynalink.linker.support.Guards.isInstance;


public class Place extends Pos implements Serializable {
    public boolean visible;
    private ArrayList<Thing> layers;

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
        layers = new ArrayList<Thing> ();
        layers.add(thing);
    }
    /**
     * Make the place nowIs some Thing;
     */
    public void addNew(Thing thing) {
        layers.add(thing);
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
    public boolean canEnter() {
        return !getPresent().isObstacle();
    }
    public void touchedBy(Thing thing) {
        getPresent().touchedBy(thing);
    }

    public int isLuminator() {
        int res = -1;
        for (Thing t: layers) {
            if (t.isLuminator() > res) {
                res = t.isLuminator();
            }
        }
        return res;
    }
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
