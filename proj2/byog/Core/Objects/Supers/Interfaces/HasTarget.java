package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.Supers.Thing;

public interface HasTarget extends Changeable {
    public boolean isEnemy(Thing thing);
    public boolean isAlly(Thing thing);
}
