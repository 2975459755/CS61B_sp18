import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    } /* Uncomment this class once you've created your Palindrome class. */
    @Test
    public void testPalindrome() {
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("A"));
        assertFalse(palindrome.isPalindrome("Aa"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertFalse(palindrome.isPalindrome("AaA "));
    }
    @Test
    public void testNewIsPalindrome() {
        OffByOne obo = new OffByOne();
        assertTrue(palindrome.isPalindrome("", obo));
        assertTrue(palindrome.isPalindrome("A", obo));
        assertFalse(palindrome.isPalindrome("Aa", obo));
        assertTrue(palindrome.isPalindrome("racedbq", obo));
        assertFalse(palindrome.isPalindrome("AaA ", obo));
        assertFalse(palindrome.isPalindrome("aba", obo));
        assertTrue(palindrome.isPalindrome("&&%", obo));
    }
//    @Test
//    public void testOffByN() {
//        OffByN obn = new OffByN(5);
//        assertTrue(obn.equalChars('b', 'g'));
//        assertTrue(palindrome.isPalindrome("binding", obn));
//    }
}
