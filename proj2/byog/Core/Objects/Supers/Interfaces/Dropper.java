package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.Supers.Thing;

public interface Dropper extends Changeable {
    public void drop(Thing thing, int direction);
}
