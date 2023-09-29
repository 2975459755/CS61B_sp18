package byog.Core;

import byog.TileEngine.Tileset;

public class RockMonster extends MovingThings{
    RockMonster(WG wg, Pos position) {
        this.wg = wg;
        this.pos = position;
        this.avatar = Tileset.MOUNTAIN;
    }

}
