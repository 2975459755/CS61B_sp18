package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.Supers.Thing;

public interface Interactable extends AnyThing {
    public void interactedBy(Thing thing);
}
