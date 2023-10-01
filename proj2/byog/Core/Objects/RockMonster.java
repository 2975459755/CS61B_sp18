package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RockMonster extends MovingThing implements Mortal, Damager {
    public static final int actionInterval = 1000;
    static final TETile default_avatar = Tileset.MOUNTAIN;
    static int default_health = 3;
    static int atk = 1;
    private int health;

    RockMonster() {}

    public RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.actIn = new Interval(0);
        this.ins = new Interval[] {actIn};
        this.health = default_health;

        updateArrays();
    }

    @Override
    public void updateArrays() {
        wg.updMTs(this);
    }

    @Override
    public TETile avatar() {
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
        if (thing instanceof Bullet b) {
            b.doDamage(this);
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
    public void damagedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {
        health -= atk;
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
