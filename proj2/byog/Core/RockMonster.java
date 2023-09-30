package byog.Core;

import byog.TileEngine.Tileset;

public class RockMonster extends MovingThings{
    Interval actIn;
    RockMonster(WG wg, Pos position) {
        this.wg = wg;
        this.pos = position;
        this.avatar = Tileset.MOUNTAIN;
        this.actIn = new Interval(0, 0);
        this.ins = new Interval[] {actIn};
    }

}
