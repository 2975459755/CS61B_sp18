package byog.Core.Objects;

import byog.Core.Interval;
import byog.Core.Objects.Headers.*;
import byog.Core.Objects.Headers.Interfaces.Ally;
import byog.Core.Objects.Headers.Interfaces.Damager;
import byog.Core.Objects.Headers.Interfaces.Friendly;
import byog.Core.Objects.Headers.Interfaces.Interactable;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/*
When a player dies, and the other is still alive,
we set ghosted to true, remove from its place;
and he becomes a ghost in the next world;

when the other enters a new world, the ghost is alive:
it has health (so that dead() is false, and therefore canAct() is true),
it has its place, and it can see the around,
but is not an obstacle, and can't but move;

Also, the ghost doesn't need to enter the door in order to get to the next world;
 */
public class Player extends MovingDamageable implements Ally {
    public static final int actionInterval = 200;
    public static final int attackInterval = 400;

    public static final TETile default_avatar = Tileset.PLAYER;
    public static final TETile damaged_avatar = Tileset.PLAYER_RED;
    public static final TETile ghosted_avatar = Tileset.PLAYER_GHOSTED;
    public static final int default_lumiRange = 3;
    public static final int default_health = 5;
    public static final int default_damageTime = 1000;

    protected boolean isObstacle = true;
    protected int lumiRange;
    protected TETile avatar;
    protected Interval attackIn;
    protected boolean ghosted; // when there is another player alive, don't remove thoroughly;
    protected boolean inEntrance;

    /////////////////////////////////////////////////////////////

    /*
     * Constructors, getter methods, .etc
     */

    /////////////////////////////////////////////////////////////
    Player() {}


    @Override
    public void addToArrays() {
        if (!wg.keepTrackOf.contains(this)) {
            wg.keepTrackOf.add(this);
        }
        if (!wg.players.contains(this)) {
            wg.players.add(this);
            wg.players.trimToSize();
        }
    }

    /**
     * Don't remove from wg.players or wg.keepTrackOf;
     */
    @Override
    public void removeFromArrays() {
    }

    /**
     * In two-player mode, when one is dead, he should become a ghost;
     */
    @Override
    public int remove() {
        // don't remove from wg.players or wg.keepTrackOf;
        removeFromArrays();
        place.remove(this);
        ghosted = true;
        return 1;
    }

    /**
     * Override it to check in every game loop
     * that there is player alive;
     * if not, game over;
     */
    @Override
    public int change() {
        wg.checkGameOver();
        return randomAction();
    }

    public boolean ghosted() {
        return ghosted;
    }

    /**
     * Enters a new world;
     */
    public void newWorld() {
        place = wg.randomSearchFloor();
        place.addNew(this);
        inEntrance = false;

        // the dead player becomes a ghost;
        if (ghosted() && dead()) {
            setHealth(1);
            lumiRange = 1;
            isObstacle = false;
        }
    }

    public boolean inEntrance() {
        return inEntrance;
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

        addToArrays();
    }

    @Override
    public int maxHealth() {
        return default_health;
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
    public int damageTime() {
        return default_damageTime;
    }

    @Override
    public TETile avatar() {
        if (duringDamage()) {
            return damagedAvatar();
        } else if (ghosted()) {
            return ghosted_avatar;
        }
        if (avatar != null) {
            return avatar;
        }
        return defaultAvatar();
    }

    @Override
    public boolean isObstacle() {
        return isObstacle;
    }
    @Override
    public int isLuminator() {
        return lumiRange;
    }

    @Override
    public boolean canAct() {
        return !inEntrance() && !dead() && (canMove() || canAttack());
    }
    public boolean canMove() {
        return actIn.ended();
    }
    public boolean canAttack() {
        return !ghosted && attackIn.ended();
    }
    public boolean canInteract() {
        return !ghosted && actIn.ended();
    }

    /////////////////////////////////////////////////////////////

    /*
     * Actions
     */

    /////////////////////////////////////////////////////////////

    @Override
    public void touchedBy(Thing thing) {
        if (!(thing instanceof Friendly) && (thing instanceof Damager d)) {
            d.doDamage(this);
        }
    }

    /**
     * Works directly with keyboard input getter;
     */
    public void act(String command) {
        switch (command) {

            // four-direction movements
            case "d", "right", "h" -> move(0);
            case "a", "left", "f" -> move(1);
            case "w", "up", "t" -> move(2);
            case "s", "down", "g" -> move(3);

            // eight-direction movements
            case "up-right" -> move(4);
            case "down-right" -> move(5);
            case "down-left" -> move(6);
            case "up-left" -> move(7);

            // interact
            case "dk", "kd", "h'", "'h" -> interact(0);
            case "ak", "ka", "f'", "'f" -> interact(1);
            case "wk", "kw", "t'", "'t" -> interact(2);
            case "sk", "ks", "g'", "'g" -> interact(3);

            // attack
            case "dj", "jd", "h;", ";h" -> attack(0);
            case "aj", "ja", "f;", ";f" -> attack(1);
            case "wj", "jw", "t;", ";t" -> attack(2);
            case "sj", "js", "g;", ";g" -> attack(3);
        }

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
            remove(); // ready to become a ghost;
            return 1;
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

        actIn.renew(actionInterval);
    }

    public void interact(int direc) {
        interact(place.next(direc));
    }
    public void interact(Place des) {
        if (!canInteract()) {
            return;
        }

        if (des.getPresent() instanceof Interactable i) {
            i.interactedBy(this);
        }
        if (des.collectable()) { // collectable item
            des.collect(this);
        }

        actIn.renew(actionInterval);
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
    public void enterEntrance() {
        inEntrance = true;
        place.remove(this);
    }

    /**
     * The ghost becomes a human again;
     */
    public void reflesh() {
        ghosted = false;
        lumiRange = default_lumiRange;
        isObstacle = true;
    }
}
