package byog.Core.Objects.Supers.Interfaces;

public interface Obstacle extends AnyThing {

    @Override
    default boolean isObstacle() {
        return true;
    }
}
