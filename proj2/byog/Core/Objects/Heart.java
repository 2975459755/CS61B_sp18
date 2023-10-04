package byog.Core.Objects;

import byog.Core.Objects.Headers.ImmobileThing;
import byog.Core.Objects.Headers.Interfaces.Collectable;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Heart extends ImmobileThing implements Collectable {

    public Heart(WG wg, Place place) {
        this.wg = wg;
        this.place = place;
    }

    @Override
    public void addToArrays() {

    }

    @Override
    public void removeFromArrays() {

    }

    @Override
    public TETile avatar() {
        return Tileset.HEART;
    }

    @Override
    public void collectedBy(Thing thing) {
        if (thing instanceof Player p) {
            p.heal(1);
            remove();
        }
    }

    @Override
    public void touchedBy(Thing thing) {

    }
}
