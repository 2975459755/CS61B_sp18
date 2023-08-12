public class LinkedListDeque<type>
{
    public Node<type> sentinel;
    public int size;

    private class Node<type>
    {
        public type first;
        public Node<type> next;
        public Node<type> prev;
        public Node(Node<type> prev, type first, Node<type> next)
        {
            this.prev = prev;
            this.first = first;
            this.next = next;
        }
    }
    public LinkedListDeque()
    {
        sentinel = new Node<type>(null, null, null);
        size = 0;
    }
    public LinkedListDeque(type val)
    {
        Node<type> item = new Node<>(sentinel, val, sentinel);
        sentinel = new Node<type>(item, null, item);
        size = 1;
    }
    public int size()
    {
        return size;
    }
    public boolean isEmpty()
    {
        return size == 0;
    }
    public void addFirst(type val)
    {
        if (sentinel.next == null)
        {
            sentinel.next = new Node<>(sentinel, val, sentinel);
            sentinel.prev = sentinel.next;
        }
        else
        {
            sentinel.next = new Node<>(sentinel, val, sentinel.next);
        }
        size ++;
    }
    public void addLast(type val)
    {
        if (sentinel.next == null)
        {
            this.addFirst(val);
        }
        else
        {
            sentinel.prev = new Node<>(sentinel.prev, val, sentinel);
            sentinel.prev.prev.next = sentinel.prev;
        }
        size ++;
    }
    public void printDeque()
    {
        Node<type> curr = sentinel.next;
        while (curr != sentinel)
        {
            System.out.print(curr.first + " ");
            curr = curr.next;
        }
    }
    public type get(int index)
    {
        Node<type> curr = sentinel;
        do
        {
            curr = curr.next;
            if (curr == sentinel)
            {
                System.out.println("error");
                return null;
            }
            curr = curr.next;
            index --;
        } while (index >= 0);
        return curr.first;
    }
    public type getRecursive(int index)
    {
        return getR(sentinel.next, index);
    }
    private type getR(Node<type> node, int index)
    {
        if (node == sentinel)
        {
            System.out.println("error");
            return null;
        }
        else if (index == 0)
        {
            return node.first;
        }
        else
        {
            return getR(node.next, index - 1);
        }
    }
    public type removeFirst()
    {
        if (size == 0)
        {
            System.out.println("error");
            return null;
        }
        type ret = sentinel.next.first;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size --;
        return ret;
    }
    public type removeLast()
    {
        if (size == 0)
        {
            System.out.println("error");
            return null;
        }
        type ret = sentinel.prev.first;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size --;
        return ret;
    }
}
