package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Bullet extends MovingThing {
    static final int actionInterval = 240;
    static final int moveDistance = 4;
    static final TETile default_avatar = Tileset.WATER;
    Interval survival;
    int direction;


    Bullet() {}

    @Override
    TETile avatar() {
        return default_avatar;
    }

    @Override
    boolean isObstacle() {
        return false;
    }
    @Override
    int isLuminator() {
        return 0;
    }

    Bullet (WG wg, Place place, int direction) {
        this.wg = wg;
        this.place = place;
        this.direction = direction;

        this.actIn = new Interval(actionInterval);
        this.survival = new Interval(actionInterval * moveDistance);
        this.ins = new Interval[] {actIn, survival};
        this.health = 1; // this does not mean anything;

        wg.updMTs(this); // bullet is mts
        wg.updLuminators(this); // bullet is lumi
    }

    @Override
    int getHealth() {
        if (survival.ended()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    int randomAction() {
        if (dead()) {
            return remove();
        }
        if (! canAct()) {
            return 0;
        }

        move(direction);
        actIn.renew(actionInterval);
        return 1;
    }

    @Override
    int goAt(Place des) {
        if (des.canEnter()) {
            return 1;
        }
        return 0;
    }
    @Override
    int remove() {
        wg.updMTs(this); // RoMo is mts;
        wg.updLuminators(this); // RoMo is lumi
        place.restore();
        return 1;
    }
}
