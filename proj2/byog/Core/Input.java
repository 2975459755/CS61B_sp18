package byog.Core;

import byog.Core.Objects.Player;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Input instance serves as a pub,
 * that distributes input keys to the right input collector instance,
 * and make each collector process its allocated inputs,
 * which then make players move;
 */
public class Input extends InputSolicitor {
    public static final int inComboInterval = 120;
    protected boolean twoPlayers;
    protected Collector in1; // player 1's input collector;
    protected Collector in2; // player 2's input collector;
    protected ArrayList<Collector> all; // all input collectors
    protected Game game;

    public Input(Game g, int numPlayers) {
        assert numPlayers == 1 || numPlayers == 2;
        twoPlayers = numPlayers > 1;
        game = g;

        in1 = new Collector(1, game.wg.p1);
        all = new ArrayList<> ();
        all.add(in1);

        if (twoPlayers) {
            in2 = new Collector(2, game.wg.p2);
            all.add(in2);
        }

        all.trimToSize();
    }

    public int oneTurn() {
        Collector[] a = all.toArray(new Collector[all.size()]);

        // update time;
        for (Collector in: a) {
            in.updateTime();
        }

        // distribute input keys;
        distributeKeys();

        // all collectors process inputs;
        int c = 0;
        if (game.wg.canPlayerAct()) {
            for (Collector in: a) {
                c += in.playerAct();
            }
        }

        // distribution ends at the first && invalid input,
        // so make sure to clear the rest;
        clearKeyStack();

        return c;
    }

    protected void distributeKeys() {
        char key = '>';
        boolean firstKey = true;
        while (true) {
            if (key != '>') {
                firstKey = false;
            }
            key = oneKey();
            if (key == '0') {
                // no more inputs;
                return;
            }

            boolean validKey = false;
            for (Collector in: all) {
                if (in.retrieveOneKey(key)) {
                    validKey = true;
                    break;
                }
            }
            // first key is invalid: output the existing key instantly (if there is one);
            if (!validKey) {
                if (firstKey) {
                    for (Collector in: all) {
                        in.clearInterval();
                    }
                }
                return;
            }
        }
    }

    /**
     * @return the first key in StdDraw; if none, return '0';
     */
    protected char oneKey() {
        if (!StdDraw.hasNextKeyTyped()) {
            // no input, return 0;
            return '0';
        } else {
            return StdDraw.nextKeyTyped();
        }
    }

    protected static void clearKeyStack() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }
}

class Collector implements Serializable {
    private static final int inComboInterval = Input.inComboInterval;
    private static final int miniInterval = Game.miniInterval;
    private static final ArrayList<Character> validInputs_1 = new ArrayList<>(Arrays.asList(
            'w', 'a', 's', 'd', 'j', 'k', 'o', 'q'));
    private static final ArrayList <String> combos_1 = new ArrayList<>(Arrays.asList(
            "wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
            "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd" // j with directions
    ));
    private static final ArrayList<Character> validInputs_2 = new ArrayList<>(Arrays.asList(
            't', 'f', 'g', 'h', ';', '\'', '='));
    private static final ArrayList <String> combos_2 = new ArrayList<>(Arrays.asList(
            "t;", ";t", "f;", ";f", "g;", ";g", "h;", ";h", // ; with directions
            "t'", "'t", "f'", "'f", "g'", "'g", "h'", "'h" // ' with directions
    ));

    private final ArrayList<Character> waitList;
    private final Interval interval;
    private final ArrayList<Character> validInputs;
    private final ArrayList<String> combos;
    private final Player player;

    protected Collector(int p, Player player) {
        waitList = new ArrayList<>();
        interval = new Interval(0);

        this.player = player;
        if (p == 1) {
            validInputs = validInputs_1;
            combos = combos_1;
        } else {
            validInputs = validInputs_2;
            combos = combos_2;
        }
        waitList.trimToSize();
        validInputs.trimToSize();
        combos.trimToSize();
    }

    /**
     *
     * @return 16: cheat mode; -8: quit and save; 1: player actioned; 0: nothing happened;
     */
    protected int playerAct() {
        String input = parseWaitList();
        if (!player.canAct() || input.equals("") || input.equals("0")) {
            return 0;
        }

        if (input.equals("o")) {
            // cheat mode on
            return 16;
        } else if (input.equals("q") || input.equals("=")) {
            // save game
            return -8;
        } else {
            player.act(input);
            return 1;
        }
    }

    protected void updateTime() {
        interval.update(Game.miniInterval);
    }
    protected void clearInterval() {
        interval.renew(0);
    }

    protected boolean retrieveOneKey(char key) {
        if (!validInputs.contains(key)) {
           return false;
        } else if (waitList.isEmpty()) {
            interval.renew(inComboInterval);
        }
        waitList.add(key);
        return true;
    }

    /**
     * @return if some key is waiting -> "0";
     * the previous key waited too long -> that key;
     * otherwise -> the combo (or one key, if no valid combo);
     */
    protected String parseWaitList() {
        if (waitList.isEmpty()) {
            // nothing this turn;
            return "";
        }

        if (interval.ended()) {
            // the previous key waited too long (no combo), return only that key;
            String ret = String.valueOf(waitList.get(0));
            waitList.remove(0);

            // Let the next key start waiting;
            interval.renew(inComboInterval);

            return ret;

        } else if (waitList.size() == 1) {
            // Some key is still waiting;

            return "0";

        } else {
            // There exists a possible combo;
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

    /**
     * Try to return a valid combo;
     * If the combination is not a combo,
     * return the first element (which should be always valid);
     */
    protected String validatedCombo(String s) {
        while (! (s.length() == 1 || combos.contains(s)) ) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    @Deprecated
    static void clearKeyStack() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }

    /**
     * @return the first key in StdDraw; if none, return '0';
     */
    @Deprecated
    char oneKey() {
        if (!StdDraw.hasNextKeyTyped()) {
            // no input, return 0;
            return '0';
        } else {
            return StdDraw.nextKeyTyped();
        }
    }
    /**
     * @return Correctly processed one input key or combo;
     * "" or "0" if no valid input;
     */
    @Deprecated
    String oneTurn() {
        interval.update(miniInterval); // first: update time;

        retrieveKeys();

        clearKeyStack();

        return parseWaitList();
    }

    /**
     * Retrieve keys from StdDraw,
     * put to waitList,
     * until met the first invalid key;
     */
    @Deprecated
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
}

/**
 * For special input soliciting (like during game initialization);
 */
class InputSolicitor implements Serializable {
    public static int solicitNumPlayers() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(320, 320, "Enter number of players (1 or 2). Press S to end.");
        StdDraw.show();

        char key;
        int num = 0;
        while (true) {
            StdDraw.pause(30);
            if (StdDraw.hasNextKeyTyped()) {
                key = solicitNumberOrS();

                if (key == 's') {
                    break;
                }
                if (key < '0' || key > '2') {
                    continue;
                }
                // player entered 1 or 2;
                StdDraw.clear(Color.BLACK);
                StdDraw.text(320, 320, "Enter number of players (1 or 2). Press S to end.");
                StdDraw.text(320, 300, "Player(s): " + key);
                StdDraw.show();

                if (key == '1') {
                    num = 1;
                } else {
                    num = 2;
                }
            }
        }

        if (num == 0) {
            num = 1;
        }
        return num;
    }
    public static long solicitSeed() {
        /*
         * Some code I CVed from GitHub;
         */
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
    public static char solicitNumberOrS() {
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
    public static char solicitInitialInput() {
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
    public static char solicitInputChar() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                return key;
            }
        }
    }
}