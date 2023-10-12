package byog.Core.Objects.MultiBlock;

import byog.Core.Objects.Supers.Interfaces.Damager;
import byog.Core.Objects.Supers.Interfaces.Monster;
import byog.Core.Objects.Supers.Interfaces.Obstacle;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class BlockStone extends Block implements Monster, Obstacle {
    protected int default_armor = 3;

    public BlockStone(WG wg, Place place, MultiBlock mother) {
        this.wg = wg;
        this.place = place;
        this.mother = mother;

        place.addNew(this);
        addToArrays();
    }

    @Override
    public TETile defaultAvatar() {
        return Tileset.STONE;
    }

    @Override
    public TETile damagedAvatar() {
        return Tileset.STONE_DAMAGED;
    }

    @Override
    public boolean canAttack() {
        return false;
    }

    @Override
    public int getArmor() {
        return default_armor;
    }

    @Override
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && d.isEnemy(this)) {
            // None Enemy Damager;
            d.doDamage(this);
        } else {
            mother.touchedBy(thing);
        }
    }
}
