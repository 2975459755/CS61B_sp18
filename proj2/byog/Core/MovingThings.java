package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

abstract class MovingThings {
    WG wg;
    Pos pos;
    TETile avatar;
    Interval[] ins;

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
}
