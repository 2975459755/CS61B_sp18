package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.MovingThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.TileEngine.TETile;

public abstract class Block extends MovingThing {
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
}
