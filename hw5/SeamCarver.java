import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class SeamCarver {
    Picture pic;
    MinPQ<Point> xFirstPQ, yFirstPQ;

    public Picture picture() {
        return pic;
    }
    public int width() {
        return pic.width();
    }
    public int height() {
        return pic.height();
    }
    public double energy(int x, int y) {
        Color up = pic.get(x, (y - 1 + height()) % height());
        Color down = pic.get(x, (y + 1) % height());
        Color left = pic.get((x - 1 + width()) % width(), y);
        Color right = pic.get((x + 1) % width(), y);

        double xGradient, yGradient;
        xGradient = Math.pow(left.getRed() - right.getRed(), 2) +
                Math.pow(left.getBlue() - right.getBlue(), 2) +
                Math.pow(left.getGreen() - right.getGreen(), 2);
        yGradient = Math.pow(up.getRed() - down.getRed(), 2) +
                Math.pow(up.getBlue() - down.getBlue(), 2) +
                Math.pow(up.getGreen() - down.getGreen(), 2);

        return xGradient + yGradient;
    }

    public SeamCarver(Picture picture) {
        pic = picture;
        xFirstPQ = new MinPQ<>(new PointCmp(true? 1 : 0));
        yFirstPQ = new MinPQ<>(new PointCmp(false? 1 : 0));

        int h = height(), w = width();
        for (int i = 0; i < w; i ++) {
            for (int j = 0; j < h; j ++) {
                Point p = new Point(energy(i, j), i, j);
                xFirstPQ.insert(p);
                yFirstPQ.insert(p);
            }
        }
    }
    private class Point implements Comparable<Point> {
        double energy;
        int x, y;
        Point(double energy, int x, int y) {
            this.energy = energy;
            this.x = x;
            this.y = y;

            /*
            calc energy -> min energy in seam;
            access index -> int array in seam;
            after seam carving:
                rearrange indices
                re-calc energy(in four directions)
             */

        }
        int index(boolean xFirst) {
            if (xFirst) return y;
            else return x;
        }
        @Override
        public int compareTo(Point o) {
            double diff = energy - o.energy;
            if (diff > 0) { return 1; }
            else if (diff == 0) { return 0; }
            else { return -1; }
        }
    }
    private class PointCmp implements Comparator<Point> {
        int xFirst;
        PointCmp(int xFirst) { this.xFirst = xFirst; }
        @Override
        public int compare(Point o1, Point o2) {
            if (xFirst == 1) {
                if (o1.x != o2.x) return o1.x - o2.x;
                else return o1.y - o2.y;
            } else if (xFirst == 0) {
                if (o1.y != o2.y) return o1.y - o2.y;
                else return o1.x - o2.x;
            } else {
                double diff = o1.energy - o2.energy;
                if (diff > 0) return 1;
                else if (diff < 0) return -1;
                else return 0;
            }
        }
    }
    public int[] findHorizontalSeam() {
        int[] ret = new int[width()];
        ArrayList<Point> seam = new ArrayList<>();
        MinPQ<Point> firstCol = new MinPQ<>(new PointCmp(2));
        for (int i = 0; i < height(); i ++) {
            firstCol.insert(xFirstPQ.delMin());
        }
        Point first = firstCol.delMin();
        seam.add(first);

        findSeamHelper(seam, first, true, xFirstPQ);

        for (int i = 0; i < width(); i ++) {
            ret[i] = seam.get(i).y;
        }
        return ret;
    }
    public int[] findVerticalSeam() {
        int[] ret = new int[height()];
        ArrayList<Point> seam = new ArrayList<>();
        MinPQ<Point> firstRow = new MinPQ<>();
        for (int i = 0; i < width(); i ++) {
            firstRow.insert(yFirstPQ.delMin());
        }
        Point first = firstRow.delMin();
        seam.add(first);

        findSeamHelper(seam, first, false, yFirstPQ);

        for (int i = 0; i < height(); i ++) {
            ret[i] = seam.get(i).x;
        }
        return ret;
    }
    private void findSeamHelper(ArrayList<Point> seam, Point prev, boolean xFirst, MinPQ<Point> points) {
        int l, n;
        if (xFirst) {l = height(); n = width();}
        else {l = width(); n = height();}

        if (seam.size() == n) return;

        MinPQ<Point> pq = new MinPQ<>(new PointCmp(2));
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < l; i ++) {
            Point p = points.delMin();
            list.add(p);
            if (Math.abs(p.index(xFirst) - prev.index(xFirst)) <= 1) {
                pq.insert(p);
            }
        }
        prev = pq.delMin();
        seam.add(prev);
        list.remove(prev);
        findSeamHelper(seam, prev, xFirst, points);

        for (Point p: list) {
            points.insert(p);
        }
    }
    public void removeHorizontalSeam(int[] seam) {
        pic = SeamRemover.removeHorizontalSeam(pic, seam);
    }
    public void removeVerticalSeam(int[] seam) {
        pic = SeamRemover.removeVerticalSeam(pic, seam);
    }
}