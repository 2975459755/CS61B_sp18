package byog.Core.Objects;

import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Key extends StaticThing {
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

    public Key(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        updateArrays();
    }

    // TODO: when key is collected
}
