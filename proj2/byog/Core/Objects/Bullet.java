package byog.Core.Objects;

import byog.Core.*;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.Ally;
import byog.Core.Objects.Headers.Interfaces.Friendly;
import byog.Core.Objects.Headers.Interfaces.Mortal;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Bullet extends Attacker implements Ally {
    public static final TETile default_avatar = Tileset.BULLET;

    public Bullet() {}

    @Override
    public boolean isTarget(Thing thing) {
        return !(thing instanceof Friendly) && (thing instanceof Mortal);
    }


    @Override
    public TETile avatar() {
        return default_avatar;
    }
    @Override
    public int isLuminator() {
        return 1;
    }

    public Bullet (WG wg, Place place, int direction) {
        this.wg = wg;
        this.place = place;
        this.direction = direction;

        this.actIn = new Interval(actionInterval);
        this.survival = new Interval(actionInterval * moveDistance);
        this.ins = new Interval[] {actIn, survival};

        this.atk = 1;

        updateArrays();
    }
}
