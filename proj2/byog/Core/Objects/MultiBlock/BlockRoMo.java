package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class BlockRoMo extends Block implements Obstacle {
    protected static final int moveInterval = 1000;

    protected static final TETile[] default_avatar = {
            Tileset.ROMO_RIGHT, Tileset.ROMO_LEFT, Tileset.ROMO_UP, Tileset.ROMO_DOWN};
    protected static final TETile[] damaged_avatar = {
            Tileset.ROMO_RIGHT_DAMAGED, Tileset.ROMO_LEFT_DAMAGED,
            Tileset.ROMO_UP_DAMAGED, Tileset.ROMO_DOWN_DAMAGED};
    protected static int default_health = 3;

    public BlockRoMo(WG wg, Place place, MultiBlock m) {
        this.wg = wg;
        this.place = place;
        this.mother = m;

        this.moveIn = new Interval(0);

        this.ins = new Interval[] {moveIn};

        addToArrays();
    }
    @Override
    public TETile avatar() {
        return default_avatar[direction];
    }

    @Override
    public void touchedBy(Thing thing) {
        mother.touchedBy(thing);
    }

    @Override
    public int randomAction() {
        return 0;
    }
}
