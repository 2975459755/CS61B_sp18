package byog.Core.Objects;

import byog.Core.Objects.Headers.StaticThing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Wall extends StaticThing {
    public static TETile avatar = Tileset.WALL;
    public Wall() {}

    @Override
    public void updateArrays() {

    }

    @Override
    public TETile avatar() {
        return Wall.avatar;
    }
    @Override
    public boolean isObstacle() {
        return true;
    }

    public Wall(WG wg) {
        this.wg= wg;

        updateArrays();
    }
}
