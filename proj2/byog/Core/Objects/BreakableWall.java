package byog.Core.Objects;

import byog.Core.Objects.Headers.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class BreakableWall extends StaticThing implements Mortal {
    public static TETile avatar = Tileset.WALL_BREAKABLE;
    public static int default_health = 3;
    private int health;

    public BreakableWall() {}
    @Override
    public void updateArrays() {

    }
    @Override
    public TETile avatar() {
        return BreakableWall.avatar;
    }
    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public void touchedBy(Thing thing) {
        if ((thing instanceof Ally) && (thing instanceof Damager d)) {
            d.doDamage(this);
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void damagedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {
        health -= atk;
        if (health <= 0) {
            remove();
        }
    }

    public BreakableWall(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.health = default_health;
    }
}
