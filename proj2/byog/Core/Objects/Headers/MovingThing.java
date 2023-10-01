package byog.Core.Objects.Headers;

import byog.Core.Game;
import byog.Core.Interval;
import byog.Core.Objects.Floor;
import byog.Core.Place;

public abstract class MovingThing extends Thing {
    public Interval actIn;
    public Interval[] ins;

    public MovingThing() {}
    public void move(int direc) {
        move(place.next(direc));
    }
    public void move(Place des) {
        int c = goAt(des);
        if (c == 1) { // destination is available for entering;
            if (des.nowIs(new Floor())) {
                /*
                Swap `present` of two places;
                 */
                des.addNew(this);
                place.restore();
                /*
                Update status;
                 */
                place = des;
            }
        }
    }
    /**
     *
     * @return 1: Destination is available for entering;
     * 0: Can't go to destination;
     * -1: something special happens;
     */
    public int goAt(Place des) {
        return 1;
    }

    public void update() {
        update(Game.miniInterval);
    }
    public <T> void update(T t) {
        for (Interval in: ins) {
            in.update(t);
        }
    }
    public boolean canAct() {
        return actIn.ended();
    }

    /**
     * Check if this is dead; if so, remove it; else, if canAct, take action;
     * @return 0: no action; > 0: actioned;
     */
    public abstract int randomAction();
}
