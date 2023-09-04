import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequGold {
    @Test
    public void randomTest() {
        StudentArrayDeque <Integer> std = new StudentArrayDeque <> ();
        ArrayDequeSolution <Integer> sol = new ArrayDequeSolution <> ();
        assertEquals(sol.isEmpty(), std.isEmpty());
        assertEquals(sol.size(), std.size());
        /* No need to have error messages for those↑↑ */
        String log = "";
        for (int i = 0; i < 30; i++) {
            if (StdRandom.bernoulli(.6) || Math.min(std.size(), sol.size()) == 0) {
                int x = StdRandom.poisson(5);
                if (StdRandom.bernoulli()) {
                    std.addFirst(x);
                    sol.addFirst(x);
                    log += String.format("addFirst(%d)" + System.lineSeparator(), x);
                } else {
                    std.addLast(x);
                    sol.addLast(x);
                    log += String.format("addLast(%d)" + System.lineSeparator(), x);
                }
            } else {
                if (StdRandom.bernoulli()) {
                    log += "removeFirst()" + System.lineSeparator();
                    assertEquals(log, sol.removeFirst(), std.removeFirst());
                } else {
                    log += "removeLast()" + System.lineSeparator();
                    assertEquals(log, sol.removeLast(), std.removeLast());
                }
            }
        }
        for (int i = 0; i < sol.size(); i++) {
            if (StdRandom.bernoulli()) {
                log += "removeFirst()" + System.lineSeparator();
                assertEquals(log, sol.removeFirst(), std.removeFirst());
            } else {
                log += "removeLast()" + System.lineSeparator();
                assertEquals(log, sol.removeLast(), std.removeLast());
            }
        }
    }
}
