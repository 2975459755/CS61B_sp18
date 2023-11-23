import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node root;
    private Map<Character, BitSequence> lookUpTable;
    private final String LEFT = "0", RIGHT = "1";

    private class Node implements Comparable<Node>, Serializable {
        Node parent = null, left = null, right = null;
        int frequency;
        char symbol; // only leaf nodes have symbol;
        String edgeTo; // "l" or "r";
        Node(Node l, Node r) {
            left = l;
            right = r;

            l.parent = this;
            l.edgeTo = LEFT;
            r.parent = this;
            r.edgeTo = RIGHT;
            edgeTo = "";
            frequency = l.frequency + r.frequency;
        }
        Node(char c, int f) {
            symbol = c;
            frequency = f;

            edgeTo = "";
        }
        boolean isLeaf() {
            return left == null && right == null;
        }
        boolean isRoot() {
            return parent == null;
        }
        Node search(int bit) {
            assert bit == 0 || bit == 1;
            return bit == 0 ? left : right;
        }

        /**
         * The path consisting of left/right directions
         *  to traverse from root to this;
         */
        String getPath() {
            if (!isRoot())
                return parent.getPath() + edgeTo;
            else
                return edgeTo;
        }
        @Override
        public int compareTo(Node o) {
            return frequency - o.frequency;
        }
    }
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (Character c: frequencyTable.keySet()) {
            pq.insert(new Node(c, frequencyTable.get(c)));
        }

        Node l, r;
        while (pq.size() > 1) {
            l = pq.delMin();
            r = pq.delMin();
            pq.insert(new Node(l, r));
        }
        root = pq.delMin();

        lookUpTable = null;
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node node = root;
        for (int i = 0; i < querySequence.length(); i ++) {
            int bit = querySequence.bitAt(i);
            node = node.search(bit);

            if (node.isLeaf()) break;
        }
        return new Match(new BitSequence(node.getPath()),
                        node.symbol);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        if (lookUpTable == null) {
            lookUpTable = new HashMap<>();
            buildTableHelper(root);
        }
        return lookUpTable;
    }

    /**
     * Traverse through the trie til the leaves;
     */
    private void buildTableHelper(Node n) {
        if (n == null) return;
        if (n.isLeaf()) {
            lookUpTable.put(n.symbol,
                    new BitSequence(n.getPath()));
        } else {
          buildTableHelper(n.left);
          buildTableHelper(n.right);
        }
    }

    /**
     * Convert an 'edgeTo' sequence to BitSequence;
     * e.g. "lrlrr" -> 01011;
     */
    @Deprecated
    private static BitSequence strToBS(String seq) {
        BitSequence bs = new BitSequence();
        for (int i = 0; i < seq.length(); i ++) {
            if (seq.charAt(i) == 'r')
                bs = bs.appended(1);
            else bs = bs.appended(0);
        }
        return bs;
    }
}