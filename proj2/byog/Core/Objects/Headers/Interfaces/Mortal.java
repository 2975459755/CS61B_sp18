package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Headers.Thing;

public interface Mortal extends Changeable {
    public int getHealth();
    public void damagedBy(Thing thing);
    public void damagedBy(int atk);
    public default boolean dead() {
        return getHealth() == 0; // use getHealth method to get up-to-date status;
    }
}
