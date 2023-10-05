package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;

import java.io.Serializable;

public interface AnyThing extends Serializable {
    /**
     * When this is constructed or removed,
     * the array tracking them should be updated;
     */
    public void addToArrays();
    public void removeFromArrays();
    public WG getWorld();
    public Place getPlace();
    /**
     * @return the TETile representation of the instance;
     */
    public TETile avatar();
    /**
     * @return whether the instance blocks way;
     */
    default boolean isObstacle() {
        return false;
    }

    /**
     * @return The range that instance luminates its surroundings;
     * 0 is only itself; -1 is none;
     */
    default int isLuminator() {
        return -1;
    }

    /**
     * @return whether the instance is collectable;
     */
    default boolean isCollectable() {
        return false;
    }
    public void touchedBy(Thing thing);
}
