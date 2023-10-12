package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {
    private int n;
    private int opened; // store the number of opened sites;
    private ArrayList<Integer> firstRowOpened; // store which of the first row are open;
    private ArrayList<Integer> filledSet; // store the set number that's filled; should be updated dynamically;
    WeightedQuickUnionUF djs;

    /**
     * Constructor is O(N); (N from construction of djs)
     */
    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        n = N;
        opened = 0;
        firstRowOpened = new ArrayList<>();
        filledSet = new ArrayList<>();
        djs = new WeightedQuickUnionUF(N * N);
    }

    /**
     * This implementation is O(logN); (logN from union, and find inside updateFilled())
     */
    public void open(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        } else if (isOpen(row, col)) {
            return;
        }

        opened ++;
        int curr = row * n + col + 1;
        if (curr <= n) {
            firstRowOpened.add(curr);
        }

        boolean f = false;
        for (int i = 0; i < 4; i ++) {
            int next = next(row, col, i);
            if (next == -1) {
                // out of bounds
                continue;
            }
            if (isOpen(next)) {
                f = true;
                djs.union(curr, next);
            }
        }

        if (f) {
            updateFilled();
        }
    }

    /**
     * This implementation is O(logN); (logN from find)
     */
    public boolean isOpen(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int curr = indexOf(row, col);
        return filledSet.contains(curr) || djs.find(curr) != curr ;
    }

    private boolean isOpen(int index) {
        return filledSet.contains(index) || djs.find(index) != index;
    }

    /**
     * This implementation is O(logN); (logN from find)
     */
    public boolean isFull(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        } else if (!isOpen(row, col)) {
            return false;
        }

        int curr = indexOf(row, col);
        return filledSet.contains(djs.find(curr));
    }

    public int numberOfOpenSites() {
        return opened;
    }

    /**
     * This implementation is O(N);
     */
    public boolean percolates() {
        for (int i = 0; i < n; i ++) {
            if (filledSet.contains(indexOf(n - 1, i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes O(1);
     */
    private int next(int row, int col, int direction) {
        int ret;
        switch(direction) {
            case 0: ret = indexOf(row - 1, col); break;
            case 1: ret = indexOf(row + 1, col); break;
            case 2: ret = indexOf(row, col - 1); break;
            default: ret = indexOf(row, col + 1);
        };
        return ret;
    }
    /**
     * Takes O(1);
     */
    private int indexOf(int row, int col) {
        if (!inBounds(row, col)) {
            return -1;
        }
        return row * n + col + 1;
    }

    /**
     * Takes O(1);
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    /**
     * Takes O(logN);
     */
    private void updateFilled() {
        filledSet = new ArrayList<> ();
        for (int i: firstRowOpened) {
            filledSet.add(djs.find(i));
        }
    }
}
