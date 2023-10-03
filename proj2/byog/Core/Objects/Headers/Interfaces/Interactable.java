package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface Interactable extends AnyThing {
    public void interactedBy(Thing thing);
}
