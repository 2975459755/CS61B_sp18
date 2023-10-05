package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

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
