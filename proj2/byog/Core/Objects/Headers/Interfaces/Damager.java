package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface Damager extends HasTarget {
    public int getAtk();
    public void doDamage(Mortal target);
}
