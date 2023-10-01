package byog.Core.Objects;

import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Nothing extends StaticThing {
    public static TETile avatar = Tileset.NOTHING;
    public static boolean isObstacle = true;
    public Nothing() {}

    @Override
    public void updateArrays() {

    }

    public Nothing(WG wg) {
        this.wg = wg;

        updateArrays();
    }

    @Override
    public TETile avatar() {
        return avatar;
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public void touchedBy(Thing thing) {

    }
}
