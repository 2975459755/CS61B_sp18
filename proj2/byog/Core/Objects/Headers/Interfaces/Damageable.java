package byog.Core.Objects.Headers.Interfaces;

import byog.TileEngine.TETile;

public interface Damageable extends Mortal {
    public boolean duringDamage();
    default int damageTime() {
        return 360; // when damaged, avatar changes for a moment;
    }
    public void setDamaged();
    public TETile defaultAvatar();
    public TETile damagedAvatar();
    default TETile avatar() {
        if (duringDamage()) {
            return damagedAvatar();
        }
        return defaultAvatar();
    }
}
