public class LinkedListDeque<T> {
    private Node<T> sentinel;
    private int size;

    private class Node<T> {
        public T first;
        public Node<T> next;
        public Node<T> prev;
        public Node(Node<T> p, T f, Node<T> n) {
            this.prev = p;
            this.first = f;
            this.next = n;
        }
    }
    public LinkedListDeque() {
        sentinel = new Node<T>(null, null, null);
        size = 0;
    }
//    public LinkedListDeque(T val){
//        Node<T> item = new Node<>(sentinel, val, sentinel);
//        sentinel = new Node<T>(item, null, item);
//        size = 1;
//    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public void addFirst(T val) {
        if (sentinel.next == null) {
            sentinel.next = new Node<>(sentinel, val, sentinel);
            sentinel.prev = sentinel.next;
        }
        else {
            sentinel.next = new Node<>(sentinel, val, sentinel.next);
        }
        size++;
    }
    public void addLast(T val) {
        if (sentinel.next == null) {
            this.addFirst(val);
        }
        else {
            sentinel.prev = new Node<>(sentinel.prev, val, sentinel);
            sentinel.prev.prev.next = sentinel.prev;
        }
        size++;
    }
    public void printDeque() {
        Node<T> curr = sentinel.next;
        while (curr != sentinel) {
            System.out.print(curr.first + " ");
            curr = curr.next;
        }
    }
    public T get(int index) {
        Node<T> curr = sentinel;
        do {
            curr = curr.next;
            if (curr == sentinel) {
//                System.out.println("error");
                return null;
            }
            curr = curr.next;
            index--;
        } while (index >= 0);
        return curr.first;
    }
    public T getRecursive(int index) {
        return getR(sentinel.next, index);
    }
    private T getR(Node<T> node, int index) {
        if (node == sentinel) {
//            System.out.println("error");
            return null;
        }
        else if (index == 0) {
            return node.first;
        }
        else {
            return getR(node.next, index - 1);
        }
    }
    public T removeFirst() {
        if (size == 0) {
//            System.out.println("error");
            return null;
        }
        T ret = sentinel.next.first;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return ret;
    }
    public T removeLast() {
        if (size == 0) {
//            System.out.println("error");
            return null;
        }
        T ret = sentinel.prev.first;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return ret;
    }
}
