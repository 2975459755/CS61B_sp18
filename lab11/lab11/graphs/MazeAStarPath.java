package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    private Queue<Integer> queue;

    private class distEstimate<T> implements Comparator<T> {
        @Override
        public int compare(Object o1, Object o2) {
            int v1 = (int) o1;
            int v2 = (int) o2;
            return h(v1) - h(v2);
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

        queue = new PriorityQueue<> (new distEstimate<>());
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return manhattan(v, t);
    }

    private int manhattan(int from, int to) {
        return Math.abs(maze.toX(from) - maze.toX(to)) + Math.abs(maze.toY(from) - maze.toX(to));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int v) {
        // TODO
        marked[v] = true;
        announce();

        if (v == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        for (int w: maze.adj(v)) {
            if (!marked[w]) {
                queue.add(w);
                edgeTo[w] = v;
                marked[w] = true;
                announce();
                distTo[w] = distTo[v] + 1;
            }
        }
        while (!queue.isEmpty()) {
            astar(queue.remove());
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

