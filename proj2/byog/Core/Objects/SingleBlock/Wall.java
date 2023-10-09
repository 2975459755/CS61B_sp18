package byog.Core.Objects.SingleBlock;

import byog.Core.Objects.Supers.FixedThing;
import byog.Core.Objects.Supers.Interfaces.Obstacle;
import byog.Core.Objects.Supers.Thing;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Wall extends FixedThing implements Obstacle {
    public static TETile avatar = Tileset.WALL;
    public Wall() {}

    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

    }

    @Override
    public TETile avatar() {
        return Wall.avatar;
    }

    @Override
    public void touchedBy(Thing thing) {

    }

    public Wall(WG wg) {
        this.wg= wg;

        addToArrays();
    }
}
