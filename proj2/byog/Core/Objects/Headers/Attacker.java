package byog.Core.Objects.Headers;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Damager;
import byog.Core.Objects.Headers.Interfaces.Mortal;
import byog.Core.Place;

public abstract class Attacker extends MovingThing implements Mortal, Damager {
    protected int actionInterval = 240;
    protected int moveDistance = 5;
    protected Interval survival;
    protected int direction;
    protected int atk;
    protected boolean vanished; // prevent from repeating updateArray;

    public Attacker() {
        vanished = false;
    }

    /**
     * Make an attacker die;
     * Notice this does not call `remove`,
     * instead, `randomAction` will take care of it;
     */
    private void vanish() {
        if (!vanished) {
            survival.renew(0); // dies;
//            remove();
            vanished = true;
        }
    }

    /**
     * When a target walks to this, perform attack;
     * Otherwise when an obstacle enters, should vanish;
     */
    @Override
    public void touchedBy(Thing thing) {
        if (isTarget(thing)) {
            // when target walks into this
            doDamage((Mortal) thing);
        } else if (thing.isObstacle()) {
            vanish();
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
    public int getHealth() {
        if (survival.ended() || atk == 0) {
            // when move to max distance, or atk expires, it dies;
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    public void damagedBy(Thing thing) {
        if (thing instanceof Attacker) { // collide with hostile attacker;
            vanish();
        }
    }
    @Override
    public void damagedBy(int atk) {

    }
    @Override
    public int getAtk() {
        return atk;
    }

    /**
     * Occasions when this is called:
     * 1. Trying to enter a target's place -> Called by target's touchedBy method;
     * 2. A target attempts to enter this place -> Called by this.touchedBy method;
     */
    @Override
    public void doDamage(Mortal m) {
        vanish();

        m.damagedBy(this); // collide with hostile attacker;
        m.damagedBy(getAtk());
    }
    @Override
    public int goAt(Place des) {
        des.touchedBy(this);

        if (!des.canEnter()) {
            // hits hard thing, vanishes;
            vanish();
            return 0;
        }
        return 1;
    }
}
