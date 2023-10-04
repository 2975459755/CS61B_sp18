package byog.Core.Objects;

import byog.Core.Objects.Headers.ImmobileThing;
import byog.Core.Objects.Headers.Interfaces.Collectable;
import byog.Core.Objects.Headers.Interfaces.Interactable;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/*
Normally a lamp is like an obstacle;
but when it's interacted, it lights up,
and if interacted again, it's removed (because it could be in the way);

When a player becomes a ghost (but not dead),
the other player will have to lit up all lamps to reflesh the ghost;
 */
public class Lamp extends ImmobileThing implements Interactable, Obstacle {
    public static TETile avatar_unlit = Tileset.LAMP_UNLIT;
    public static TETile avatar_lit = Tileset.LAMP_LIT;
    public static int lumiRange = 6;
    protected boolean litUp;

    public Lamp() {}

    @Override
    public void addToArrays() {
        if (wg.keepTrackOf.contains(this)) {
            return;
        }
        wg.keepTrackOf.add(this);
    }
    @Override
    public void removeFromArrays() {
        wg.keepTrackOf.remove(this);
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
    public void touchedBy(Thing thing) {
    }

    public Lamp(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        litUp = false;

        addToArrays();
    }

    public void lightUp() {
        if (! litUp) {
            litUp = true;
        } else {
            remove();
        }

        // check whether all lamps are lit;
        wg.checkAllLamps();
    }

    public boolean isLitUp() {
        return litUp;
    }

    @Override
    public void interactedBy(Thing thing) {
        if (thing instanceof Player p) {
            lightUp();
        }
    }
}
