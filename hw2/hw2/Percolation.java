package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.HashSet;

public class Percolation {
    private int n;
    private HashSet<Integer> opened;
    @Deprecated
    private HashSet<Integer> firstRowOpened; // store which of the first row are open;
    @Deprecated
    private HashSet<Integer> filledSet; // store the set number that's filled; should be updated dynamically;
    WeightedQuickUnionUF djs;

    /**
     * Constructor is O(N^2);
     */
    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        n = N;
        opened = new HashSet<>();
        djs = new WeightedQuickUnionUF(n * n + 1); // the last one is for "source of water";
        /* connect the top layer with the "source of water"; */
        for (int i = 0; i < n; i ++) {
            djs.union(i, n * n);
        }
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

        int curr = indexOf(row, col);
        opened.add(curr);

        for (int i = 0; i < 4; i ++) {
            int next = next(row, col, i);
            if (next == -1) {
                // out of bounds
                continue;
            }
            if (isOpen(next)) {
                djs.union(curr, next);
            }
        }
    }

    /**
     * This implementation is O(1), if contains() of HashSet is O(1);
     */
    public boolean isOpen(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int curr = indexOf(row, col);
        return opened.contains(curr);
    }

    private boolean isOpen(int index) {
        return opened.contains(index);
    }

    /**
     * This implementation is O(logN),
     * if contains() of HashSet is not worse than O(logN); (logN from find)
     */
    public boolean isFull(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        } else if (!isOpen(row, col)) {
            return false;
        }

        int curr = indexOf(row, col);
        return djs.find(curr) == djs.find(n * n);
    }

    public int numberOfOpenSites() {
        return opened.size();
    }

    /**
     * This implementation is O(N) * O_isFull;
     */
    public boolean percolates() {
        for (int i = 0; i < n; i ++) {
            if (isFull(n - 1, i)) {
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
        return row * n + col;
    }

    /**
     * Takes O(1);
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    /**
     * Takes O(NlogN);
     */
    @Deprecated
    private void updateFilled() {
        filledSet = new HashSet<> ();
        for (int i: firstRowOpened) {
            filledSet.add(djs.find(i));
        }
    }
}
