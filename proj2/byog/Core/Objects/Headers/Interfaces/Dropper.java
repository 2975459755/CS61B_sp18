package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface Dropper extends Changeable {
    public void drop(Thing thing, int direction);
}
