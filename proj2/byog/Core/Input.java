package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Input {
    static final int inComboInterval = 45;
    static final int comboLength = 2;
    static final int nanoInterval = 15;
    static final Character[] validInputs = {'w', 'a', 's', 'd', 'k', 'o'};
    static final String[] combos = {"wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
                            "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd", // j with directions
    };

    /**
     * Clear the key queue in StdDraw;
     */
    static void clearKeyQueue() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }

    /**
     * Never ends until the first input;
     */
    static char solicitInputChar() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                return key;
            }
        }
    }
    /**
     * Never ends until the first VALID input;
     * This method will remove all invalid inputs, until the first valid input;
     */
    @Deprecated
    static char solicitValidChar() {
        char input;
        while (true) {
            input = solicitInputChar();
            if (contains(validInputs, input)) {
                return input;
            }
        }
    }
    /**
     * This method is deprecated, use `tryValidCombo` instead;
     */
    @Deprecated
    static String solicitInputString(int n) {
        assert n > 0;

        Character[] s = new Character[n]; // store input chars
        int count = 1; // count actual typed length during `keyInterval` (might be less than `n`);

        s[0] = solicitValidChar(); // wait and get the first valid input

        char next;
        while (count < n) {
            next = tryComboSuccessor(s);

            if (next != '/') {
                s[count] = next;
                count ++;
            } else {
                break;
            }
        }

        /*
        collect input chars into String:
         */
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i ++) {
            builder.append(s[i]);
        }

        /*
        get a valid combo
         */
        String res = builder.toString();
        if (res.length() > 1 && !contains(combos, res)) {
            res = String.valueOf(res.charAt(0));
        }
        return res;
    }

    /**
     * Try to get a following combo key after the keys in 's', during combo interval;
     * If none, return '/';
     * This method will always cause the program to pause for inComboInterval;
     * @param s previous input keys;
     */
    static char tryComboSuccessor(Character[] s) {
        char ret = '/'; // no valid input during keyInterval: return '/';
        char input;
        for (int t = 0; t < inComboInterval / nanoInterval; t ++) {
            StdDraw.pause(nanoInterval);
            input = tryValidChar();
            if (input != '/') {
                if (!contains(s, input)) {
                    // a valid input must not repeat the previous inputs;
                    ret = input;
                }
                // pause for the rest of inComboInterval; and exit the loop;
                StdDraw.pause(nanoInterval *
                        (Math.floorDiv(inComboInterval, nanoInterval) - (t + 1)));
                break;
            }
        }
        return ret;
    }

    /**
     * Try to get a valid input from the key queue in StdDraw;
     * If none (no input, or has inputs but not valid), return '/';
     * This method will remove all INvalid inputs from the key queue until a valid one;
     */
    static char tryValidChar() {
        char input = '/';
        while (StdDraw.hasNextKeyTyped()) {
            input = StdDraw.nextKeyTyped();
            if (contains(validInputs, input)) {
                break;
            } else {
                input = '/';
            }
        }
        return input;
    }

    /**
     * Try to get a valid combo;
     * If none (no input, or has inputs but not valid), return empty "";
     * If player typed in valid inputs, but they are not a valid combo, return the first input;
     * If player input < length (during inComboInterval), still returns the inputs;
     * @param length maximum combo length, should be >= 1;
     * @param canAct whether asks for combo (when player can't act, asking for combo causes meaningless pause)
     */
    static String tryValidCombo(int length, boolean canAct) {
        assert length > 0;

        char input = tryValidChar();
        if (input == '/') {
            return "";
        }

        Character[] s = new Character[length];
        s[0] = input;
        int count = 1;

        /*
        Try attaining combo successors;
         */
        char next;
        while (canAct && count < length) {
            next = tryComboSuccessor(s);
            if (next != '/') {
                s[count] = next;
                count ++;
            } else {
                break;
            }
        }

        /*
        collect input chars, put into String:
         */
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i ++) {
            builder.append(s[i]);
        }

        /*
        get a valid combo
         */
        String res = builder.toString();
        if (res.length() > 1 && !contains(combos, res)) {
            res = String.valueOf(res.charAt(0));
        }
        return res;
    }

    /**
     * A generic method
     * to check whether an element 'c' is in an array 'arr';
     */
    static <T> boolean contains(T[] arr, T c) {
        for (T t : arr) {
            if (c.equals(t)) { // use .equals!
                return true;
            }
        }
        return false;
    }

    /**
     * Some code I CVed from GitHub;
     */
    static long solicitSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(320, 320, "Enter seed. Press S to end.");
        StdDraw.show();

        StringBuilder builder = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
//                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                char key = solicitNumberOrS(); // this line I wrote myself;

                if (key == 's') {
                    break;
                }

                builder.append(key);
                StdDraw.clear(Color.BLACK);
                StdDraw.text(320, 320, "Enter seed. Press S to end.");
                StdDraw.text(320, 300, "Your input: " + builder);
                StdDraw.show();
            }
        }

        if (builder.toString().equals("")) { // This line I added myself;
            return -1; // no input seed
        }
        return Long.parseLong(builder.toString());
    }

    /**
     * Valid input char for seed should be either a number or 's';
     */
    static char solicitNumberOrS() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                int key = (int) Character.toLowerCase(StdDraw.nextKeyTyped());
                if (key >= 48 && key <= 57 || key == 115) {
                    return (char) key;
                }
            }
        }
    }

    /**
     * The initial input should be 'n' or 'l' or 'q';
     */
    static char solicitInitialInput() {
        while(true) {
            char key = solicitInputChar();
            if (key == 'n' || key == 'l' || key == 'q') {
                return key;
            }
        }
    }
}
