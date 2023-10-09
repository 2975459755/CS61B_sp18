package byog.Core.Objects.Supers;

import byog.Core.Place;

public abstract class RemovableThing extends Thing {
    public void enter(Place des) {
        des.addNew(this);
        place.remove(this);

        place = des;
    }
    /**
     * Remove it from the world;
     * Currently Walls can't be removed;
     * For Mortals: use after dead() !
     */
    public int remove() {
        removeFromArrays();
        place.remove(this);
        return 1;
    }
}
