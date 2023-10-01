package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

class Door extends StaticThing {
    static TETile avatar_locked = Tileset.LOCKED_DOOR;
    static TETile avatar_open = Tileset.UNLOCKED_DOOR;
    static int lumiRange = 2;
    boolean open;

    Door() {}

    @Override
    TETile avatar() {
        if (open) {
            return avatar_open;
        } else {
            return avatar_locked;
        }
    }
    @Override
    boolean isObstacle() {
        return true;
    }
    @Override
    int isLuminator() {
        if (open) {
            return Door.lumiRange;
        } else {
            return -1;
        }
    }

    Door(WG wg, Place place) {
        this.wg = wg;
        this.place = place;
    }

    void open() {
        open = true;
    }
}
