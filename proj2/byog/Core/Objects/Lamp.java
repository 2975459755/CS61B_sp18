package byog.Core.Objects;

import byog.Core.Objects.Headers.ImmobileThing;
import byog.Core.Objects.Headers.Interfaces.Collectable;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/*
Normally a lamp is like an obstacle;
but when it's interacted, it lights up,
and if interacted again, it's removed (because it could be in the way);

Future thoughts:
When a player becomes a ghost in the next world,
the other player will have to lit up all lamps to reflesh the ghost;
 */
public class Lamp extends ImmobileThing {
    public static TETile avatar_unlit = Tileset.LAMP_UNLIT;
    public static TETile avatar_lit = Tileset.LAMP_LIT;
    public static int lumiRange = 6;
    public boolean litUp;

    public Lamp() {}

    @Override
    public void updateArrays() {
        wg.updTrack(this);
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
        } else {
            remove();
        }
    }

}
