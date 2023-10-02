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

    public Attacker() {}

    public abstract boolean isTarget(Thing thing);
    private void vanish() {
        survival.renew(0); // dies;
        remove();
    }
    @Override
    public void touchedBy(Thing thing) {
        if (isTarget(thing)) {
            // when target walks into this
            doDamage((Mortal) thing);
        } else {
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
        if (survival.ended()) {
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    public void damagedBy(Thing thing) {

    }
    @Override
    public void damagedBy(int atk) {

    }
    @Override
    public int getAtk() {
        return atk;
    }
    @Override
    public void doDamage(Mortal m) {
        vanish();

        m.damagedBy(getAtk());

    }
    @Override
    public int goAt(Place des) {
        des.touchedBy(this);
        // If attacker did damage and died, should end immediately,
        // otherwise could be possibly removed again (and updateArray again);
        if (dead()) {
            return 0;
        }
        if (!des.canEnter()) {
            // hits hard thing, vanishes;
            vanish();
            return 0;
        }
        return 1;
    }
}
