package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Damager;
import byog.Core.Objects.Headers.Interfaces.HasTarget;
import byog.Core.Objects.Headers.Interfaces.Mortal;
import byog.Core.Objects.Headers.MovingDamageable;
import byog.Core.Objects.Headers.MovingThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.TileEngine.TETile;

public abstract class Block extends MovingDamageable implements Damager, HasTarget {
    public MultiBlock mother;

    /**
     * A Block should not be added to changeable array;
     */
    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

    }

    @Override
    public void move(int direc) {
        direction = direc;
        move(place.next(direc));
    }
    @Override
    public void move(Place des) {
        // block's goAt() method was already used in MultiBlock's move() method;
        if (true) { // destination is available for entering;
            enter(des);
        }
    }
    @Override
    public int goAt(Place des) {
        des.touchedBy(this);
        if (des.canEnter() || mother.blocks.contains(des.getPresent())) {
            return 1;
        }
        return 0;
    }
    @Override
    public void touchedBy(Thing thing) {
        mother.touchedBy(thing);
    }

    @Override
    public int randomAction() {
        return 0;
    }

    @Override
    public int getAtk() {
        return mother.getAtk();
    }

    @Override
    public void doDamage(Mortal target) {

    }

    @Override
    public int maxHealth() {
        return mother.maxHealth();
    }

    @Override
    public int getHealth() {
        return mother.getHealth();
    }

    @Override
    public void damagedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {
        mother.damagedBy(atk);
    }
}
