package byog.Core;

import byog.Core.Objects.Floor;
import byog.Core.Objects.Nothing;
import byog.Core.Objects.Headers.Thing;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

import static jdk.dynalink.linker.support.Guards.isInstance;


public class Place implements Serializable {
    int x;
    int y;
    public Thing intrinsic; // Nothing, Floor, Wall, Door;
    public Thing present;
    public boolean visible;

    public Place(int xCoor, int yCoor, Thing thing) {
        x = xCoor;
        y = yCoor;
        intrinsic = thing;
        present = intrinsic;
    }
    public Place(int xCoor, int yCoor) {
        x = xCoor;
        y = yCoor;
    }
    public Place(Thing thing) {
        intrinsic = thing;
        present = intrinsic;
    }
    public void fill(Thing thing) {
        intrinsic = thing;
        present = thing;
    }
    public void addNew(Thing thing) {
        present = thing;
    }
    public void restore() {
        present = intrinsic;
    }
    TETile getVisible() {
        if (visible) {
            return present.avatar();
        }
        return Nothing.avatar;
    }
    public boolean collectable() {
        return present.collectable();
    }
    public boolean canEnter() {
        return !(intrinsic.isObstacle() && present.isObstacle());
    }
    public int isLuminator() {
        return Math.max(intrinsic.isLuminator(), present.isLuminator());
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
        return hasNext(sides, new Nothing());
    }
    public boolean hasNextFloor(int sides) {
        return hasNext(sides, new Floor());
    }
    public boolean hasNext(int sides, Thing thing) {
        for (int i = 0; i < 8; i ++) {
            if (hasDirec(i % sides, thing)) {
                return true;
            }
        }
        return false;
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
        return thing.getClass().isInstance(intrinsic);
    }
    public boolean nowIs(Thing thing) {
        if (present != null) {
            return thing.getClass().isInstance(present);
        }
        return thing.getClass().isInstance(intrinsic);
    }

    public boolean inMap() {
        return x >= WG.startWIDTH && x < WG.WIDTH
                && y >= WG.startHEIGHT && y < WG.HEIGHT;
    }
}
