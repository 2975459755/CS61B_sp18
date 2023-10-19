package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private int length;
    private int[][] tiles;
    private static final int BLANK = 0;
    private int zero; // the position of zero;

    public Board(int[][] tiles) {
        length = 0;
        for (int[] row: tiles) {
            length ++;
        }
        this.tiles = new int[length][length];
        zero = -1;
        for (int i = 0; i < length; i ++) {
            for (int j = 0; j < length; j ++) {
                this.tiles[i][j] = tiles[i][j];
                if (zero == -1 && tiles[i][j] == BLANK) {
                    zero = numOfIndex(i, j);
                }
            }
        }
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        // where 0 is located:
        int i = iIndex(zero);
        int j = jIndex(zero);

        // make a copy:
        int[][] newState = new int[length][length];
        for (int x = 0; x < length; x ++) {
            for (int y = 0; y < length; y ++) {
                newState[x][y] = tiles[x][y];
            }
        }

        if (j > 0) {    // left;
            j --;
            neighbors.enqueue(new Board(swap(i, j, newState)));
            swapBack(i, j, newState);
            j ++;
        }
        if (j < length - 1) {   // right;
            j ++;
            neighbors.enqueue(new Board(swap(i, j, newState)));
            swapBack(i, j, newState);
            j --;
        }
        if (i > 0) { // up;
            i --;
            neighbors.enqueue(new Board(swap(i, j, newState)));
            swapBack(i, j, newState);
            i ++;
        }
        if (i < length - 1) { // down
            i ++;
            neighbors.enqueue(new Board(swap(i, j, newState)));
            swapBack(i, j, newState);
            i --;
        }
        return neighbors;
    }

    /**
     * Swap the tile (i, j) with the zero,
     * return that array;
     */
    private int[][] swap(int ii, int jj, int[][] newState) {
        newState[iIndex(zero)][jIndex(zero)] = newState[ii][jj];
        newState[ii][jj] = BLANK;
        return newState;
    }

    /**
     * Swap back;
     */
    private void swapBack(int ii, int jj, int[][] newState) {
        newState[ii][jj] = tileAt(ii, jj);
        newState[iIndex(zero)][jIndex(zero)] = BLANK;
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
