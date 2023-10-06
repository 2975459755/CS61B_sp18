package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Interfaces.Changeable;
import byog.Core.Objects.Headers.Interfaces.Damageable;
import byog.Core.Objects.Headers.Interfaces.HasTarget;
import byog.Core.Objects.Headers.MovingDamageable;
import byog.Core.Objects.Headers.MovingThing;
import byog.Core.Objects.Headers.RemovableThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.TileEngine.TETile;

import java.util.ArrayList;

public abstract class MultiBlock extends MovingThing implements Damageable {
    protected ArrayList<MovingThing> blocks;
    protected int size;
    protected Rectangle rectangle;
    protected Interval moveIn;
    protected Interval[] ins;
    protected int direction;

    public MultiBlock() {
        this.blocks = new ArrayList<> ();
    }

    @Override
    public abstract int damageTime();

    @Override
    public TETile defaultAvatar() {
        return null;
    }

    @Override
    public TETile damagedAvatar() {
        return null;
    }

    @Override
    public TETile avatar() {
        return null;
    }

    @Override
    public boolean isObstacle() {
        for (Thing block: blocks) {
            if (block.isObstacle()) {
                return true;
            }
        }
        return false;
    }

    public abstract int maxHealth();

    @Override
    public int change() {
        return randomAction();
    }

    public abstract int randomAction();
    @Override
    public int remove() {
        removeFromArrays();
        for (MovingThing block: blocks) {
            block.remove();
        }
        return 1;
    }

    @Override
    public boolean dead() {
        return getHealth() == 0 || noMoreBlocks();
    }

    public boolean noMoreBlocks() {
        return blocks.isEmpty();
    }

    @Override
    public void enter(Place des) {

    }
    public void move(int direc) {
        direction = direc;
        ArrayList<MovingThing> frontSide = rectangle.sides(blocks, direc);
        for (MovingThing block: blocks) {
            block.setDirection(direc);
        }
        int c = 0;
        for (int i = 0; i < frontSide.size(); i ++) {
            c += frontSide.get(i).goAt(direc);
        }
        if (c != frontSide.size()) {
            return; // some block hits obstacle;
        } else {
            rectangle = rectangle.moveTowards(direc);
            for (MovingThing block: blocks) {
                block.move(direc);
            }
        }
    }
    /**
     * Try to enter some place;
     * @return 1: Destination is available for entering;
     * 0: Can't go to destination;
     * -1: something special happens;
     */
    public abstract int goAt(Place des);
    public boolean canMove() {
        return moveIn.ended();
    }

}
