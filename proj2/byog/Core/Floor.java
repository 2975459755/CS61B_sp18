package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Floor extends StaticThing {
    static TETile avatar = Tileset.FLOOR;

    Floor() {}

    @Override
    TETile avatar() {
        return Floor.avatar;
    }

    @Override
    boolean isObstacle() {
        return false;
    }

    Floor(WG wg) {
        this.wg = wg;
    }
}
