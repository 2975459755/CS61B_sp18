package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Supers.Interfaces.Damageable;
import byog.Core.Objects.Supers.Interfaces.Damager;
import byog.Core.Objects.Supers.Interfaces.Monster;
import byog.Core.Objects.Supers.Interfaces.Mortal;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Place;
import byog.Core.Utils;
import byog.Core.WG;

import static byog.Core.Utils.*;

public class MobileFlower extends MultiBlock implements Monster {
    protected final int moveInterval = 1200;
    protected final int attackInterval = 1500;
    protected final int default_health = 5;
    protected Interval attackIn;
    protected int atk = 1;
    protected BlockFlower flower;

    public MobileFlower() {}
    public MobileFlower(WG wg, Rectangle rect) {
        this.wg = wg;
        this.rectangle = rect;

        this.health = default_health;

        this.attackIn = new Interval(0);
        this.ins = new Interval[] {damaged, moveIn, attackIn};

        for (int x = 0; x < rect.width; x ++) {
            for (int y = 0; y < rect.height; y ++) {
                if (x == 0 || x == 2) {
                    Place place = rect.rectangle[x][y];
                    BlockStone stone = new BlockStone(wg, place, this);
                    blocks.add(stone);
                } else if (y == 1) {
                    Place place = rect.rectangle[x][y];
                    BlockFlower f = new BlockFlower(wg, place, this);
                    blocks.add(f);
                    flower = f;
                    f.setDirection(Up);
                }
            }
        }

        this.direction = Up;
        addToArrays();
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
    public int goAt(Place des) {
        return Unavailable;
    }

    @Override
    public int randomAction() {
        // check if sees a target;
        int d;
        boolean f = false;
        if (flower.sees(direction)) {
            d = direction;
            f = true;
        } else {
            // search in the opposite direction;
            d = Utils.oppositeDirec(direction);
            if (flower.sees(d)) {
                f = true;
            }
        }
        if (f) {
            // sees a target;
            if (canAttack()) {
                flower.shoot(d, flower.getPlace());
                attackIn.renew(attackInterval);
            } else {
                move(Utils.oppositeDirec(d));
                moveIn.renew(moveInterval);
            }

            return 1;
        } else if (canMove()) {
            return wander();
        }
        return 0;
    }
    public int wander() {
        int direc = wg.rand.nextInt(4);
        if (direc == direction || direc == Utils.oppositeDirec(direction)) {
            direction = direc;
            move(direction);
        } else {
            if (wg.rand.nextBoolean()) {
                // rotate
                rotate(Utils.clockwise(direction, direc));
                direction = direc;
            } else {
                // crab-like move;
                move(direc);
            }

        }
        moveIn.renew(moveInterval);
        return 1;
    }

    @Override
    public boolean canAct() {
        return canMove() || canAttack();
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
    public int maxHealth() {
        return default_health;
    }
}
