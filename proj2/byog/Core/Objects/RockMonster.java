package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Mortal;
import byog.Core.Objects.Headers.MovingThing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RockMonster extends MovingThing implements Mortal {
    public static final int actionInterval = 1000;
    static final TETile default_avatar = Tileset.MOUNTAIN;
    static int default_health = 3;
    private int health;

    RockMonster() {}

    @Override
    public void updateArrays() {
        wg.updMTs(this);
    }

    @Override
    public TETile avatar() {
        return RockMonster.default_avatar;
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    public RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.actIn = new Interval(0);
        this.ins = new Interval[] {actIn};
        this.health = default_health;

        updateArrays();
    }

    @Override
    public int randomAction() {
        return wander();
    }
    public int wander() {
        if (!canAct()) {
            return 0;
        }
        move(wg.rand.nextInt(4));
        actIn.renew(actionInterval);
        return 1;
    }

    @Override
    public int getHealth() {
        return health;
    }
}
