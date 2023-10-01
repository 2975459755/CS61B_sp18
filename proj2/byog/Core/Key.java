package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Key extends StaticThing {
    static TETile avatar = Tileset.FLOWER;

    Key() {}

    @Override
    TETile avatar() {
        return Key.avatar;
    }

    @Override
    boolean isObstacle() {
        return true;
    }

    Key(WG wg, Place place) {
        this.wg = wg;
        this.place = place;
    }

    @Override
    boolean collectable() {
        return true;
    }

    // TODO: when key is collected
}
