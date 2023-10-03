package byog.Core.Objects;

import byog.Core.Objects.Headers.Enterable;
import byog.Core.Objects.Headers.FixedThing;
import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Door extends Enterable {
    public static TETile avatar_locked = Tileset.LOCKED_DOOR;
    public static TETile avatar_open = Tileset.UNLOCKED_DOOR;
    public static int lumiRange = 2;

    public Door() {}

    @Override
    public TETile avatar() {
        if (open) {
            return avatar_open;
        } else {
            return avatar_locked;
        }
    }

    @Override
    public void newWorld() {
        wg.randomWorld(false);
    }

    @Override
    public int isLuminator() {
        if (open) {
            return Door.lumiRange;
        } else {
            return -1;
        }
    }

    public Door(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.open = false;

        updateArrays();
    }

}
