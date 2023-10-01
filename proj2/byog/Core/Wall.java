package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

class Wall extends StaticThing {
    static TETile avatar = Tileset.WALL;
    Wall() {}

    @Override
    TETile avatar() {
        return Wall.avatar;
    }
    @Override
    boolean isObstacle() {
        return true;
    }

    Wall(WG wg) {
        this.wg= wg;
    }
}
