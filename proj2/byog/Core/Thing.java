package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

abstract class Thing implements Serializable {
    WG wg;
    Place place;
    Thing() {}
    abstract TETile avatar();
    abstract boolean isObstacle();
    int isLuminator() {
        return -1;
    }
    boolean collectable() {
        return false;
    }
}
