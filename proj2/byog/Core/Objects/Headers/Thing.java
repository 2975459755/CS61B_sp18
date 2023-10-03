package byog.Core.Objects.Headers;

import byog.Core.Objects.Headers.Interfaces.AnyThing;
import byog.Core.Place;
import byog.Core.WG;

public abstract class Thing implements AnyThing {
    public WG wg;
    public Place place;
    public Thing() {}
}
