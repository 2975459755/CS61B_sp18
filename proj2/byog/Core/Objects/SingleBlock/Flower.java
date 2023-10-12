package byog.Core.Objects.SingleBlock;

import byog.Core.Interval;
import byog.Core.Objects.Supers.ImmobileDamageable;
import byog.Core.Objects.Supers.Interfaces.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Flower extends ImmobileDamageable implements Monster, Obstacle, Shooter, HasSight {
    protected Interval attackIn;
    protected static int attackInterval = 1500;
    protected static int default_health = 4;
    protected static int default_sight = 6;
    protected int atk = 1;

    public Flower() {}

    public Flower(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.attackIn = new Interval(0);
        this.ins = new Interval[] {damaged, attackIn};
        this.health = default_health;

        place.addNew(this);
        addToArrays();
    }

    @Override
    public int maxHealth() {
        return default_health;
    }

    @Override
    public TETile defaultAvatar() {
        return Tileset.FLOWER_DEFAULT;
    }

    @Override
    public TETile damagedAvatar() {
        return Tileset.FLOWER_DAMAGED;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void doDamage(Mortal target) {

    }

    @Override
    public boolean canAttack() {
        return attackIn.ended();
    }

    @Override
    public TETile bulletAvatar() {
        return Tileset.BULLET_FLOWER;
    }

    @Override
    public int bulletAtk() {
        return getAtk();
    }

    @Override
    public boolean canAct() {
        return canAttack();
    }

    @Override
    protected int randomAction() {
        // perform attack
        int direc = -1;
        for (int i = 0; i < 4; i ++) {
            if (sees(i)) {
                direc = i;
                break;
            }
        }
        if (direc == -1) {
            return 0;
        }
        return attack(direc);
    }

    public int attack(int direc) {
        shoot(direc, place);
        attackIn.renew(attackInterval);
        return 1;
    }

    @Override
    public boolean sees(int direc) {
        Place startCenter = place;
        int d = 2 * (1 - direc / 2);
        Place startRight = place.next(d + 0);
        Place startLeft = place.next(d + 1);

        return seeHelper(direc, startCenter);
//                || seeHelper(direc, startLeft)
//                || seeHelper(direc, startRight);
    }

    protected boolean seeHelper(int direc, Place spot) {
        if (spot == null) {
            return false;
        }
        for (int i = 0; i < sight(); i ++) {
            spot = spot.next(direc);
            if (spot == null) {
                break;
            } else if (isEnemy(spot.getPresent())) {
                return true;
            } else if (spot.getPresent().isObstacle()) {
                break;
            }
        }
        return false;
    }

    @Override
    public int sight() {
        return default_sight;
    }
}
