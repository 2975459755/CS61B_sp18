package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.SingleBlock.Bullet;
import byog.Core.Place;

public interface Shooter extends Damager {
    default void shoot(int direc) {
        Bullet b = new Bullet(getWorld(), getWorld().randomSearchFloor(), this, direc); // set to a random floor;
        b.move(direc); // try to attack and move to the place next to shooter;
    }
}
