package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;
public class hexTests {
    @Test
    public void testHex () {
        assertTrue(HexWorld.hex(2, 1, 0));
        assertTrue(HexWorld.hex(2, 2, 1));
        assertFalse(HexWorld.hex(2, 0, 0));

        assertTrue(HexWorld.hex(3, 2, 0));
        assertTrue(HexWorld.hex(3, 1, 1));
        assertFalse(HexWorld.hex(3, 1, 6));

        assertTrue(HexWorld.hex(4, 2, 1));
    }
}
