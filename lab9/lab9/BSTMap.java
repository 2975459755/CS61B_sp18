package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (key.equals(p.key)) {
            return p.value;
        } else if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size ++;
            return new Node(key, value);
        } else if (key.equals(p.key)) {
            p.value = value;
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> s = new HashSet<>();
        keySet(s, root);
        return s;
    }

    private void keySet(Set<K> s, Node p) {
        if (p == null) {
            return ;
        }
        s.add(p.key);
        keySet(s, p.left);
        keySet(s, p.right);
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V val = get(key);
        root = remove(key, root);
        return val;
    }

    private Node remove(K key, Node p) {
        if (p == null) {
            return null;
        } else if (key.equals(p.key)) {
            Node right = p.right;
            p = p.left;
            size --;
            root = addNode(right, root);
        } else if (key.compareTo(p.key) > 0) {
            p.right = remove(key, p.right);
        } else {
            p.left = remove(key, p.left);
        }
        return p;
    }

    private Node addNode(Node p, Node to) {
        if (to == null) {
            return p;
        } else if (p.key.compareTo(to.key) > 0) {
            to.right = addNode(p, to.right);
        } else {
            to.left = addNode(p, to.left);
        }
        return to;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V val = get(key);
        if (val != value) {
            return null;
        }
        root = remove(key, root);
        return val;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
