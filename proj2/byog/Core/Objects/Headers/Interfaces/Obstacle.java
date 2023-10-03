package byog.Core.Objects.Headers.Interfaces;

public interface Obstacle extends AnyThing {

    @Override
    default boolean isObstacle() {
        return true;
    }
}
