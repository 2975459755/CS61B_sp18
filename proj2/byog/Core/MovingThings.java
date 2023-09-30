package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

abstract class MovingThings {
    WG wg;
    Pos pos;
    TETile avatar;
    Interval actIn;
    Interval[] ins;
    int health;

    void move(int direc) {
        move(pos.next(direc));
    }
    void move(Pos des) {
        int c = goAt(des);
        if (c == -1) {
            return; // player enters a new world;
        } else if (c == 1) { // destination is available for entering;
            if (des.isFLOOR(wg.world)) {
            /*
            Swap two tiles;
             */
                wg.world[pos.x][pos.y] = Tileset.FLOOR;
                wg.world[des.x][des.y] = avatar;
            /*
            Update status;
             */
                pos = des;
            }
        }
    }
    /**
     *
     * @return 1: Destination is available for entering;
     * 0: Can't go to destination;
     * -1: something special happens;
     */
    int goAt(Pos des) {
        return 1;
    }

    void update() {
        for (Interval in: ins) {
            in.update();
        }
    }
    boolean canAct() {
        return actIn.ended();
    }

    /**
     * This should be overriden for non-player MovingThings;
     * @return 0: no action; > 0: actioned;
     */
    int randomAction() {
        return 0;
    }

    /**
     * For non-creature MovingThings (like a bullet), this should be overriden;
     */
    int getHealth() {
        return health;
    }
    boolean dead() {
        return getHealth() == 0; // use getHealth method to get up-to-date status;
    }
    /**
     * Use after dead() !!!
     */
    int remove() {
        wg.updateMTs(this);
        wg.world[pos.x][pos.y] = Tileset.FLOOR;
        return 1;
    }
}
