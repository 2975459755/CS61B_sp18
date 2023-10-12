package byog.Core.Objects.Supers;

import byog.Core.Interval;
import byog.Core.Objects.Supers.Interfaces.Damager;
import byog.Core.Objects.Supers.Interfaces.HasTarget;
import byog.Core.Objects.Supers.Interfaces.Mortal;
import byog.Core.Place;
import byog.TileEngine.TETile;

public abstract class Attacker extends MovingThing implements HasTarget, Mortal, Damager {
    protected int moveInterval = 240;
    protected int moveDistance = 5;
    protected Interval survival;
    protected int atk;
    protected boolean vanished;
    public HasTarget owner;
    protected TETile avatar;

    public Attacker() {
        vanished = false;
    }

    /**
     * Make an attacker die;
     * Notice this does not call `remove`,
     * instead, `randomAction` will take care of it;
     * because when it dies, it remains on the screen for a moment,
     * (only showed on the screen, does not do anything else);
     */
    protected void vanish() {
        if (!vanished) {
            survival.renew(150); // dies, but keeps on the screen for a moment;
            moveIn.renew(9999); // but should not move again;
            vanished = true;
        }
    }

    @Override
    public boolean isEnemy(Thing thing) {
        return owner.isEnemy(thing);
    }
    @Override
    public boolean isAlly(Thing thing) {
        return owner.isAlly(thing);
    }

    /**
     * When a target walks into this, perform attack;
     * Otherwise when an obstacle enters, should vanish;
     */
    @Override
    public void touchedBy(Thing thing) {
        if (isEnemy(thing)) {
            // when target walks into this;
            doDamage((Mortal) thing);
        } else if ((thing instanceof Attacker d) && (owner.isEnemy((Thing)d.owner))) {
            // when enemy attacker walks into this, should both vanish;
            doDamage(d);
        } else if (thing.isObstacle()) {
            // hits an obstacle;
            vanish();
        }
    }
    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        if (! canMove()) {
            return 0;
        }

        move(direction);
        moveIn.renew(moveInterval);
        return 1;
    }
    @Override
    public int getHealth() {
        if (survival.ended()) {
            // when move to max distance, it dies;
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    public void damagedBy(Thing thing) {
        if (vanished) {
            return;
        }
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
     * This means nothing to Attackers;
     */
    @Override
    public boolean canAttack() {
        return true;
    }

    /**
     * Occasions when this is called:
     * 1. Trying to enter a target's place -> Called by target's touchedBy method;
     * 2. A target attempts to enter this place -> Called by this.touchedBy method;
     */
    @Override
    public void doDamage(Mortal m) {
        if (vanished) {
            return;
        }

        vanish();

        m.damagedBy(this); // collide with hostile attacker;
        m.damagedBy(getAtk());
    }
    @Override
    public int goAt(Place des) {
        des.touchedBy(this);

        if (isEnemy(des.getPresent())) {
            return 1; // so that it covers the attacked target for a moment;
        }
        if (!des.canEnter()) {
            // hits hard thing, vanishes;
            vanish();
            return 0;
        }
        return 1;
    }
    @Override
    public void move(int direc) {
        move(place.next(direc));
    }
    @Override
    public void move(Place des) {
        int c = goAt(des);
        if (c == 1) { // destination is available for entering;
            enter(des);
        }
    }
}
