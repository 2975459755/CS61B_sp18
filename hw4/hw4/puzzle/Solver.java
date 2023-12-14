package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashSet;

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
    private HashSet<WorldState> calculatedStates;

    public Solver(WorldState initial) {
        soln = new ArrayList<>();
        queue = new MinPQ<> ();
        queue.insert(new SearchNode(initial, null));

        calculatedStates = new HashSet<> ();
        solve();
    }

    private void solve() {
        SearchNode node = queue.delMin();
        // Don't use recursion, or this may run into StackOverFlow Error;
        while(calculatedStates.contains(node.ws) || !node.ws.isGoal()) {

            calculatedStates.add(node.ws);
            for (WorldState next: node.ws.neighbors()) {
                if (node.prev != null && next.equals(node.prev.ws)) {
                    continue;
                }
                queue.insert(new SearchNode(next, node));
            }
            node = queue.delMin();
        }

        solved(node);
    }

    private void solved(SearchNode node) {
        while (node.prev != null) {
            soln.add(0, node.ws);
            node = node.prev;
        }
        soln.add(0, node.ws);
    }

    public int moves() {
        return soln.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return soln;
    }
}
