package byog.Core.Objects.Supers;

import byog.Core.Interval;
import byog.Core.Objects.Supers.Interfaces.Changeable;
import byog.Core.Place;

public abstract class MovingThing extends RemovableThing implements Changeable {
    protected Interval moveIn;
    protected Interval[] ins;
    protected int direction;

    public MovingThing() {
        this.moveIn = new Interval (0);
        this.ins = new Interval[] {moveIn};
        this.direction = 2;
    }
    @Override
    public void addToArrays() {
        if (!wg.keepTrackOf.contains(this)) {
            wg.keepTrackOf.add(this);
        }
    }
    @Override
    public void removeFromArrays() {
        wg.keepTrackOf.remove(this);
    }
    public void setDirection(int direc) {
        direction = direc;
    }
    public void move(int direc) {
        setDirection(direc);
        move(place.next(direc));
    }
    /**
     * This is protected because it does not set direction;
     */
    protected void move(Place des) {
        int c = goAt(des);
        if (c == 1) { // destination is available for entering;
            enter(des);
        }
    }
    public int goAt(int direc) {
        return goAt(place.next(direc));
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
