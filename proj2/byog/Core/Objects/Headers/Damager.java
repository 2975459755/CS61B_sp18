package byog.Core.Objects.Headers;

import java.io.Serializable;

public interface Damager extends Serializable {
    public int getAtk();
    public void doDamage(Mortal target);
}
