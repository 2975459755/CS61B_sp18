package byog.Core;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class tests {
    TETile[][] w = Game.buildWorld();
    @Test
    public void testNext () {
        int x = 1; int y = 1;
        int[] c0 = Game.next(x, y, 0);
        int[] c1 = Game.next(x, y, 1);
        int[] c2 = Game.next(x, y, 2);
        int[] c3 = Game.next(x, y, 3);

        assertEquals(2, c0.length);
//        assertEquals(c0[0], c1[0]);
//        assertEquals(c0[1], c3[1]);
//        assertEquals(c2[0], c3[0]);
//        assertEquals(c1[1], c2[1]);
    }
    @Test
    public void testHasNext () {
        assertTrue(Game.hasNext(w, 0, 0, 4));

        w[0][1] = Tileset.WALL;
        assertTrue(Game.hasNext(w, 0, 0, 4));

        w[1][0] = Tileset.WALL;
        assertFalse(Game.hasNext(w, 0, 0, 4));
        assertTrue(Game.hasNext(w, 1, 1, 4));

        w[2][1] = Tileset.WALL;
        w[1][2] = Tileset.WALL;
        assertFalse(Game.hasNext(w, 1, 1, 4));
    }
    @Test
    public void testSearchNext () {
        w[0][1] = Tileset.WALL;
        w[1][0] = Tileset.WALL;
        w[2][1] = Tileset.WALL;
        w[1][2] = Tileset.NOTHING;
        assertEquals(1, Game.searchNext(w, 1, 1, new Random(), 4)[0]);
        assertEquals(2, Game.searchNext(w, 1, 1, new Random(), 4)[1]);
    }
}
