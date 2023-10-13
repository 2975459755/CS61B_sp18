package hw2;

import edu.princeton.cs.algs4.StdStats;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class PercolationStats {
    private int size;
    private int times;
    private Random rand;
    private PercolationFactory perc;
    private ArrayList<Integer> statistics;
    private double[] statsArray;

    public PercolationStats(int N, int T, PercolationFactory pf) throws IllegalArgumentException {
        if (T <= 0) {
            throw new IllegalArgumentException("T should be greater than 0");
        }

        size = N;
        times = T;
        rand = new Random();
        perc = pf;
        statistics = new ArrayList<> ();
        statsArray = new double[T];

        simulate(N, T);
    }

    private void simulate(int N, int T) {
        Percolation p = perc.make(N);
        int c = 0;
        while (!p.percolates()) {
            int row = rand.nextInt(N);
            int col = rand.nextInt(N);
            if (p.isOpen(row, col)) {
                continue;
            }
            p.open(row, col);
            c ++;
        }

        statistics.add(c);

        if (T > 1) {
            simulate(N, T - 1);
        } else {
            // convert arrayList to array;
            for (int i = 0; i < times; i ++) {
                statsArray[i] = (double) statistics.get(i) / (size * size);
            }
        }
    }

    public double mean() {
        return StdStats.mean(statsArray);
    }

    public double stddev() {
        return StdStats.stddev(statsArray);
    }

    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(times));
    }

    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(times));
    }
}
