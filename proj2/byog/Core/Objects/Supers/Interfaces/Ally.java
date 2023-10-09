package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.Supers.Thing;

public interface Ally extends HasTarget {
    @Override
    default boolean isEnemy(Thing thing) {
        return !(thing instanceof Ally) && (thing instanceof Mortal);
    }
    @Override
    default boolean isAlly(Thing thing) {
        return thing instanceof Ally;
    }
}
