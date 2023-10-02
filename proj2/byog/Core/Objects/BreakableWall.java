package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.Ally;
import byog.Core.Objects.Headers.Interfaces.Damager;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class BreakableWall extends ImmobileDamageable {
    public static TETile default_avatar = Tileset.WALL_BREAKABLE;
    public static TETile damaged_avatar = Tileset.WALL_DAMAGED;
    public static int default_health = 3;

    public BreakableWall() {}
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
    public TETile defaultAvatar() {
        return default_avatar;
    }

    @Override
    public TETile damagedAvatar() {
        return damaged_avatar;
    }

    public BreakableWall(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.health = default_health;
        this.damaged = new Interval(0);
        this.ins = new Interval[] {damaged};

        updateArrays();
    }
}
