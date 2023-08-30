public class LinkedListDeque<T> implements Deque <T> {
    private Node<T> sentinel;
    private int size;
    private class Node<T> {
        private T first;
        /*
        double-ended;
         */
        private Node<T> next;
        private Node<T> prev;
        public Node(Node<T> p, T f, Node<T> n) {
            prev = p;
            first = f;
            next = n;
        }
    }
    public LinkedListDeque() {
        sentinel = new Node<T>(null, null, null);
        /*
        circular LLD: when size == 0,
        sentinel points to itself in either direction;
         */
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
//    public LinkedListDeque(T val){
//        Node<T> item = new Node<>(sentinel, val, sentinel);
//        sentinel = new Node<T>(item, null, item);
//        size = 1;
//    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public void addFirst(T val) {
        if (sentinel.next == sentinel) {
            sentinel.next = new Node<>(sentinel, val, sentinel);
            sentinel.prev = sentinel.next;
        } else {
            sentinel.next = new Node<>(sentinel, val, sentinel.next);
            sentinel.next.next.prev = sentinel.next;
        }
        size++;
    }
    @Override
    public void addLast(T val) {
        if (sentinel.next == sentinel) {
            this.addFirst(val);
        } else {
            sentinel.prev = new Node<>(sentinel.prev, val, sentinel);
            sentinel.prev.prev.next = sentinel.prev;
            size++;
        }
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.next.first;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return ret;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.prev.first;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return ret;
    }
    @Override
    public void printDeque() {
        Node<T> curr = sentinel.next;
        while (curr != sentinel) {
            System.out.print(curr.first + " ");
            curr = curr.next;
        }
    }
    @Override
    public T get(int index) {
        Node<T> curr = sentinel;
        do {
            curr = curr.next;
            if (curr == sentinel) {
                return null;
            }
            index--;
        } while (index >= 0);
        return curr.first;
    }
    public T getRecursive(int index) {
        return getR(sentinel.next, index);
    }
    private T getR(Node<T> node, int index) {
        /*
        helper function for getRecursive;
         */
        if (node == sentinel) {
            return null;
        } else if (index == 0) {
            return node.first;
        } else {
            return getR(node.next, index - 1);
        }
    }
//    public static void main(String[] args){
//        LinkedListDeque arr = new LinkedListDeque();
//        LinkedListDeque arr2 = new LinkedListDeque();
//        arr2.addFirst(0);
//        arr2.addFirst(2);
//        arr2.addFirst(3);
//        arr2.addFirst(4);
//        System.out.println(arr2.removeLast());
////        System.out.println(arr2.get(15));
//        arr2.addFirst(6);
//        arr2.addFirst(7);
//        System.out.println(arr2.removeLast());
//        arr2.printDeque();
//    }
}
