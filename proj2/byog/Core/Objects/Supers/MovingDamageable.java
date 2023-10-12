package byog.Core.Objects.Supers;

import byog.Core.Interval;
import byog.Core.Objects.Supers.Interfaces.Damageable;
import byog.Core.Objects.Supers.Interfaces.Damager;

public abstract class MovingDamageable extends MovingThing implements Damageable {
    protected Interval damaged;
    protected int health;
    public MovingDamageable() {
        this.damaged = new Interval(0);
        this.ins = new Interval[] {moveIn, damaged};
    }
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
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && d.isEnemy(this)) {
            d.doDamage(this);
        }
    }
    @Override
    public void damagedBy(Thing thing) {

    }
    @Override
    public void damagedBy(int atk) {
        int actualDamage = Math.max(atk - getArmor(), 0);
        if (actualDamage > 0) {
            health -= actualDamage;
            setDamaged();
        }
    }
    @Override
    public void setDamaged() {
        damaged.renew(damageTime());
    }
    @Override
    public boolean duringDamage() {
        return !damaged.ended();
    }
    @Override
    public int change() {
        if (dead()) {
            return remove();
        } else if (!canAct()) {
            return 0;
        }
        return randomAction();
    }
}
