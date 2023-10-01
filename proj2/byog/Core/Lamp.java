package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Lamp extends StaticThing {
    static TETile avatar_unlit = Tileset.LAMP_UNLIT;
    static TETile avatar_lit = Tileset.LAMP_LIT;
    static int lumiRange = 6;
    boolean litUp;

    Lamp() {}

    @Override
    TETile avatar() {
        if (litUp) {
            return avatar_lit;
        } else {
            return avatar_unlit;
        }
    }
    @Override
    boolean isObstacle() {
        return true;
    }
    @Override
    int isLuminator() {
        if (litUp) {
            return lumiRange;
        } else {
            return -1;
        }
    }
    @Override
    boolean collectable() {
        return true;
    }

    Lamp(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        litUp = false;
    }

    void lightUp() {
        if (! litUp) {
            litUp = true;
        }
    }
}
