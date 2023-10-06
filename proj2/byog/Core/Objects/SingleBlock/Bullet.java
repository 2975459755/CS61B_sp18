package byog.Core.Objects.SingleBlock;

import byog.Core.*;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.Ally;
import byog.Core.Objects.Headers.Interfaces.HasTarget;
import byog.Core.Objects.Headers.Interfaces.Mortal;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Bullet extends Attacker {
    public static final TETile default_avatar = Tileset.BULLET;

    public Bullet() {}

    @Override
    public void addToArrays() {
        super.addToArrays();
        if (!wg.luminators.contains(this)) {
            wg.luminators.add(this);
        }
    }

    @Override
    public void removeFromArrays() {
        super.removeFromArrays();
        wg.luminators.remove(this);
    }

    @Override
    public TETile avatar() {
        return default_avatar;
    }
    @Override
    public int isLuminator() {
        return 1;
    }
    @Override
    protected void vanish() {
        if (!vanished) {
            survival.renew(0); // unlike the others, bullets disappear instantly;
            vanished = true;
        }
    }

    public Bullet (WG wg, Place place, HasTarget owner, int direction) {
        this.wg = wg;
        this.place = place;
        this.direction = direction;
        this.owner = owner;

        this.moveIn = new Interval(moveInterval);
        this.survival = new Interval(moveInterval * moveDistance);
        this.ins = new Interval[] {moveIn, survival};

        this.atk = 1;

        addToArrays();
    }
}
