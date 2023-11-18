import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.ArrayList;

public class SeamCarver {
    Picture pic;
    Point topLeft;
    ArrayList<Point> verticalSeam;
    ArrayList<Point> horizontalSeam;

    public SeamCarver(Picture picture) {
        pic = picture;
        Point[][] matrix = new Point[height()][width()];

        int h = height(), w = width();

        for (int i = 0; i < w; i ++) {
            for (int j = 0; j < h; j ++) {
                Point e = new Point(energy(i, j),
                        get(matrix, i - 1, j), get(matrix, i, j - 1));
                matrix[j][i] = e;
            }
        }
        topLeft = matrix[0][0];
    }
    private static Point get(Point[][] matrix, int x, int y) {
        try {
            return matrix[y][x];
        } catch (Exception exception) {
            return null;
        }
    }
    private class Point implements Comparable<Point> {
        Point left, right, up, down;
        double energy;
        boolean deleted;
        Point(double energy, Point left, Point up) {
            this.energy = energy;
            this.up = up;
            this.left = left;

            if (left != null) left.right = this;
            if (up != null) up.down = this;

            this.deleted = false;
        }
        Point findMinVertical() {
            MinPQ<Point> pq = new MinPQ<>();
            if (down != null) {
                pq.insert(down);
                if (left != null) pq.insert(left.down);
                if (right != null) pq.insert(right.down);
            } else {
                return null;
            }
            return pq.delMin();
        }

        Point findMinHorizontal() {
            MinPQ<Point> pq = new MinPQ<>();
            if (right != null) {
                pq.insert(right);
                if (up != null) pq.insert(right.up);
                if (down != null) pq.insert(right.down);
            } else {
                return null;
            }
            return pq.delMin();
        }

        int x() {
            if (left == null) return 0;
            else return left.x() + 1;
        }
        int y() {
            if (up == null) return 0;
            else return up.y() + 1;
        }
        void calcEnergy() {
            energy = energy(x(), y());
        }

        void deleteVerti() {
            if (this.deleted) return;
            else this.deleted = true;

            if (left != null)  {
                left.right = right;
                left.calcEnergy();
            }
            if (right != null) {
                right.left = left;
                right.calcEnergy();
            }
            if (up != null) {
                up.down = right;
                up.calcEnergy();
            }
            if (down != null) {
                down.up = right;
                down.calcEnergy();
            }
        }
        void deleteHori() {
            if (this.deleted) return;
            else this.deleted = true;

            if (up != null) {
                up.down = down;
                up.calcEnergy();
            }
            if (down != null) {
                down.up = up;
                down.calcEnergy();
            }
            if (left != null) {
                left.right = down;
                left.calcEnergy();
            }
            if (right != null) {
                right.left = down;
                right.calcEnergy();
            }
        }
        @Override
        public int compareTo(Point o) {
            double diff = energy - o.energy;
            if (diff > 0) {
                return 1;
            } else if (diff == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }
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
    public int[] findHorizontalSeam() {
        Point p = topLeft;
        MinPQ<Point> pq = new MinPQ<> ();
        while (p != null) {
            pq.insert(p);
            p = p.down;
        }

        p = pq.delMin();
        horizontalSeam = new ArrayList<>();
        while (p != null) {
            horizontalSeam.add(p);
            p = p.findMinHorizontal();
        }

        int[] res = new int[width()];
        for (int i = 0; i < width(); i ++) {
            res[i] = horizontalSeam.get(i).y();
        }

        return res;
    }
    public int[] findVerticalSeam() {
        Point p = topLeft;
        MinPQ<Point> pq = new MinPQ<> ();
        while (p != null) {
            pq.insert(p);
            p = p.right;
        }

        p = pq.delMin();
        verticalSeam = new ArrayList<>();
        while (p != null) {
            verticalSeam.add(p);
            p = p.findMinVertical();
        }

        int[] res = new int[height()];
        for (int i = 0; i < height(); i ++) {
            res[i] = verticalSeam.get(i).x();
        }

        return res;
    }
    public void removeHorizontalSeam(int[] seam) {
        pic = SeamRemover.removeHorizontalSeam(pic, seam);
        for (Point p: horizontalSeam) {
            p.deleteHori();
        }
    }
    public void removeVerticalSeam(int[] seam) {
        pic = SeamRemover.removeVerticalSeam(pic, seam);
        for (Point p: verticalSeam) {
            p.deleteVerti();
        }
    }
}