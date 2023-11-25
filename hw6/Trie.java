import java.util.*;

public class Trie {
    public class Node {
        Map<Character, Node> children;
        char value;
        boolean isEnd;
        Node(char val, boolean end) {
            value = val;
            isEnd = end;

            children = new HashMap<>();
        }
        Node() {
            isEnd = false;

            children = new HashMap<>();
        }
        Node getChild(char c) {
            return children.get(c);
        }
        void insert(char c, Node p) {
            children.put(c, p);
        }
    }

    public Node root;

    public Trie() {
        root = new Node();
    }
    public Trie(String dictPath) {
        root = new Node();
        In in = new In(dictPath);
        while (in.hasNextLine()) {
            put(in.readLine());
        }
    }
    public void put(String word) {
        if (word.isEmpty()) {
            root.isEnd = true;
            return;
        }
        char firstChar = word.charAt(0);
        Node target = root.getChild(firstChar);
        root.insert(firstChar,
                put(target, word, 0));
    }
    private Node put(Node node, String word, int index) {
        if (node == null) {
            node = new Node(word.charAt(index), false);
        }
        if (index == word.length() - 1) {
            node.isEnd = true;
        } else {
            char nextChar = word.charAt(index + 1);
            node.insert(nextChar,
                    put(node.getChild(nextChar), word, index + 1));
        }
        return node;
    }
}
