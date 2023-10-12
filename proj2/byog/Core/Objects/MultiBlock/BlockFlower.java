package byog.Core.Objects.MultiBlock;

import byog.Core.Objects.Supers.Interfaces.HasSight;
import byog.Core.Objects.Supers.Interfaces.Monster;
import byog.Core.Objects.Supers.Interfaces.Obstacle;
import byog.Core.Objects.Supers.Interfaces.Shooter;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class BlockFlower extends Block implements Monster, Shooter, Obstacle, HasSight {

    protected final int default_sight = 6;
    public BlockFlower(WG wg, Place place, MultiBlock mother) {
        this.wg = wg;
        this.place = place;
        this.mother = mother;

        place.addNew(this);
        addToArrays();
    }
    @Override
    public TETile defaultAvatar() {
        return Tileset.FLOWER_DEFAULT;
    }

    @Override
    public TETile damagedAvatar() {
        return Tileset.FLOWER_DAMAGED;
    }

    @Override
    public boolean canAttack() {
        return mother.canAttack();
    }

    @Override
    public TETile bulletAvatar() {
        return Tileset.BULLET_FLOWER;
    }

    @Override
    public int bulletAtk() {
        return mother.getAtk();
    }

    @Override
    public boolean sees(int direc) {
        Place spot = this.place;
        for (int i = 0; i < sight(); i ++) {
            spot = spot.next(direc);
            if (spot == null) {
                break;
            } else if (isEnemy(spot.getPresent())) {
                return true;
            } else if (spot.getPresent().isObstacle()) {
                break;
            }
        }
        return false;
    }

    @Override
    public int sight() {
        return default_sight;
    }
}
