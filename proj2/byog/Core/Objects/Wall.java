package byog.Core.Objects;

import byog.Core.Objects.Headers.FixedThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Wall extends FixedThing {
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

    @Override
    public void touchedBy(Thing thing) {

    }

    public Wall(WG wg) {
        this.wg= wg;

        updateArrays();
    }
}
