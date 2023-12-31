package byog.Core.Objects.SingleBlock;

import byog.Core.Objects.Supers.*;
import byog.Core.Objects.Supers.Interfaces.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RockMonster extends MovingDamageable implements Damager, Monster, Obstacle {
    protected static final int moveInterval = 1000;
    protected static final TETile[] default_avatar = {
            Tileset.ROMO_RIGHT, Tileset.ROMO_LEFT, Tileset.ROMO_UP, Tileset.ROMO_DOWN};
    protected static final TETile[] damaged_avatar = {
            Tileset.ROMO_RIGHT_DAMAGED, Tileset.ROMO_LEFT_DAMAGED,
            Tileset.ROMO_UP_DAMAGED, Tileset.ROMO_DOWN_DAMAGED};
    protected static int default_health = 3;
    protected static int atk = 1;
    protected int direction;

    RockMonster() {}

    @Override
    public int maxHealth() {
        return default_health;
    }

    public RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.health = default_health;

        place.addNew(this);
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
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && d.isEnemy(this)) {
            // None Enemy Damager;
            d.doDamage(this);
        }
        if (isEnemy(thing)) {
            // Mortal Friendly;
            doDamage((Damageable) thing);
        }
    }

    @Override
    public int randomAction() {
        return wander();
    }

    /**
     * Randomly walks around;
     */
    public int wander() {
        direction = wg.rand.nextInt(4);

        // if next to a target, reset direction to that;
        for (int i = 0; i < 4; i ++) {
            Thing neighbor = place.next(i).getPresent();
            // don't walk into enemy attacker;
            if (isEnemy(neighbor) && !(neighbor instanceof Attacker)) {
                direction = i;
                break;
            }
        }
        move(direction);
        moveIn.renew(moveInterval);
        return 1;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void doDamage(Mortal target) {
        target.damagedBy(getAtk());
    }

    @Override
    public boolean canAttack() {
        return moveIn.ended();
    }
}
