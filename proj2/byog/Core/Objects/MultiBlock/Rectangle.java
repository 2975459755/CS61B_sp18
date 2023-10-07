package byog.Core.Objects.MultiBlock;

import byog.Core.Objects.Headers.Thing;
import byog.Core.Place;

import java.io.Serializable;
import java.util.ArrayList;

public class Rectangle implements Serializable {
    public Place[][] rectangle;
    public Place start; // the left bottom place;
    public int width;
    public int height;
    protected boolean valid = true;
    public Rectangle(Place start, int w, int h) {
        this.start = start;
        width = w;
        height = h;

        expandIntoRect(w, h);
    }

    /**
     * Reset rectangle based on existing start point and given w and h;
     */
    protected void expandIntoRect(int w, int h) {
        rectangle = new Place[w][h];
        rectangle[0][0] = start;
        for (int y = 0; y < h; y ++) {
            if (y != 0) {
                rectangle[0][y] = rectangle[0][y - 1].next(2);
            }
            for (int x = 1; x < w; x ++) {
                rectangle[x][y] = rectangle[x - 1][y].next(0);
                if (rectangle[x][y] == null) {
                    // invalid rectangle: out of bounds of map;
                    valid = false;
                }
            }
        }
    }
    public boolean valid() {
        return valid;
    }

    public void fillWith(Thing thing) {
        for (Place[] p: rectangle) {
            for (Place place: p) {
                place.fill(thing);
            }
        }
    }
    public int countObstacles(ArrayList<Thing> blocks) {
        int count = 0;
        for (Place[] p: rectangle) {
            for (Place place: p) {
                if (place.getPresent().isObstacle()) {
                    count ++;
                }
            }
        }
        int complement = 0;
        for (Thing block: blocks) {
            if (block.isObstacle()) {
                complement ++;
            }
        }
        return count - complement;
    }
    public ArrayList sides(ArrayList blocks, int direc) {
        switch (direc) {
            case 0 -> {return rightSide(blocks);}
            case 1 -> {return leftSide(blocks);}
            case 2 -> {return upSide(blocks);}
            default -> {return bottomSide(blocks);}
        }
    }
    public ArrayList<Thing> leftSide(ArrayList<Thing> blocks) {
        ArrayList<Thing> ret = new ArrayList<> ();
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x ++) {
                Thing thing = rectangle[x][y].getPresent();
                if (blocks.contains(thing)) {
                    ret.add(thing);
                    break;
                }
            }
        }
        return ret;
    }
    public ArrayList<Thing> rightSide(ArrayList<Thing> blocks) {
        ArrayList<Thing> ret = new ArrayList<> ();
        for (int y = 0; y < height; y ++) {
            for (int x = width - 1; x >= 0; x --) {
                Thing thing = rectangle[x][y].getPresent();
                if (blocks.contains(thing)) {
                    ret.add(thing);
                    break;
                }
            }
        }
        return ret;
    }
    public ArrayList<Thing> bottomSide(ArrayList<Thing> blocks) {
        ArrayList<Thing> ret = new ArrayList<> ();
        for (int x = 0; x < width; x ++) {
            for (int y = 0; y < height; y ++) {
                Thing thing = rectangle[x][y].getPresent();
                if (blocks.contains(thing)) {
                    ret.add(thing);
                    break;
                }
            }
        }
        return ret;
    }
    public ArrayList<Thing> upSide(ArrayList blocks) {
        ArrayList<Thing> ret = new ArrayList<> ();
        for (int x = 0; x < width; x ++) {
            for (int y = height - 1; y >= 0; y --) {
                Thing thing = rectangle[x][y].getPresent();
                if (blocks.contains(thing)) {
                    ret.add(thing);
                    break;
                }
            }
        }
        return ret;
    }
    public Rectangle moveTowards(int direc) {
        return new Rectangle(start.next(direc), width, height);
    }

    public boolean canRotate(ArrayList blocks) {
        return countObstacles(blocks) == 0;
    }

    /**
     * Rotate(mutate) rectangle field in a Rectangle instance,
     * and change blocks' places accordingly;
     */
    public void rotate(boolean clockwise, ArrayList blocks) {
        if (clockwise) {
            rotateClockwise(blocks);
        } else {
            rotateCounter(blocks);
        }
    }
    protected void rotateClockwise(ArrayList blocks) {
        /*
        Reset rectangle:
         */
        int t = width;
        width = height;
        height = t;
        expandIntoRect(width, height);
        /*
        Rotate the blocks:
         */
        for (Block block: (ArrayList<Block>) blocks) {
            Place p = block.getPlace();
            int x = p.x() - start.x();
            int y = p.y() - start.y();
            block.enter(rectangle[y][height - 1 - x]);
        }
    }
    protected void rotateCounter(ArrayList blocks) {
        /*
        Reset rectangle:
         */
        int t = width;
        width = height;
        height = t;
        expandIntoRect(width, height);
        /*
        Rotate the blocks:
         */
        for (Block block: (ArrayList<Block>) blocks) {
            Place p = block.getPlace();
            int x = p.x() - start.x();
            int y = p.y() - start.y();
            block.enter(rectangle[width - 1 - y][x]);
        }
    }
}
