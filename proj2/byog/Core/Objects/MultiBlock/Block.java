package byog.Core.Objects.MultiBlock;

import byog.Core.Objects.Supers.Interfaces.Damager;
import byog.Core.Objects.Supers.Interfaces.HasTarget;
import byog.Core.Objects.Supers.Interfaces.Mortal;
import byog.Core.Objects.Supers.MovingDamageable;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Place;

import static byog.Core.Utils.*;

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
        des.touchedBy(this); // TODO
        if (des.canEnter() || mother.blocks.contains(des.getPresent())) {
            return Available;
        }
        return Unavailable;
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
        int reducedAtk = Math.max(0, atk - getArmor());
        mother.damagedBy(reducedAtk);
    }
}
