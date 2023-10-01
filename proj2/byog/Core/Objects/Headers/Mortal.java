package byog.Core.Objects.Headers;

import java.io.Serializable;

public interface Mortal extends Serializable {
    public int getHealth();
    public void damagedBy(Thing thing);
    public void damagedBy(int atk);
    public default boolean dead() {
        return getHealth() == 0; // use getHealth method to get up-to-date status;
    }
}
