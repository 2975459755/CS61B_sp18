package byog.Core.Objects.Headers;

import java.io.Serializable;

public interface Collectable extends Serializable {
    public void collectedBy(Thing thing);
}
