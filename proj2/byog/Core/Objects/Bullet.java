package byog.Core.Objects;

import byog.Core.*;
import byog.Core.Objects.Headers.*;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Bullet extends MovingThing implements Mortal, Damager, Ally {
    public static final int actionInterval = 240;
    public static final int moveDistance = 5;
    public static final TETile default_avatar = Tileset.WATER;
    private Interval survival;
    private int direction;
    private int atk;


    public Bullet() {}

    @Override
    public void updateArrays() {
        wg.updMTs(this);
        wg.updLuminators(this);
    }

    @Override
    public TETile avatar() {
        return default_avatar;
    }

    @Override
    public boolean isObstacle() {
        return false;
    }
    @Override
    public int isLuminator() {
        return 1;
    }

    @Override
    public void touchedBy(Thing thing) {
        if (!(thing instanceof Friendly) && (thing instanceof Mortal m)) {
            // when mortal monsters walks into this;
            doDamage(m);
        } else {
            vanish();
        }
    }

    @Override
    public int getHealth() {
        if (survival.ended()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void damagedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {
        if (atk > 0) {
            remove();
        }
    }

    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        if (! canAct()) {
            return 0;
        }

        move(direction);
        actIn.renew(actionInterval);
        return 1;
    }
    @Override
    public int goAt(Place des) {
        des.touchedBy(this);
        // If does damage and died, should end immediately,
        // otherwise could be possibly removed again (and updateArray again);
        if (dead()) {
            return 0;
        }
        if (!des.canEnter()) {
            // hits hard thing, vanishes;
            vanish();
            return 0;
        }
        return 1;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void doDamage(Mortal m) {
        m.damagedBy(getAtk());
        vanish();
    }

    public Bullet (WG wg, Place place, int direction) {
        this.wg = wg;
        this.place = place;
        this.direction = direction;

        this.actIn = new Interval(actionInterval);
        this.survival = new Interval(actionInterval * moveDistance);
        this.ins = new Interval[] {actIn, survival};

        this.atk = 1;

        updateArrays();
    }

    private void vanish() {
        survival.renew(0); // dies;a
        remove();
    }
}
