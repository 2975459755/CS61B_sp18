package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class RockMonster extends MovingThing {
    static final int actionInterval = 1000;
    static final TETile default_avatar = Tileset.MOUNTAIN;

    static int isLuminator = 0;

    RockMonster() {}

    @Override
    TETile avatar() {
        return RockMonster.default_avatar;
    }

    @Override
    boolean isObstacle() {
        return true;
    }

    RockMonster(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.actIn = new Interval(0);
        this.ins = new Interval[] {actIn};
        this.health = 1;
    }

    @Override
    int randomAction() {
        return wander();
    }
    int wander() {
        if (!canAct()) {
            return 0;
        }
        move(wg.rand.nextInt(4));
        actIn.renew(actionInterval);
        return 1;
    }
}
