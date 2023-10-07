package byog.Core.Objects.MultiBlock;

import byog.Core.Interval;
import byog.Core.Objects.Headers.Attacker;
import byog.Core.Objects.Headers.Interfaces.*;
import byog.Core.Objects.Headers.MovingThing;
import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;
import byog.Core.WG;

import java.util.ArrayList;

public class TwinRoMo extends MultiBlock implements Monster {
    protected static final int moveInterval = 1000;
    protected static int atk = 1;
    protected static int default_health = 3;
    public TwinRoMo(WG wg, Rectangle rect) {
        this.wg = wg;
        this.rectangle = rect;

        this.health = default_health;

        for (int x = 0; x < rect.width; x ++) {
            if (blocks.size() == 2) {
                break;
            }
            for (int y = 0; y < rect.height; y ++) {
                Place place = rect.rectangle[x][y];
                BlockRoMo block = new BlockRoMo(wg, place, this);
                blocks.add(block);
                break;
            }
        }

        direction = 0; // initial state: left-right oriented;
        setOrientation();

        addToArrays();
    }
    protected void setOrientation() {
        if (direction < 2) {
            // left-right;
            setLeftRight();
        } else {
            // up-down;
            setUpDown();
        }
    }
    protected void setLeftRight() {
        ArrayList<Block> side = rectangle.sides(blocks, 0);
        for (Block block: side) {
            block.setDirection(0);
        }
        side = rectangle.sides(blocks, 1);
        for(Block block: side) {
            block.setDirection(1);
        }
    }
    protected void setUpDown() {
        ArrayList<Block> side = rectangle.sides(blocks, 2);
        for (Block block: side) {
            block.setDirection(2);
        }
        side = rectangle.sides(blocks, 3);
        for(Block block: side) {
            block.setDirection(3);
        }
    }
    @Override
    public void touchedBy(Thing thing) {
        if ((thing instanceof Damager d) && d.isEnemy(this)) {
            // None Enemy Damager;
            d.doDamage(this);
        }
        if (isEnemy(thing)) {
            // Mortal Friendly;
            doDamage((Damageable) thing);
        }
    }

    @Override
    public int maxHealth() {
        return default_health;
    }

    @Override
    public int randomAction() {
        if (dead()) {
            return remove();
        }
        if (!canMove()) {
            return 0;
        }
        return wander();
    }
    public int wander() {
        int direc = wg.rand.nextInt(4);
        int diff = direction - direc;
        if (Math.abs(diff) <= 1 && direction > 1 == direc > 1) {
            direction = direc;
            move(direction);
        } else {
            if ((diff == 2 && direction == 0 || direction == 1)
                || (diff == -1 && direction == 2)
                || (diff == -3 && direction == 3)) {
                direction = direc;
                rotate(false);
            } else {
                direction = direc;
                rotate(true);
            }
        }
        moveIn.renew(moveInterval);
        return 1;
    }

    @Override
    public void move(int direc) {
        direction = direc;
        ArrayList<MovingThing> frontSide = rectangle.sides(blocks, direc);
        // Don't reset direction of every block;
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
    public void rotate(boolean clockwise) {
        if (rectangle.canRotate(blocks)) {
            rectangle.rotate(clockwise, blocks);
            setOrientation();
        }
    }

    @Override
    public int goAt(Place des) {
        return 0;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void doDamage(Mortal target) {
        target.damagedBy(getAtk());
    }
}