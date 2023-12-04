package byog.Core.Objects.SingleBlock;

import byog.Core.Interval;
import byog.Core.Objects.Supers.*;
import byog.Core.Objects.Supers.Interfaces.*;
import byog.Core.Place;
import byog.Core.WG;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import static byog.Core.Utils.*;

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
public class Player extends MovingDamageable implements Ally, Shooter {
    public static final int moveInterval = 200;
    public static final int attackInterval = 400;
    public static final int interactInterval = 5000;

    protected static final TETile default_avatar = Tileset.PLAYER;
    protected static final TETile damaged_avatar = Tileset.PLAYER_RED;
    protected static final TETile ghosted_avatar = Tileset.PLAYER_GHOSTED;
    protected static final int default_lumiRange = 3;
    protected static final int default_health = 5;
    protected static final int default_damageTime = 1000;

    protected boolean isObstacle = true;
    protected int lumiRange;
    protected TETile avatar;
    protected Interval attackIn;
    protected Interval interactIn;
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
        if (!wg.luminators.contains(this)) {
            wg.luminators.add(this);
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
        if (ghosted()) {
            wg.checkGameOver();
        }
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

        this.moveIn = new Interval(0);
        this.attackIn = new Interval(0);
        this.interactIn = new Interval(0);
        this.damaged = new Interval(0);
        this.ins = new Interval[] {moveIn, attackIn, interactIn, damaged};
        this.direction = Right;

        place.addNew(this);
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

    public boolean canAct() {
        return !inEntrance() && !dead() && (canMove() || canAttack() || canInteract());
    }
    @Override
    public boolean canMove() {
        return moveIn.ended();
    }
    @Override
    public boolean canAttack() {
        return !ghosted && attackIn.ended();
    }
    public boolean canInteract() {
        return !ghosted && interactIn.ended();
    }
    public boolean canSufferDamage() {
        return damaged.ended() && !ghosted();
    }
    @Override
    public int getAtk() {
        return 1;
    }
    @Override
    public TETile bulletAvatar() {
        return Tileset.BULLET;
    }

    @Override
    public int bulletAtk() {
        return getAtk();
    }

    /////////////////////////////////////////////////////////////

    /*
     * Actions
     */

    /////////////////////////////////////////////////////////////

    @Override
    public void touchedBy(Thing thing) {
        if (!isAlly(thing) && (thing instanceof Damager d)) {
            d.doDamage(this);
        }
    }
    @Override
    public void damagedBy(int atk) {
        if (!canSufferDamage()) {
            return;
        }
        int actualDamage = Math.max(atk - getArmor(), 0);
        if (actualDamage > 0) {
            health -= actualDamage;
            damaged.renew(damageTime());
        }
    }

    /**
     * Works directly with keyboard input getter;
     */
    public void act(String command) {
        switch (command) {

            // four-direction movements
            case "d", "right", "h" -> {direction = Right; move(direction);}
            case "a", "left", "f" -> {direction = Left; move(direction);}
            case "w", "up", "t" -> {direction = Up; move(direction);}
            case "s", "down", "g" -> {direction = Down; move(direction);}

            // eight-direction movements
            case "up-right" -> move(UpRight);
            case "down-right" -> move(DownRight);
            case "down-left" -> move(DownLeft);
            case "up-left" -> move(UpLeft);

            // interact
            case "k", "'" -> interact(direction);
            case "dk", "kd", "h'", "'h" -> {direction = Right; interact(direction);}
            case "ak", "ka", "f'", "'f" -> {direction = Left; interact(direction);}
            case "wk", "kw", "t'", "'t" -> {direction = Up; interact(direction);}
            case "sk", "ks", "g'", "'g" -> {direction = Down; interact(direction);}

            // attack
            case "j", ";" -> attack(direction);
            case "dj", "jd", "h;", ";h" -> {direction = Right; attack(direction);}
            case "aj", "ja", "f;", ";f" -> {direction = Left; attack(direction);}
            case "wj", "jw", "t;", ";t" -> {direction = Up; attack(direction);}
            case "sj", "js", "g;", ";g" -> {direction = Down; attack(direction);}
        }

    }

    @Override
    public int goAt(Place des) {
        des.touchedBy(this);
        if (des.canEnter()) {
            if (des.collectable()) {
                des.collect(this);
            }
            return Available;
        }
        return Unavailable;
    }

    @Override
    public int randomAction() {
        if (dead() && !ghosted()) {
            remove(); // ready to become a ghost;
            return 1;
        }
        return 0;
    }

    @Override
    protected void move(Place des) {
        if (!canMove()) {
            return;
        }

        int c = goAt(des);
        if (c == Available) { // destination is available for entering;
            enter(des);
        }

        moveIn.renew(moveInterval);
    }

    public void interact(int direc) {
        if (!canInteract()) {
            return;
        }
        setDirection(direc);
        interact(place.next(direc));
    }

    /**
     * This is protected because it does not set direction;
     */
    protected void interact(Place des) {
        if (des.getPresent() instanceof Interactable i) {
            i.interactedBy(this);
        }
//        if (des.collectable()) { // collectable item
//            des.collect(this);
//        }

        interactIn.renew(interactInterval);
    }
    public void attack(int direc) {
        if (!canAttack()) {
            return;
        }
        setDirection(direc);
        shoot(direc, place);

        attackIn.renew(attackInterval);
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

    @Override
    public void doDamage(Mortal target) {

    }
}
