import edu.princeton.cs.algs4.MinPQ;

import java.sql.Array;
import java.util.*;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    private static int n, m;
    private static boolean solved = false;
    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        // Throw exception if k is non-positive;
        if (k < 0) throw new IllegalArgumentException("k should be positive");
        // Read the dictionary;
        Trie allWords = new Trie(dictPath);
        // Read the text;
        List<ArrayList<CH>> table = readTable(boardFilePath);
        n = table.get(0).size(); // # columns
        m = table.size(); // # rows
        // Find solution;
        MinPQ<String> sol = findSol(allWords, table);
        HashSet<String> ret = new HashSet<>();
        while (ret.size() < k) {
            ret.add(sol.delMin());
        }
        return new ArrayList<>(ret);
    }
    private static MinPQ<String> findSol(Trie t, List<ArrayList<CH>> table) {
        MinPQ<String> sol = new MinPQ<>(new StrCmp());
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                // For each point, find the solution;
                searchBoggle(sol, t.root, table, i, j, new ArrayList<>());
            }
        }
        return sol;
    }
    private static void searchBoggle(MinPQ<String> sol, Trie.Node prevNode, List<ArrayList<CH>> table,
                                     int i, int j, ArrayList<CH> searched) {
        if (invalid(i, j)) return; // invalid index;
        CH thisCH = table.get(i).get(j);
        if (searched.contains(thisCH)) return; // traversed tile;
        ArrayList<CH> copyOfSearched = new ArrayList<>(searched); // make a copy;
        copyOfSearched.add(thisCH);

        char thisChar = thisCH.val;
        Trie.Node thisNode = prevNode.getChild(thisChar);
        if (thisNode == null) return; // no such word;
        if (thisNode.isEnd) { // finds a word;
            if (copyOfSearched.size() >= 3) { // word should be sized >= 3;
                sol.insert(toStr(copyOfSearched));
            }
        }
        searchBoggle(sol, thisNode, table, i - 1, j - 1, copyOfSearched);
        searchBoggle(sol, thisNode, table, i, j - 1, copyOfSearched);
        searchBoggle(sol, thisNode, table, i - 1, j, copyOfSearched);
        searchBoggle(sol, thisNode, table, i + 1, j + 1, copyOfSearched);
        searchBoggle(sol, thisNode, table, i + 1, j, copyOfSearched);
        searchBoggle(sol, thisNode, table, i, j + 1, copyOfSearched);
        searchBoggle(sol, thisNode, table, i + 1, j - 1, copyOfSearched);
        searchBoggle(sol, thisNode, table, i - 1, j + 1, copyOfSearched);
    }
    private static String toStr(List<CH> searched) {
        StringBuilder builder = new StringBuilder();
        for (CH ch: searched) {
            builder.append(ch.val);
        }
        return builder.toString();
    }
    private static boolean invalid(int i, int j) {
        return i < 0 || j < 0 || i >= m || j >= n;
    }
    private static ArrayList<ArrayList<CH>> readTable(String boardFilePath) {
        In in = new In(boardFilePath);
        ArrayList<ArrayList<CH>> list = new ArrayList<>();
        int l = 0;
        while (!in.isEmpty()) {
            String str = in.readString();
            if (str.length() != l && l != 0) {
                throw new IllegalArgumentException("table should be rectangle");
            }
            l = str.length();

            ArrayList<CH> chars = new ArrayList<>();
            for (int i = 0; i < l; i ++) {
                chars.add(new CH(str.charAt(i)));
            }
            list.add(chars);
        }
        return list;
    }

    private static class CH {
        char val;
        CH(char value) {
            val = value;
        }
    }
    private static class StrCmp implements Comparator<String> {
        @Override
        /**
         * Length first, alphabetic last;
         */
        public int compare(String o1, String o2) {
            int l1 = o1.length(), l2 = o2.length();
            int diff = l1 - l2;
            if (diff != 0) {
                return - diff; // the longer, the smaller;
            } else {
                for (int i = 0; i < l1; i ++) {
                    diff = o1.charAt(i) - o2.charAt(i);
                    if (diff != 0) {
                        return diff;
                    }
                }
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        List<String> sol = solve(7, "exampleBoard.txt");
        for (String s: sol) {
            System.out.println(s);
        }
    }
}
