package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface Damager extends Changeable {
    public int getAtk();
    public boolean isTarget(Thing thing);
    public void doDamage(Mortal target);
}
