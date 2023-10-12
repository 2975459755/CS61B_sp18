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
    private WeightedQuickUnionUF djs;
    private WeightedQuickUnionUF djs_2;

    /**
     * Constructor is O(N^2) from the constructor of disjointSet;
     * The rest of the constructor takes O(NlogN); logN from union();
     */
    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        n = N;
        opened = new HashSet<>();
        /* The last one is for "source of water"; */
        djs = new WeightedQuickUnionUF(n * n + 1);
        /*
        A copy of `djs` with an extra component;
        The last one is for "bottom of the shape";
        Because you can't just connect with this last one with the "source",
        this will cause every site at the bottom to be filled,
        even if they are not;
        This will double the runtime of constructor and open(...),
        so I hope I would find another implementation that's both fast and not ugly;
        */
        djs_2 = new WeightedQuickUnionUF(n * n + 2);
        /* connect the top layer with the "source of water"; */
        for (int i = 0; i < n; i ++) {
            djs.union(i, n * n);
            djs_2.union(i, n * n);
        }
        /* connect the bottom layer with the "bottom of the shape"; */
        for (int i = 0; i < n; i ++) {
            djs_2.union(indexOf(n - 1, i), n * n + 1);
        }
    }

    /**
     * This implementation is O(logN); (logN from union())
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
                djs_2.union(curr, next);
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
     * if contains() of HashSet is not worse than O(logN); (logN from connected())
     */
    public boolean isFull(int row, int col) throws IndexOutOfBoundsException {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        } else if (!isOpen(row, col)) {
            return false;
        }

        int curr = indexOf(row, col);
        return djs.connected(curr, n * n);
    }

    public int numberOfOpenSites() {
        return opened.size();
    }

    /**
     * This implementation is O(logN); (logN from connected())
     */
    public boolean percolates() {
        return djs_2.connected(n * n, n * n + 1);
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

    public static void main(String[] args) {

    }
}
