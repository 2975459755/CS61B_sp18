public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int first;
    private int Last;

    private int startLength = 8;

    public ArrayDeque() {
        items = (T []) new Object[startLength];
        size = 0;
        first = 0;
        Last = items.length - 1;
    }
//    public ArrayDeque(T val){
//        items = (T []) new Object[startLength];
//        items[0] = val;
//        size = 1;
//        first = 0;
//        Last = 0;
//    }

    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void printDeque() {
        for (int i = first; i < first + size & i < items.length; i++) {
            System.out.print(items[i] + " ");
        }
        if (first > Last) {
            for (int i = 0; i < Last + 1; i++) {
                System.out.print(items[i] + " ");
            }
        }
    }
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        else {
            return items[indices(first, index)];
        }
    }
    public void addFirst(T val) {
        checkLength();
        first = indices(first, -1);
        items[first] = val;
        size++;
    }
    public void addLast(T val) {
        checkLength();
        Last = indices(Last, 1);
        items[Last] = val;
        size++;
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T res = items[first];
        items[first] = null;
        first = indices(first, 1);
        size--;
        checkLength();
        return res;
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = items[Last];
        items[Last] = null;
        Last = indices(Last, -1);
        size--;
        checkLength();
        return res;
    }
    /*
    helper functions;
    */
    private int indices(int start, int step) {
        int i = start + step;
        while (i < 0) i += items.length;
        return i % items.length;
    }
    private void resize(int length) {
        /*
        create and sign items to a new array sized <length>;
         */
        T[] newItems = (T []) new Object[length];
        System.arraycopy(items, first, newItems, 0, Math.min(items.length - first, size));
        if (first > Last) {
            System.arraycopy(items, 0, newItems, items.length - first, Last + 1);
        }
        first = 0;
        Last = size - 1;
        items = newItems;
    }
    private void checkLength() {
        /*
        length should be adequate as well as not too big;
        usage should be >= 0.25 when length >= 16;
         */
        if (size == items.length) {
            resize(items.length * 2);
        }
        else if (items.length >= 16 & (double) size / items.length < 0.25) {
            resize(items.length / 2);
        }
    }
//    public static void main(String[] args){
//        ArrayDeque arr = new ArrayDeque();
//        for (int i = 0; i < 20; i++){
//            arr.addFirst(Integer.toString(i));
//        }
//        for (int i = 0; i < 19; i++){
//            arr.removeLast();
//        }
//        arr.printDeque();
//    }
}
