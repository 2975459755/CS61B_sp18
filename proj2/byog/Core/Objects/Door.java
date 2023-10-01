package byog.Core.Objects;

import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Door extends StaticThing {
    public static TETile avatar_locked = Tileset.LOCKED_DOOR;
    public static TETile avatar_open = Tileset.UNLOCKED_DOOR;
    public static int lumiRange = 2;
    public boolean open;

    public Door() {}

    @Override
    public void updateArrays() {
        wg.updLuminators(this); // door is possible luminator
    }

    @Override
    public TETile avatar() {
        if (open) {
            return avatar_open;
        } else {
            return avatar_locked;
        }
    }
    @Override
    public boolean isObstacle() {
        return true;
    }
    @Override
    public int isLuminator() {
        if (open) {
            return Door.lumiRange;
        } else {
            return -1;
        }
    }

    @Override
    public void touchedBy(Thing thing) {
        if (open && (thing instanceof Player)) {
            // enters a new world
            newWorld();
        }
    }

    public Door(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.open = false;

        updateArrays();
    }

    public void open() {
        open = true;
    }
    private void newWorld() {
        wg.randomWorld(false);
    }
}
