package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
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

        //TODO: Initialize random number generator

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n

        String ret = "";
        for (int i = 0; i < n; i ++) {
            ret += CHARACTERS[rand.nextInt(26)];
        }

        return ret;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen

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

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        int pause = 500; // 500 ms between each letter shows up
        int show = 1000; // 1000 ms for each letter shows

        for (int i = 0; i < letters.length(); i ++) {
            StdDraw.pause(pause);
            drawFrame(String.valueOf(letters.charAt(i)));
            StdDraw.pause(show);
            drawFrame("");
        }

    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input

        String str = "";

        drawFrame(str);

        while (str.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                str += StdDraw.nextKeyTyped();
                drawFrame(str);
            }
        }
        return str;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        gameOver = false;
        playerTurn = false;

        //TODO: Establish Game loop

        String ans;
        for (round = 1; !gameOver; round ++) {
            drawFrame("Round " + round);
            StdDraw.pause(1500);

            ans = generateRandomString(round);
            flashSequence(ans);

            clearKeyQueue(); // clear the key queue;
            StdDraw.pause(500);
            drawFrame("Type!");

            playerTurn = true;
            StdDraw.pause(1000);
            String userInput = solicitNCharsInput(round);
            StdDraw.pause(1000);
            playerTurn = false;

            if (Objects.equals(userInput, ans)) {
                drawFrame("Correct!");
                StdDraw.pause(2000); // after each turn, pause for 2 sec;
            } else {
                gameOver = true;
                drawFrame("Game Over! You reached level " + round);
            }
        }
    }

    /**
     * Clear the key queue;
     * Because StdDraw will store what you typed at all time;
     * If you don't do this, this game is easy to cheat (try M..G..Solution.java);
     */
    private void clearKeyQueue() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
    }
}
