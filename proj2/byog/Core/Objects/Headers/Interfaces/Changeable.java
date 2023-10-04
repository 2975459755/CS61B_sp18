package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Game;

public interface Changeable extends AnyThing {
    public int change();
    default void update() {
        update(Game.miniInterval);
    }
    public <T> void update(T t);
}
