package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

import java.io.Serializable;

public interface Collectable extends AnyThing {

    @Override
    default boolean isCollectable() {
        return true;
    }
    public void collectedBy(Thing thing);
}
