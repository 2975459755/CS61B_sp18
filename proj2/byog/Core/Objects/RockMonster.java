package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RockMonster extends MovingDamageable implements Damager, Enemy {
    public static final int actionInterval = 1000;
    static final TETile default_avatar = Tileset.MOUNTAIN;
    static final TETile damaged_avatar = Tileset.DAMAGED_ROMO;
    static int default_health = 3;
    static int atk = 1;

    RockMonster() {}

    public RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.actIn = new Interval(0);

        this.damaged = new Interval(0);

        this.ins = new Interval[] {actIn, damaged};
        this.health = default_health;

        updateArrays();
    }

    @Override
    public TETile defaultAvatar() {
        return default_avatar;
    }

    @Override
    public TETile damagedAvatar() {
        return damaged_avatar;
    }

    @Override
    public TETile avatar() {
        if (duringDamage()) {
            return RockMonster.damaged_avatar;
        }
        return RockMonster.default_avatar;
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public int getHealth() {
        return health;
    }


    @Override
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && !(thing instanceof Enemy)) {
            // None Enemy Damager;
            d.doDamage(this);
        }
        if ((thing instanceof Friendly) && (thing instanceof Mortal m)) {
            // Mortal Friendly;
            doDamage(m);
        }
    }

    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        return wander();
    }
    public int wander() {
        if (!canAct()) {
            return 0;
        }
        move(wg.rand.nextInt(4));
        actIn.renew(actionInterval);
        return 1;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void doDamage(Mortal target) {
        if (target instanceof Ally) {
            target.damagedBy(getAtk());
        }
    }
}
