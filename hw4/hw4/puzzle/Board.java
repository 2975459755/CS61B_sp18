package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private int length;
    private int[][] tiles;
    private static final int BLANK = 0;

    public Board(int[][] tiles) {
        length = 0;
        for (int[] row: tiles) {
            length ++;
        }
        this.tiles = new int[length][length];
        for (int i = 0; i < length; i ++) {
            for (int j = 0; j < length; j ++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Source: <a href="http://joshh.ug/neighbors.html"> click here </a>;
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int tileAt(int i, int j) {
        if (!inBounds(i, j)) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size() {
        return length;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < length; i ++) {
            for (int j = 0; j < length; j ++) {
                if (numOfIndex(i, j) != tileAt(i, j) && tileAt(i, j) != BLANK) {
                    hamming ++;
                }
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < length; i ++) {
            for (int j = 0; j < length; j ++) {
                int num = tileAt(i, j);
                if (num == 0) {
                    continue;
                }
                manhattan += Math.abs(i - iIndex(num))
                            + Math.abs(j - jIndex(num));
            }
        }
        return manhattan;
    }

    private int iIndex(int num) {
        return (num - 1) / length;
    }

    private int jIndex(int num) {
        return (num - 1) % length;
    }

    private int numOfIndex(int i, int j ) {
        if (!inBounds(i, j)) {
            return -1;
        }
        return i * length + j + 1;
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < length && j >= 0 && j < length;
    }

    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        }
        Board b = (Board) y;
        if (b.size() != size()) {
            return false;
        }
        for (int i = 0; i < length; i ++) {
            for (int j = 0; j < length; j ++) {
                if (tileAt(i, j) != b.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
