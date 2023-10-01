package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

class Nothing extends StaticThing {
    static TETile avatar = Tileset.NOTHING;
    static boolean isObstacle = true;
    Nothing() {}

    Nothing(WG wg) {
        this.wg = wg;
    }

    @Override
    TETile avatar() {
        return avatar;
    }

    @Override
    boolean isObstacle() {
        return true;
    }
}
