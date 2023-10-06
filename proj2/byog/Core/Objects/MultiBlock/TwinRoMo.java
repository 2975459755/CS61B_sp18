package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Attacker;
import byog.Core.Objects.Headers.Interfaces.Damager;
import byog.Core.Objects.Headers.Interfaces.Monster;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;

public class TwinRoMo extends MultiBlock {
    protected static final int moveInterval = 1000;
    public TwinRoMo(WG wg, Rectangle rect) {
        this.wg = wg;
        this.rectangle = rect;

        this.moveIn = new Interval(0);

        this.ins = new Interval[] {moveIn};
        this.direction = 2;

        for (int x = 0; x < rect.width; x ++) {
            for (int y = 0; y < rect.height; y ++) {
                Place place = rect.rectangle[x][y];
                BlockRoMo block = new BlockRoMo(wg, place, this);
                blocks.add(block);
                place.addNew(block);
            }
        }

        addToArrays();
    }
    @Override
    public void touchedBy(Thing thing) {

    }

    @Override
    public boolean duringDamage() {
        return false;
    }

    @Override
    public int getHealth() {
        return 1;
    }

    @Override
    public void damagedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {

    }

    @Override
    public int damageTime() {
        return 0;
    }

    @Override
    public int maxHealth() {
        return 1;
    }

    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        if (!canMove()) {
            return 0;
        }
        return wander();
    }
    public int wander() {
        direction = wg.rand.nextInt(4);
        move(direction);
        moveIn.renew(moveInterval);
        return 1;
    }

    @Override
    public int goAt(Place des) {
        return 0;
    }
}