package lab11.graphs;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private ArrayDeque<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

        queue = new ArrayDeque<>();
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int V) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        marked[V] = true;
        announce();

        if (V == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        for (int w: maze.adj(V)) {
            if (!marked[w]) {
                queue.add(w);
                edgeTo[w] = V;
                marked[w] = true;
                announce();
                distTo[w] = distTo[V] + 1;
            }
        }
        while (!queue.isEmpty()) {
            bfs(queue.removeFirst());
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

