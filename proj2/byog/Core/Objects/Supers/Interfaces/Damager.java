package byog.Core.Objects.Supers.Interfaces;

public interface Damager extends HasTarget {
    public int getAtk();
    public void doDamage(Mortal target);
    public boolean canAttack();
}
