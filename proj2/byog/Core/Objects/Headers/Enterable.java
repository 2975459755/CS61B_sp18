package byog.Core.Objects.Headers;

import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Objects.Headers.Interfaces.Obstacle;
import byog.Core.Objects.Player;
import byog.TileEngine.TETile;

import java.util.ArrayList;

public abstract class Enterable extends FixedThing implements Changeable, Obstacle {

    protected boolean open;
    protected ArrayList<Player> insidePlayers;

    public Enterable() {
        insidePlayers = new ArrayList<> ();
    }
    @Override
    public void updateArrays() {
        wg.updTrack(this);
    }

    @Override
    public void touchedBy(Thing thing) {
        if (open && (thing instanceof Player p)) {
            p.enterEntrance();
            insidePlayers.remove(p); // prevent from adding twice;
            insidePlayers.add(p);
            check();
        }
    }

    public void open() {
        open = true;
    }
    public void close() {
        open = false;
    }

    public boolean isEntered() {
        return !insidePlayers.isEmpty();
    }

    /**
     * Check whether all non-ghost player has entered this;
     */
    protected int check() {
        for (Player player: wg.players) {
            if (!player.ghosted() && !player.inEntrance()) {
                return 0;
            }
        }
        // enters a new world
        if (!insidePlayers.isEmpty()) {
            newWorld();
        } else {
            // there are more than one entrance, one player entered another,
            // the other player died later, but this is checked first;
            Enterable en = this; // the right entrance must not be this, but initialization is forced;
            for (Thing t: wg.keepTrackOf) {
                if ((t instanceof Enterable e) && (e.isEntered())) {
                    en = e;
                    break; // prevent from possible? iteration Exception;
                }
            }
            en.newWorld();
        }

        return 1;
    }
    public abstract void newWorld();

    @Override
    public int change() {
        return check();
    }

    @Override
    public <T> void update(T t) {

    }
}
