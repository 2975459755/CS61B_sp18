package byog.Core.Objects.Supers;

import byog.Core.Objects.Supers.Interfaces.AnyThing;
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
