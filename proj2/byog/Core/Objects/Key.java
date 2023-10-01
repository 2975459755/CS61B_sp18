package byog.Core.Objects;

import byog.Core.Objects.Headers.Collectable;
import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Key extends StaticThing implements Collectable {
    public static TETile avatar = Tileset.FLOWER;

    public Key() {}

    @Override
    public void updateArrays() {

    }

    @Override
    public TETile avatar() {
        return Key.avatar;
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public boolean collectable() {
        return true;
    }

    @Override
    public void touchedBy(Thing thing) {

    }

    @Override
    public void collectedBy(Thing thing) {
        if (thing instanceof Player) {
            remove();
            wg.door.open();
        }
    }

    public Key(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        updateArrays();
    }



    // TODO: when key is collected
}
