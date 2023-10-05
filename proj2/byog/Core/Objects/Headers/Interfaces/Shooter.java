package byog.Core.Objects.Headers.Interfaces;

import byog.Core.Objects.Bullet;
import byog.Core.Place;

public interface Shooter extends Damager {
    default void shoot(int direc) {
        Place des = getPlace().next(direc);
        Bullet b = new Bullet(getWorld(), getWorld().randomSearchFloor(), this, direc); // set to a random floor;
        b.move(des); // try to attack and move to the place next to shooter;
    }
}
