package byog.Core;

import byog.TileEngine.Tileset;

class Player extends MovingThings{
    static final int actionInterval = 150;

    Interval actIn;

    Player(WG wg, Pos position) {
        this.wg = wg;
        this.pos = position;
        this.avatar = Tileset.PLAYER;

        actIn = new Interval(0, 0);
        ins = new Interval[]{actIn};
    }

    boolean canAct() {
        return actIn.ended();
    }
    void act(String command) {
        switch (command) {
            // four-direction movements
            case "d", "right" -> move(0);
            case "a", "left" -> move(1);
            case "w", "up" -> move(2);
            case "s", "down" -> move(3);

            // eight-direction movements
            case "up-right" -> move(4);
            case "down-right" -> move(5);
            case "down-left" -> move(6);
            case "up-left" -> move(7);

            // interact
            case "dk", "kd" -> interact(0);
            case "ak", "ka" -> interact(1);
            case "wk", "kw" -> interact(2);
            case "sk", "ks" -> interact(3);
        }

        actIn.renew(command); // TODO: don't creat a new instance, instead, update the original

    }
    @Override
    int goAt(Pos des) {
        if (des.isTile(wg.world, Tileset.FLOWER)) {
            wg.world[des.x][des.y] = Tileset.FLOOR;
            Pos d = WG.doorPos;
            wg.world[d.x][d.y] = Tileset.UNLOCKED_DOOR;
            return 1;
        } else if (des.isTile(wg.world, Tileset.UNLOCKED_DOOR)) {
            wg.randomWorld(); // enters a new world
            return -1;
        } else if (des.isTile(wg.world, Tileset.LAMP_UNLIT)) {
            wg.world[des.x][des.y] = Tileset.LAMP_LIT;
            return 0;
        }
        return 1;
    }
    void interact(int direc) {
        interact(pos.next(direc));
    }
    void interact(Pos des) {
        if (des.collectable(wg.world)) { // collectable item
            wg.world[des.x][des.y] = Tileset.FLOOR;
        }
    }

}
