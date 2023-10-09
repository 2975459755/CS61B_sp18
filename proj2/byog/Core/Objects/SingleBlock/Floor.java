package byog.Core.Objects.SingleBlock;

import byog.Core.Objects.Supers.FixedThing;
import byog.Core.Objects.Supers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Floor extends FixedThing {
    public static TETile avatar = Tileset.FLOOR;

    public Floor() {}


    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

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

        addToArrays();
    }
}
