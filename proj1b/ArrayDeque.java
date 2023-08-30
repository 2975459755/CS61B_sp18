public class ArrayDeque <T> implements Deque <T> {
    private T[] items;
    private int size;
    /*
    circular AD: <first> and <last> hold the start and end of AD;
     */
    private int first;
    private int last;
    /* the initiating length */
    private int startLength = 8;

    public ArrayDeque() {
        items = (T []) new Object[startLength];
        size = 0;
        first = 0;
        /*
        worth noted: last should not be set to equal first;
         */
        last = items.length - 1;
    }
//    public ArrayDeque(T val){
//        items = (T []) new Object[startLength];
//        items[0] = val;
//        size = 1;
//        first = 0;
//        last = 0;
//    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
//        for (int i = first; i < first + size & i < items.length; i++) {
//            System.out.print(items[i] + " ");
//        }
//        if (first > last) {
//            for (int i = 0; i < last + 1; i++) {
//                System.out.print(items[i] + " ");
//            }
//        }
        for (int i = 0; i < size; i++){
            System.out.print(items[indices(first, i)] + " ");
        }
    }
    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        } else {
            return items[indices(first, index)];
        }
    }
    @Override
    public void addFirst(T val) {
        checkLength();
        first = indices(first, -1);
        items[first] = val;
        size++;
    }
    @Override
    public void addLast(T val) {
        checkLength();
        last = indices(last, 1);
        items[last] = val;
        size++;
    }
    @Override
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
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = items[last];
        items[last] = null;
        last = indices(last, -1);
        size--;
        checkLength();
        return res;
    }
    /*
    helper functions;
    */
    private int indices(int start, int step) {
        /*
        get index: from <start> by <step>;
        */
        int i = start + step;
        while (i < 0) {
            i += items.length;
        }
        return i % items.length;
    }
    private void resize(int length) {
        /*
        create and reassign <items> to a new array sized <length>;
         */
        T [] newItems = (T []) new Object[length];
//        System.arraycopy(items, first, newItems, 0, Math.min(items.length - first, size));
//        if (first > last) {
//            System.arraycopy(items, 0, newItems, items.length - first, last + 1);
//        }
        for (int i = 0; i < size; i++){
            newItems[i] = items[indices(first, i)];
        }
        first = 0;
        last = size - 1;
        items = newItems;
    }
    private void checkLength() {
        /*
        length should be adequate as well as not too big;
        usage should be >= 0.25 when length >= 16;
         */
        if (size == items.length) {
            resize(items.length * 2);
        } else if (items.length >= 16 & (double) size / items.length < 0.25) {
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
