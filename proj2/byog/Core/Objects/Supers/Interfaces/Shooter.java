package byog.Core.Objects.Supers.Interfaces;

import byog.Core.Objects.SingleBlock.Bullet;
import byog.Core.Place;
import byog.TileEngine.TETile;

public interface Shooter extends Damager {
    public TETile bulletAvatar();
    public int bulletAtk();

    /**
     *
     * @param direc Direction of bullet;
     * @param place Place of the shooter; Used to derive the bullet's initial place;
     */
    default void shoot(int direc, Place place) {
        Bullet b = new Bullet(getWorld(), getWorld().randomSearchFloor(),
                this, direc, bulletAvatar(), bulletAtk()); // set to a random floor;
        b.move(place.next(direc)); // try to attack and move to the place next to shooter;
    }
}
