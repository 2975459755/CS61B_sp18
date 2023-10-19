package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Stack;

public class Solver {
    private class SearchNode implements Comparable<SearchNode>{
        WorldState ws;
        int alreadyMoved;
        SearchNode prev;
        SearchNode(WorldState ws, SearchNode prev) {
            this.ws = ws;
            this.prev = prev;
            if (prev == null) {
                alreadyMoved = 0;
            } else {
                alreadyMoved = prev.alreadyMoved + 1;
            }
        }
        protected int priority() {
            return ws.estimatedDistanceToGoal() + alreadyMoved;
        }
        @Override
        public int compareTo(SearchNode o) {
            return priority() - o.priority();
        }
    }

    private MinPQ<SearchNode> queue;
    private ArrayList<WorldState> soln;
    private int enqueued;

    public Solver(WorldState initial) {
        soln = new ArrayList<>();
        queue = new MinPQ<> ();
        queue.insert(new SearchNode(initial, null));
        enqueued = 1; // TODO
        solve();
    }

    private void solve() {
        SearchNode node = queue.delMin();
        if (node.ws.isGoal()) {
            solved(node);
            return;
        }
        for (WorldState next: node.ws.neighbors()) {
            if (node.prev != null && next.equals(node.prev.ws)) {
                continue;
            }
            queue.insert(new SearchNode(next, node));
            enqueued ++; // TODO
        }
        solve();
    }

    private void solved(SearchNode node) {
        while (node.prev != null) {
            soln.add(0, node.ws);
            node = node.prev;
        }
        soln.add(0, node.ws);
    }

    public int totalEnqueued() {
        return enqueued;
    }

    public int moves() {
        return soln.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return soln;
    }
}
