package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

class Player extends MovingThing {
    static final int actionInterval = 150;
    static final TETile default_avatar = Tileset.PLAYER;
    static final boolean isObstable = true;
    static final int default_lumiRange = 3;
    static final int default_health = 5;
    int lumiRange;
    TETile avatar;

    Player() {}
    Player(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.avatar = default_avatar;
        this.lumiRange = default_lumiRange;
        this.health = default_health;

        this.actIn = new Interval(0);
        this.ins = new Interval[]{actIn};
    }
    @Override
    TETile avatar() {
        if (avatar == null) {
            return Player.default_avatar;
        }
        return avatar;
    }

    @Override
    boolean isObstacle() {
        return Player.isObstable;
    }
    @Override
    int isLuminator() {
        return lumiRange;
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

            // attack
            case "dj", "jd" -> attack(0);
            case "aj", "ja" -> attack(1);
            case "wj", "jw" -> attack(2);
            case "sj", "js" -> attack(3);
        }

        actIn.renew(command); // TODO: don't creat a new instance, instead, update the original

    }
    @Override
    int goAt(Place des) {
        if (des.nowIs(new Key())) {
            des.restore();
            wg.door.open();
            return 1;
        } else if (des.nowIs(new Door()) && wg.door.open) {
            wg.randomWorld(false); // enters a new world
            return -1;
        } else if (des.nowIs(new Lamp())) {
            ((Lamp) des.present).lightUp();
            return 0;
        }
        return 1;
    }
    void interact(int direc) {
        interact(place.next(direc));
    }
    void interact(Place des) {
        if (des.collectable()) { // collectable item
            des.restore();
        }
    }
    void attack(int direc) {
        Place des = place.next(direc);
        if (des.nowIs(new Floor())) {
            Bullet b = new Bullet(wg, des, direc);
            des.addNew(b);
            b.goAt(des);
        }
    }
}
