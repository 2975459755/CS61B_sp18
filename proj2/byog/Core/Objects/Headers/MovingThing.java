package byog.Core.Objects.Headers;

import byog.Core.Game;
import byog.Core.Interval;
import byog.Core.Objects.Floor;
import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Place;

public abstract class MovingThing extends RemovableThing implements Changeable {
    public Interval moveIn;
    public Interval[] ins;
    protected int direction;

    public MovingThing() {}
    @Override
    public void addToArrays() {
        if (wg.keepTrackOf.contains(this)) {
            return;
        }
        wg.keepTrackOf.add(this);
    }
    @Override
    public void removeFromArrays() {
        wg.keepTrackOf.remove(this);
    }
    public void move(int direc) {
        direction = direc;
        move(place.next(direc));
    }
    public void move(Place des) {
        int c = goAt(des);
        if (c == 1) { // destination is available for entering;
            enter(des);
        }
    }
    /**
     * Try to enter some place;
     * @return 1: Destination is available for entering;
     * 0: Can't go to destination;
     * -1: something special happens;
     */
    public int goAt(Place des) {
        des.touchedBy(this);
        if (des.canEnter()) {
            return 1;
        }
        return 0;
    }
    @Override
    public <T> void update(T t) {
        for (Interval in: ins) {
            in.update(t);
        }
    }
    public boolean canMove() {
        return moveIn.ended();
    }

    @Override
    public int change() {
        return randomAction();
    }
    /**
     * Check if this is dead; if so, remove it; else, if canAct, take action;
     * @return 0: no action; > 0: actioned;
     */
    public abstract int randomAction();
}
