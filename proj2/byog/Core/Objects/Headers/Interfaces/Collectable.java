package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

import java.io.Serializable;

public interface Collectable extends AnyThing {
    public void collectedBy(Thing thing);
}
