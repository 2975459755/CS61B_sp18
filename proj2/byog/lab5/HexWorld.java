package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int width = 64;
    private static final int height = 40;
    private static final Random RANDOM = new Random();

    private static final int s = 6; // The length of the side of hexagons
    private static final int length = s + (s - 1) * 2; // The length of the rows
    private static final int n = 15; // number of hexagons

    public static void addHexagon(int s, int w, int h, TETile[][] world, int c) {
        // view a hexagon as a rectangle with spaces
        // c determines the type of tiles to draw
        for (int y = h; y < h + 2 * s; y++) { // y: rows of a hexagon
            for (int x = w; x < w + length; x++) { // x: columns of a hexagon
                if (hex(s, x - w, y - h)) { // whether the point in this rectangle is in the hexagon
                    if (!outBounds(x, y)) {
                        world[x][y] = tile(c);
                    }
                }
            }
        }
    }
    private static boolean outBounds(int w, int h) {
        // check if index is out of bounds
        return w >= width || w < 0 || h >= height || h < 0;
    }
    /*
    return a certain tpye of TETile
     */
    private static TETile tile(int c) {
        switch (c) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WATER;
            default: return Tileset.FLOOR;
        }
    }
    /*
    helper for addHexagon
    check whether the point is in the hexagon
     */
    public static boolean hex(int s, int x, int y) {
        // zero-index
        int length = s + (s - 1) * 2;
        if (y > s - 1) {
            y = 2 * s - y - 1;
        }
        y = s - 1 - y;
        if (x >= y && x <= length - 1 - y) {
            return true;
        }
        return false;
    }
    /*
    set the world
     */
    public static void buildWorld(TETile[][] world) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    /*
    draw n hexagons that are side by side
     */
    public static void hexagons(TETile[][] world) {
        /* start point: */
        int w = 0;
        int h = 0;

        int count = 0; // number of hexagons
        boolean [][] used = new boolean[width][height]; // store the start points that have been used

        while (count < n) {
            addHexagon(s, w, h, world, RANDOM.nextInt(5)); // use random tile
            used[w][h] = true;
            while (used[w][h]) {
                int[] next = next(w, h);
                w = next[0]; h = next[1];
            }
            count ++;
        }
    }
    /*
    the next starting point to draw
    return a int[] with 2 elements: width and height
     */
    private static int[] next(int w, int h) {
        int[] res = new int[2];
        int c = RANDOM.nextInt(4);
        switch (c) {
            // go up right
            case 0: res[0] = w + s; res[1] = h + s;
                break;
            case 1: res[0] = w - s; res[1] = h + s;
                break;
            case 2: res[0] = w - s; res[1] = h - s;
                break;
            default: res[0] = w + s; res[1] = h - s;
                break;
        }
        if (outBounds(res[0], res[1])) {
            return next(w, h);
        }
        return res;
    }
    public static void main(String [] args) {
        /*
        Initialize
         */
        TERenderer ter = new TERenderer();
        ter.initialize(width ,height);
        /*
        Build world
         */
        TETile[][] world = new TETile[width][height];
        buildWorld(world);
        /*
        Draw hexagons
         */
        hexagons(world);
        /*
        Render
         */
        ter.renderFrame(world);
    }
}
