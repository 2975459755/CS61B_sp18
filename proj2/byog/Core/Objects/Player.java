package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.Ally;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Player extends MovingDamageable implements Ally {
    public static final int actionInterval = 150;
    public static final int attackInterval = 500;

    public static final TETile default_avatar = Tileset.PLAYER;
    public static final TETile damaged_avatar = Tileset.PLAYER_RED;
    private static final boolean isObstacle = true;
    public static final int default_lumiRange = 3;
    public final int default_health = 5;
    public int lumiRange;
    private TETile avatar;
    Interval attackIn;

    /////////////////////////////////////////////////////////////

    /*
     * Constructors, getter methods
     */

    /////////////////////////////////////////////////////////////
    Player() {}
    @Override
    public void updateArrays() {
        wg.updTrack(this);
        wg.updArray(wg.players, this);
    }

    public Player(WG wg, Place place) {
        this.wg = wg;
        this.place = place;

        this.avatar = default_avatar;
        this.lumiRange = default_lumiRange;
        this.health = default_health;

        this.actIn = new Interval(0);
        this.attackIn = new Interval(0);
        this.damaged = new Interval(0);
        this.ins = new Interval[]{actIn, attackIn, damaged};

        updateArrays();
    }

    @Override
    public TETile defaultAvatar() {
        return default_avatar;
    }

    @Override
    public TETile damagedAvatar() {
        return damaged_avatar;
    }

    @Override
    public TETile avatar() {
        if (duringDamage()) {
            return damagedAvatar();
        }
        if (avatar != null) {
            return avatar;
        }
        return defaultAvatar();
    }

    @Override
    public boolean isObstacle() {
        return Player.isObstacle;
    }
    @Override
    public int isLuminator() {
        return lumiRange;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean canAct() {
        return canMove() || canAttack();
    }
    public boolean canMove() {
        return actIn.ended();
    }
    public boolean canAttack() {
        return attackIn.ended();
    }

    /////////////////////////////////////////////////////////////

    /*
     * Actions
     */

    /////////////////////////////////////////////////////////////

    @Override
    public void touchedBy(Thing thing) {

    }

    @Override
    public void damagedBy(int atk) {
        if (!duringDamage()) {
            health -= atk;
            damaged.renew(1000);
//            StdDraw.pause(800);
        }
    }

    /**
     * Works directly with keyboard input getter;
     */
    public void act(String command) {
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

        actIn.renew(actionInterval); // don't creat a new instance, instead, update the original

    }

    @Override
    public int goAt(Place des) {
        des.touchedBy(this);
        if (des.canEnter()) {
            if (des.collectable()) {
                des.collect(this);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public int randomAction() {
        if (dead()) {
            System.exit(0); // end the game;
        }
        return 0;
    }

    @Override
    public void move(Place des) {
        if (!canMove()) {
            return;
        }

        int c = goAt(des);
        if (c == 1) { // destination is available for entering;
            enter(des);
        }
    }

    public void interact(int direc) {
        interact(place.next(direc));
    }
    public void interact(Place des) {
        if (des.collectable()) { // collectable item
            des.collect(this);
        }
    }
    public void attack(int direc) {
        if (!canAttack()) {
            return;
        }

        shoot(direc);

        attackIn.renew(attackInterval);
    }
    public void shoot(int direc) {
        Place des = place.next(direc);
        Bullet b = new Bullet(wg, wg.randomSearchFloor(), direc); // set to a random floor;
        b.move(des); // try to attack and move to the place next to Player;
    }

}
