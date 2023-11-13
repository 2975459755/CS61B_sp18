import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        // find the length of the longest (has most digits) item;
        int numDigits = 0;
        for (String s: asciis) {
            numDigits = Math.max(s.length(), numDigits);
        }

        // sort;
        String[] sorted = new String[asciis.length]; // non-destructive;
        System.arraycopy(asciis, 0, sorted, 0, asciis.length);
        for (int i = 1; i <= numDigits; i ++) { // starts from 1;
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort

        // Create the Nodes;
        HashMap<Character, Node> map = new HashMap<> ();
        for (String s: asciis) {
            char digit = digitAt(s, index);
            if (map.containsKey(digit)) {
                map.get(digit).strings.add(s);
            } else {
                Node n = new Node(digit, s);
                map.put(digit, n);
            }
        }

        // Insert Nodes into PQ;
        MinPQ<Node> pq = new MinPQ<> ();
        for (char digit: map.keySet()) {
            pq.insert(map.get(digit));
        }

        int i = 0;
        while (!pq.isEmpty()) {
            Node n = pq.delMin();
            for (String s: n.strings) {
                asciis[i] = s;
                i ++;
            }
        }
    }

    /**
     * Since PriorityQueue is not stable, I made this instead;
     */
    private static class Node implements Comparable<Node> {
        public char label;
        // Use an array to store all 'equivalent' strings;
        // This will guarantee to be stable;
        public ArrayList<String> strings;
        public Node(char l, String s) {
            label = l;
            strings = new ArrayList<>();
            strings.add(s);
        }
        // compareTo method used for MinPQ;
        @Override
        public int compareTo(Node o) {
            return label - o.label;
        }
    }

    private static char digitAt(String s, int index) {
        if (s.length() < index) {
            return 0;
        }
        return s.charAt(s.length() - index);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] arr = {"ccc", "aaa", "bbb", "ab","abc", "cba",  "bc"};
        String[] sorted = sort(arr);
        for (String s: sorted) {
            System.out.println(s);
        }
    }
}
