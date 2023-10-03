package byog.Core.Objects.Headers;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Damageable;

public abstract class MovingDamageable extends MovingThing implements Damageable {
    protected Interval damaged;
    protected int health;
    public MovingDamageable() {}
    public abstract int maxHealth();
    public void restoreFullHealth() {
        setHealth(maxHealth());
    }
    public void heal(int value) {
        setHealth(Math.max(health + 1, maxHealth()));
    }
    public void setHealth(int value) {
        health = value;
    }

    @Override
    public int getHealth() {
        return health;
    }
    public int getArmor() {
        return 0;
    }
    @Override
    public void damagedBy(Thing thing) {

    }
    @Override
    public void damagedBy(int atk) {
        int actualDamage = Math.max(atk - getArmor(), 0);
        if (actualDamage > 0) {
            health -= actualDamage;
            damaged.renew(damageTime());
        }
    }
    @Override
    public boolean duringDamage() {
        return !damaged.ended();
    }
}
