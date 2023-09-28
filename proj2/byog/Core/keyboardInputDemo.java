package byog.Core;

import byog.lab6.MemoryGame;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class keyboardInputDemo {
    private int width;
    private int height;
    public static final int keyInterval = 100; // interval between two key presses;
    static int miniInterval = 10;
    static Character[] validInputs = {'w', 'a', 's', 'd', 'k', 'o'}; // TODO
    static String[] combos = {"wk", "ak", "sk", "dk", "kw", "ka", "ks", "kd", // k with directions
            "wj", "aj", "sj", "dj", "jw", "ja", "js", "jd", // j with directions
    };
    public keyboardInputDemo(int width, int height) {
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
        while (true) {
            clearKeyQueue();

            StdDraw.pause(0);

            input = solicitInputString(2);
            drawFrame(input);

            StdDraw.pause(0);
        }
    }
    private static char solicitInputChar() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                return key;
            }
        }
    }
    static char comboInput(Character[] s) {
        for (int t = 0; t < keyInterval / miniInterval; t ++) {
            StdDraw.pause(miniInterval);
            if (StdDraw.hasNextKeyTyped()) {

                char input = solicitInputChar();
                if (!contains(s, input) && contains(validInputs, input)) {
                    return input;
                }
            }
        }
        return '/'; // no valid combo successor
    }
    private static String solicitInputString(int n) {
        Character[] s = new Character[n];
        int count = 0;
        char input;

        input = solicitInputChar();
        if (!contains(s, input) && contains(validInputs, input)) {
            s[count] = input;
            count ++;

            while (count < n) {
                char next = comboInput(s);

                if (next != '/') {
                    s[count] = next;
                    count ++;
                } else {
                    break;
                }
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
    private static <T> boolean contains(T[] arr, T c) {
        for (T t : arr) {
            if (c.equals(t)) {
                return true;
            }
        }
        return false;
    }
    private void clearKeyQueue() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }
    public static void main(String[] args) {
        keyboardInputDemo game = new keyboardInputDemo(40, 40);
        game.startGame();

    }
}
