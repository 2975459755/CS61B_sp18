package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Supers.Interfaces.Damager;
import byog.Core.Objects.Supers.Interfaces.HasTarget;
import byog.Core.Objects.Supers.MovingDamageable;
import byog.Core.Objects.Supers.MovingThing;
import byog.Core.Objects.Supers.Thing;
import byog.Core.Place;
import byog.TileEngine.TETile;
import java.util.ArrayList;

public abstract class MultiBlock extends MovingDamageable implements HasTarget, Damager {
    protected ArrayList<Block> blocks;
    protected Rectangle rectangle;
    protected int direction;

    public MultiBlock() {
        this.blocks = new ArrayList<> ();
        blocks.trimToSize();
    }

    @Override
    public void damagedBy(int atk) {
        int actualDamage = Math.max(atk - getArmor(), 0);
        if (actualDamage > 0) {
            health -= actualDamage;
            damaged.renew(damageTime());
            setAllDamaged(); // change appearance of every block;
        }
    }
    protected void setAllDamaged() {
        for (Block block: blocks) {
            block.setDamaged();
        }
    }

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
    @Override
    public void move(int direc) {
        direction = direc;
        ArrayList<MovingThing> frontSide = rectangle.sides(blocks, direc);
        // Reset direction of every block;
        for (MovingThing block: blocks) {
            block.setDirection(direc);
        }
        // Count number of obstacles in front of the frontSide;
        int c = 0;
        for (int i = 0; i < frontSide.size(); i ++) {
            c += frontSide.get(i).goAt(direc);
        }
        if (c != frontSide.size()) {
            return; // some block hits obstacle;
        } else {
            // Move every block, and move the rectangle;
            rectangle = rectangle.moveTowards(direc);
            for (MovingThing block: blocks) {
                block.move(direc);
            }
        }
    }
    @Override
    public abstract int goAt(Place des);
    @Override
    public boolean canMove() {
        return moveIn.ended();
    }
    public boolean canRotate() {
        return rectangle.canRotate(blocks);
    }
    public void rotate(boolean clockwise) {
        if (canRotate()) {
            rectangle.rotate(clockwise, blocks);
        }
    }

    @Override
    public <T> void update(T t) {
        for (Interval in: ins) {
            in.update(t);
        }
        for (Block block: blocks) {
            block.update(t);
        }
    }
}
