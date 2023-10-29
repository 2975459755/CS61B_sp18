import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> res = new Queue<>();
        for (Item i: items) {
            Queue<Item> subQueue = new Queue<> ();
            subQueue.enqueue(i);
            res.enqueue(subQueue);
        }
        return res;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> res = new Queue<> ();
        Item i1, i2;
        while (!(q1.isEmpty() || q2.isEmpty())) {
            i1 = q1.peek();
            i2 = q2.peek();
            if (i1.compareTo(i2) < 0) {
                res.enqueue(q1.dequeue());
            } else {
                res.enqueue(q2.dequeue());
            }
        }

        if (q1.isEmpty()) {
            while (!q2.isEmpty()) {
                res.enqueue(q2.dequeue());
            }
        } else if (q2.isEmpty()) {
            while (!q1.isEmpty()) {
                res.enqueue(q1.dequeue());
            }
        }
        return res;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() <= 1) {
            return items;
        }
        Queue<Queue<Item>> res = makeSingleItemQueues(items);
        Stack<Queue<Item>> stack = new Stack<> ();

        while (res.size() > 1) {
            while (res.size() > 1)      stack.push(mergeSortedQueues(res.dequeue(), res.dequeue()));
            if (!res.isEmpty())         stack.push(res.dequeue());
            while (!stack.isEmpty())    res.enqueue(stack.pop());
        }

        return res.dequeue();
    }
    public static void main(String[] args) {
        Queue<String> q1 = new Queue<> ();
        Queue<String> q2 = new Queue<> ();
        q1.enqueue("a");
        q1.enqueue("c");
        q1.enqueue("b");

        Queue res = mergeSort(q1);
        System.out.println(res);
        System.out.println(q1);
    }
}
