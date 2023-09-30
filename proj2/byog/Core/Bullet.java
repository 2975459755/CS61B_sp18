package byog.Core;

import byog.TileEngine.Tileset;

public class Bullet extends MovingThings {
    static final int actionInterval = 240;
    static final int moveDistance = 4;
    Interval survival;
    int direction;

    Bullet (WG wg, Pos pos, int direction) {
        this.wg = wg;
        this.pos = pos;
        this.direction = direction;

        this.avatar = Tileset.WATER;
        this.actIn = new Interval(0, actionInterval);
        this.survival = new Interval(0, actionInterval * moveDistance);
        this.ins = new Interval[] {actIn, survival};
        this.health = 1; // this does not mean anything;
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
    int goAt(Pos des) {
        if (des.isFLOOR(wg.world)) {
            return 1;
        }
        return 0;
    }
}
