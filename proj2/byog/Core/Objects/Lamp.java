package byog.Core.Objects;

import byog.Core.Objects.Headers.Collectable;
import byog.Core.Objects.Headers.StaticThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Lamp extends StaticThing implements Collectable {
    public static TETile avatar_unlit = Tileset.LAMP_UNLIT;
    public static TETile avatar_lit = Tileset.LAMP_LIT;
    public static int lumiRange = 6;
    public boolean litUp;

    public Lamp() {}

    @Override
    public void updateArrays() {
        wg.updLuminators(this); // lamp is luminator
    }

    @Override
    public TETile avatar() {
        if (litUp) {
            return avatar_lit;
        } else {
            return avatar_unlit;
        }
    }
    @Override
    public boolean isObstacle() {
        return true;
    }
    @Override
    public int isLuminator() {
        if (litUp) {
            return lumiRange;
        } else {
            return -1;
        }
    }
    @Override
    public boolean collectable() {
        return true;
    }

    @Override
    public void touchedBy(Thing thing) {
        if (thing instanceof Player) {
            lightUp();
        }
    }

    @Override
    public void collectedBy(Thing thing) {
        if (thing instanceof Player) {
            remove();
        }
    }

    public Lamp(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        litUp = false;

        updateArrays();
    }

    public void lightUp() {
        if (! litUp) {
            litUp = true;
        }
    }

}
