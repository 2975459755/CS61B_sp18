package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RockMonster extends MovingDamageable implements Damager, Enemy, Monster, Obstacle {
    public static final int actionInterval = 1000;
    static final TETile[] default_avatar = {
            Tileset.ROMO_RIGHT, Tileset.ROMO_LEFT, Tileset.ROMO_UP, Tileset.ROMO_DOWN};
    static final TETile[] damaged_avatar = {
            Tileset.ROMO_RIGHT_DAMAGED, Tileset.ROMO_LEFT_DAMAGED,
            Tileset.ROMO_UP_DAMAGED, Tileset.ROMO_DOWN_DAMAGED};
    static int default_health = 3;
    static int atk = 1;
    protected int direction;

    RockMonster() {}

    @Override
    public int maxHealth() {
        return default_health;
    }

    public RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.actIn = new Interval(0);

        this.damaged = new Interval(0);

        this.ins = new Interval[] {actIn, damaged};
        this.health = default_health;
        this.direction = 2;

        addToArrays();
    }

    @Override
    public TETile defaultAvatar() {
        return default_avatar[direction];
    }

    @Override
    public TETile damagedAvatar() {
        return damaged_avatar[direction];
    }

    @Override
    public TETile avatar() {
        if (duringDamage()) {
            return damagedAvatar();
        }
        return defaultAvatar();
    }

    @Override
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && !(thing instanceof Enemy)) {
            // None Enemy Damager;
            d.doDamage(this);
        }
        if (isTarget(thing)) {
            // Mortal Friendly;
            doDamage((Damageable) thing);
        }
    }

    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        if (!canAct()) {
            return 0;
        }
        return wander();
    }

    /**
     * Randomly walks around;
     */
    public int wander() {
        direction = wg.rand.nextInt(4);

        // if next to a target, reset direction to that;
        for (int i = 0; i < 4; i ++) {
            if (isTarget((place.next(i).getPresent()))) {
                direction = i;
                break;
            }
        }
        move(direction);
        actIn.renew(actionInterval);
        return 1;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public boolean isTarget(Thing thing) {
        return (thing instanceof Friendly) && (thing instanceof Damageable);
    }

    @Override
    public void doDamage(Mortal target) {
        target.damagedBy(getAtk());
    }
}
