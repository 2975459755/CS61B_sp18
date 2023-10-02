package byog.Core.Objects.Headers;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Damageable;

public abstract class ImmobileDamageable extends ImmobileThing implements Damageable {
    protected Interval[] ins;
    protected Interval damaged;
    protected int health;
    public ImmobileDamageable() {}
    @Override
    public void updateArrays() {
        wg.updTrack(this);
    }
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
    @Override
    public int change() {
        if (dead()) {
            remove();
            return 1;
        }
        return 0;
    }

    @Override
    public <T> void update(T t) {
        for (Interval in: ins) {
            in.update(t);
        }
    }
}
