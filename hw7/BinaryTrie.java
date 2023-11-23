import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    Node root;
    Map<Character, BitSequence> lookUpTable;
    private class Node implements Comparable<Node> {
        boolean isEnd;
        Node parent = null, left = null, right = null;
        int frequency;
        char symbol;
        String edgeTo;
        Node(Node l, Node r) {
            left = l;
            right = r;

            l.parent = this;
            l.edgeTo = "l";
            r.parent = this;
            r.edgeTo = "r";
            isEnd = false;
            edgeTo = "";
            frequency = l.frequency + r.frequency;
        }
        Node(char c, int f) {
            symbol = c;
            frequency = f;

            isEnd = true;
            edgeTo = "";
        }
        Node search(int bit) {
            assert bit == 0 || bit == 1;
            return bit == 0 ? left : right;
        }
        String getSequence() {
            if (parent != null) {
                return parent.getSequence() + edgeTo;
            } else {
                return edgeTo;
            }
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
        BitSequence bs = new BitSequence();
        Node node = root;
        for (int i = 0; i < querySequence.length(); i ++) {
            int bit = querySequence.bitAt(i);
            bs = bs.appended(bit);
            node = node.search(bit);

            if (node.isEnd) break;
        }
        return new Match(bs, node.symbol);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        if (lookUpTable == null) {
            lookUpTable = new HashMap<>();
            buildTableHelper(root);
        }
        return lookUpTable;
    }

    /**
     * Traverse through the trie to the leaves;
     */
    private void buildTableHelper(Node n) {
        if (n == null) return;
        if (n.isEnd) {
            lookUpTable.put(n.symbol,
                    strToBS(n.getSequence()));
        } else {
          buildTableHelper(n.left);
          buildTableHelper(n.right);
        }
    }

    /**
     * Convert an 'edgeTo' sequence to BitSequence;
     * e.g. "lrlrr" -> 01011;
     */
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