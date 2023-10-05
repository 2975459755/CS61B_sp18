package byog.Core.Objects.Headers;

import byog.Core.Objects.Headers.Interfaces.AnyThing;
import byog.Core.Place;
import byog.Core.WG;

public abstract class Thing implements AnyThing {
    protected WG wg;
    protected Place place;
    public Thing() {}

    @Override
    public Place getPlace() {
        return place;
    }
    @Override
    public WG getWorld() {
        return wg;
    }
}
