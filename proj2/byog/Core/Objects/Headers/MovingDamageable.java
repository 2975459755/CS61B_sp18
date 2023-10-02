package byog.Core.Objects.Headers;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Damageable;

public abstract class MovingDamageable extends MovingThing implements Damageable {
    protected Interval damaged;
    protected int health;
    public MovingDamageable() {}
    @Override
    public void damagedBy(Thing thing) {

    }
    @Override
    public void damagedBy(int atk) {
        health -= atk;
        damaged.renew(damageTime());
    }
    @Override
    public boolean duringDamage() {
        return !damaged.ended();
    }
}
