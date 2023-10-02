package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Input implements Serializable {
    static final int inComboInterval = 120;
    static final int comboLength = 2;
    static final int miniInterval = Game.miniInterval;
    static final ArrayList<Character> validInputs = new ArrayList<>(Arrays.asList(new Character[]
            {'w', 'a', 's', 'd', 'j', 'k', 'o', 'q'}));
    static final ArrayList <String> combos = new ArrayList<>(Arrays.asList( new String[]
            {"wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
                    "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd", // j with directions
            }));

    ArrayList<Character> waitList;
    Interval interval;
    Input () {
        waitList = new ArrayList<>();
        interval = new Interval(0);
    }

    /**
     * @return Correctly processed one input key or combo;
     * "" or "0" if no valid input;
     */
    String oneTurn() {
        interval.update(miniInterval); // first: update time;

        retrieveKeys();

        clearKeyStack();

        return parseWaitList();
    }

    /**
     * @return the first key in StdDraw; if none, return '0';
     */
    char oneKey() {
        if (!StdDraw.hasNextKeyTyped()) {
            // no input, return 0;
            return '0';
        } else {
            return StdDraw.nextKeyTyped();
        }
    }

    /**
     * Retrieve keys from StdDraw,
     * put to waitList,
     * until met the first invalid key;
     */
    void retrieveKeys() {
        char key;
        while (true) {
            key = oneKey();
            if (key == '0') {
                // no more inputs;
                break;
            } else if (!validInputs.contains(key)) {
                // invalid input
                interval.renew(0); // So that if there is a key waiting, can output immediately;
                break;
            }

            // valid input
            if (waitList.isEmpty()) {
                // no key is waiting: add this key and start waiting;
                interval.renew(inComboInterval);
            }
            waitList.add(key);
        }
    }

    /**
     * @return if some key is waiting -> "0";
     * the previous key waited too long -> that key;
     * otherwise -> the combo (or one key, if no valid combo);
     */
    String parseWaitList() {
        if (!waitList.isEmpty()) {
            if (interval.ended()) {
                // the previous key waited too long (no combo), return only that key;
                String ret = String.valueOf(waitList.get(0));
                // Let the next key start waiting;
                waitList.remove(0);
                interval.renew(inComboInterval);
                return ret;
            } else if (waitList.size() == 1) {
                // has only one key;
                return "0";
            } else {

                StringBuilder b = new StringBuilder();
                while (!waitList.isEmpty()) {
                    char c = waitList.get(0);
                    b.append(c);
                    while (waitList.contains(c)) {
                        waitList.remove((Object) c);
                    }
                }

                return validatedCombo(b.toString());
            }
        }
        // nothing this turn;
        return "";
    }

    /**
     * If the combination is not a combo,
     * return the first element (which should be always valid);
     */
    String validatedCombo(String s) {
        while (! (s.length() == 1 || combos.contains(s)) ) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    void clearKeyStack() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }

    //////////////////////////////////////////////////////

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
}