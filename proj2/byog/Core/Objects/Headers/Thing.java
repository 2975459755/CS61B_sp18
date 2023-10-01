package byog.Core.Objects.Headers;

import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;

import java.io.Serializable;

public abstract class Thing implements Serializable {
    public WG wg;
    public Place place;
    public Thing() {}

    /**
     * When something is constructed or removed,
     * the special arrays should be updated (like wg.MTs);
     */
    public abstract void updateArrays();

    /**
     * @return the TETile representation of the instance;
     */
    public abstract TETile avatar();

    /**
     * @return whether the instance blocks way;
     */
    public abstract boolean isObstacle();

    /**
     * @return The range that instance luminates its surroundings;
     * 0 is only itself; -1 is none;
     */
    public int isLuminator() {
        return -1;
    }

    /**
     * @return whether the instance is collectable;
     */
    public boolean collectable() {
        return false;
    }
    public void enter(Place des) {
        des.addNew(this);
        place.restore();

        place = des;
    }
    public abstract void touchedBy(Thing thing);
    /**
     * Remove it from the world;
     * Currently Walls can't be removed;
     * Use after dead() !
     */
    public int remove() {
        updateArrays();
        place.restore();
        return 1;
    }
}
