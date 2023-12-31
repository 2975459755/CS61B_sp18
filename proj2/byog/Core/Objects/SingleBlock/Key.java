package byog.Core.Objects.SingleBlock;

import byog.Core.Objects.Supers.ImmobileThing;
import byog.Core.Objects.Supers.Interfaces.Collectable;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Key extends ImmobileThing implements Collectable {
    public static TETile avatar = Tileset.KEY;

    public Key() {}

    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

    }

    @Override
    public TETile avatar() {
        return Key.avatar;
    }

    @Override
    public void touchedBy(Thing thing) {

    }

    @Override
    public void collectedBy(Thing thing) {
        if (thing instanceof Player) {
            remove();
            wg.door.open();
        }
    }

    public Key(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        place.addNew(this);
        addToArrays();
    }

}
