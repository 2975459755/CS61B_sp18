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
    public abstract TETile avatar();
    public abstract boolean isObstacle();
    public int isLuminator() {
        return -1;
    }
    public boolean collectable() {
        return false;
    }
    public void walkedTo() {
        // TODO
    }
    /**
     * Use after dead() !
     */
    public int remove() {
        updateArrays();
        place.restore();
        return 1;
    }
}
