package byog.Core.Objects;

import byog.Core.Objects.Headers.FixedThing;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Headers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Nothing extends FixedThing implements Obstacle {
    public static TETile avatar = Tileset.NOTHING;
    public Nothing() {}


    public Nothing(WG wg) {
        this.wg = wg;

        addToArrays();
    }

    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

    }

    @Override
    public TETile avatar() {
        return avatar;
    }

    @Override
    public void touchedBy(Thing thing) {

    }
}
