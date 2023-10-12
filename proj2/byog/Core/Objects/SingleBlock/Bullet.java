package byog.Core.Objects.SingleBlock;

import byog.Core.*;
import byog.Core.Objects.Supers.*;
import byog.Core.Objects.Supers.Interfaces.HasTarget;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Bullet extends Attacker {

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
        return avatar;
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

    public Bullet (WG wg, Place place, HasTarget owner, int direction, TETile avatar, int atk) {
        this.wg = wg;
        this.place = place;
        this.direction = direction;
        this.owner = owner;
        this.atk = atk;
        this.avatar = avatar;

        this.moveIn = new Interval(moveInterval);
        this.survival = new Interval(moveInterval * moveDistance);
        this.ins = new Interval[] {moveIn, survival};

        addToArrays();
    }

}
