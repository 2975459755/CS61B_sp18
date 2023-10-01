package byog.Core.Objects;

import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Floor extends StaticThing {
    public static TETile avatar = Tileset.FLOOR;

    public Floor() {}

    @Override
    public void updateArrays() {

    }

    @Override
    public TETile avatar() {
        return Floor.avatar;
    }

    @Override
    public boolean isObstacle() {
        return false;
    }

    @Override
    public void touchedBy(Thing thing) {

    }

    public Floor(WG wg) {
        this.wg = wg;

        updateArrays();
    }
}
