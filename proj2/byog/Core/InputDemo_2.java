package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;

public class InputDemo_2 {
    private int width;
    private int height;

    public InputDemo_2(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }
    public void drawFrame(String s) {

        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);

        StdDraw.show();
    }
    void startGame() {
        String input;
        In in = new In();
        while (true) {
            StdDraw.pause(In.miniInterval);
            input = in.oneTurn();
            if (input.equals("0")) {
                continue;
            }
            if (!input.isEmpty()) {
                drawFrame(input);
                System.out.println(input);
            }

        }
    }

    public static void main(String[] args) {
        InputDemo_2 game = new InputDemo_2(40, 40);
        game.startGame();
    }
}

class In {
    static final int inComboInterval = 120;
    static final int comboLength = 2;
    static final int miniInterval = 25;
    static final ArrayList<Character> validInputs = new ArrayList<>(Arrays.asList(new Character[]
            {'w', 'a', 's', 'd', 'j', 'k', 'o', 'q'}));
    static final ArrayList <String> combos = new ArrayList<>(Arrays.asList( new String[]
            {"wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
                    "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd", // j with directions
            }));

    ArrayList<Character> waitList;
    Interval interval;
    In () {
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
//                // there is a possible combo input;
//                while (waitList.size() > 1 && waitList.get(1) != waitList.get(0)) {
//                    // from the second item, remove if is same as the first;
//                    waitList.remove(1);
//                }
//                String ret = validatedCombo(firstKeysInWaitList(2));

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
    String firstKeysInWaitList(int length) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length && !waitList.isEmpty(); i ++) {
            ret.append(waitList.get(0));
            waitList.remove(0);
        }
        return ret.toString();
    }
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
}