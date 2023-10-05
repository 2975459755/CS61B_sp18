package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface HasTarget extends Changeable {
    public boolean isEnemy(Thing thing);
    public boolean isAlly(Thing thing);
}
