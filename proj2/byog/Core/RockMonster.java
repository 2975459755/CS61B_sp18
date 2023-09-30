package byog.Core;

import byog.TileEngine.Tileset;

public class RockMonster extends MovingThings{
    static final int actionInterval = 1000;
    RockMonster(WG wg, Pos pos) {
        this.wg = wg;
        this.pos = pos;

        this.avatar = Tileset.MOUNTAIN;
        this.actIn = new Interval(0, 0);
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
