package byog.Core.Objects.Headers.Interfaces;

public interface Damager extends Changeable {
    public int getAtk();
    public void doDamage(Mortal target);
}
