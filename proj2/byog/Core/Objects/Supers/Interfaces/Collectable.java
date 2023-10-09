package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.Supers.Thing;

public interface Collectable extends AnyThing {

    @Override
    default boolean isCollectable() {
        return true;
    }
    public void collectedBy(Thing thing);
}
