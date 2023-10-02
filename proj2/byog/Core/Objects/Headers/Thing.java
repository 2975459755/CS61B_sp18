package byog.Core.Objects.Headers;

import byog.Core.Objects.Headers.Interfaces.AnyThing;
import byog.Core.Place;
import byog.Core.WG;

public abstract class Thing implements AnyThing {
    public WG wg;
    public Place place;
    public Thing() {}

    /**
     * @return whether the instance blocks way;
     */
    public abstract boolean isObstacle();

    /**
     * @return The range that instance luminates its surroundings;
     * 0 is only itself; -1 is none;
     */
    public int isLuminator() {
        return -1;
    }

    /**
     * @return whether the instance is collectable;
     */
    public boolean collectable() {
        return false;
    }
    public abstract void touchedBy(Thing thing);
}
