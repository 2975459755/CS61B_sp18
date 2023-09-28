package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Input {
    static int keyInterval = Game.keyInterval;
    static int miniInterval = Game.miniInterval;
    static Character[] validInputs = Game.validInputs;
    static String[] combos = Game.combos;

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
     */
    static char solicitValidChar() {
        char input;
        while (true) {
            input = solicitInputChar();
            if (contains(validInputs, input)) {
                return input;
            }
        }
    }
    static char solicitComboInput(Character[] s) {
        char ret = '/'; // no valid combo successor: return '/';
        for (int t = 0; t < keyInterval / miniInterval; t ++) {
            StdDraw.pause(miniInterval);
            if (StdDraw.hasNextKeyTyped()) {

                char input = solicitInputChar();
                /*
                Combo input should not be the same as previous inputs;
                 */
                if (!contains(s, input) && contains(validInputs, input)) {
                    ret = input;
                    break;
                }
            }
        }
        return ret;
    }
    static String solicitInputString(int n) {
        Character[] s = new Character[n]; // store input chars
        int count = 1; // count actual typed length during `keyInterval` (might be less than `n`);

        s[0] = solicitValidChar(); // wait and get the first input

        char next;
        while (count < n) {
            next = solicitComboInput(s);

            if (next != '/') {
                s[count] = next;
                count ++;
            } else {
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i ++) {
            builder.append(s[i]);
        }

        String res = builder.toString();
        if (res.length() > 1 && !contains(combos, res)) {
            res = String.valueOf(res.charAt(0));
        }
        return res;
    }

    /**
     * A generic method
     * to determine whether 'c' is in an array 'arr';
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
