package byog.Core.Objects.Headers.Interfaces;

import byog.TileEngine.TETile;

import java.io.Serializable;

public interface AnyThing extends Serializable {
    /**
     * When this is constructed or removed,
     * the array tracking them should be updated;
     */
    public void updateArrays();
    /**
     * @return the TETile representation of the instance;
     */
    public TETile avatar();
}
