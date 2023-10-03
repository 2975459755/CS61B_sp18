package byog.Core.Objects;

import byog.Core.Objects.Headers.FixedThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Floor extends FixedThing {
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
    public void touchedBy(Thing thing) {

    }

    public Floor(WG wg) {
        this.wg = wg;

        updateArrays();
    }
}
