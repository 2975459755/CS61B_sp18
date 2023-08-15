public class ArrayDeque<T>
{
    private T[] items;
    private int size;
    private int First;
    private int Last;

    private int start_length = 8;

    private int indices(int start, int step)
    {
        int i = start + step;
        while (i < 0) i += items.length;
        return i % items.length;
    }
    public ArrayDeque()
    {
        items = (T []) new Object[start_length];
        size = 0;
        First = 0;
        Last = items.length - 1;
    }
    public ArrayDeque(T val)
    {
        items = (T []) new Object[start_length];
        items[0] = val;
        size = 1;
        First = 0;
        Last = 0;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }
    public int size()
    {
        return size;
    }
    public void printDeque()
    {
        for (int i = First; i < First + size & i < items.length; i ++)
        {
            System.out.print(items[i] + " ");
        }
        if (First > Last)
        {
            for (int i = 0; i < Last + 1; i ++)
            {
                System.out.print(items[i] + " ");
            }
        }
    }
    public T get(int index)
    {
        if (index >= size)
        {
            System.out.println("error");
            return null;
        }
        else
        {
            return items[indices(First, index)];
        }
    }
    public void addFirst(T val)
    {
        checkLength();
        First = indices(First, -1);
        items[First] = val;
        size ++;
    }
    public void addLast(T val)
    {
        checkLength();
        Last = indices(Last, 1);
        items[Last] = val;
        size ++;
    }
    public T removeFirst()
    {
        T res = items[First];
        items[First] = null;
        First = indices(First, 1);
        size --;
        checkLength();
        return res;
    }
    public T removeLast()
    {
        T res = items[Last];
        items[Last] = null;
        Last = indices(Last, -1);
        size --;
        checkLength();
        return res;
    }
    /*
    helper functions;
    */
    private void resize(int length)
    {
        /*
        create and sign items to a new array sized <length>;
         */
        T[] new_items = (T []) new Object[length];
        System.arraycopy(items, First, new_items, 0, Math.min(items.length - First, size));
        if (First > Last)
        {
            System.arraycopy(items, 0, new_items, items.length - First, Last + 1);
        }
        First = 0;
        Last = size - 1;
        items = new_items;
    }
    private void checkLength()
    {
        /*
        length should be adequate as well as not too big;
        usage should be >= 0.25 when length >= 16;
         */
        if (size == items.length)
        {
            resize(items.length * 2);
        }
        else if (items.length >= 16 & (double) size / items.length < 0.25)
        {
            resize(items.length / 2);
        }
    }
//    public static void main(String[] args)
//    {
//        ArrayDeque arr = new ArrayDeque();
//        for (int i = 0; i < 20; i ++)
//        {
//            arr.addFirst(Integer.toString(i));
//        }
//        for (int i = 0; i < 19; i ++)
//        {
//            arr.removeLast();
//        }
//        arr.printDeque();
//    }
}
