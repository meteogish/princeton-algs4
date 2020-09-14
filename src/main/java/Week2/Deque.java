import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private transient Item item = null;
        private transient Node next = null;
        private transient Node prev = null;

        Node()
        {
        }

        Node(Item item, Node next, Node prev)
        {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private final transient Node pre;
    private final transient Node post;

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private transient int size;

    // construct an empty deque
    public Deque() 
    {
        pre = new Node();
        post = new Node();
        pre.next = post;
        post.prev = pre;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null) throw new IllegalArgumentException();

        Node newFirst = new Node(item, pre.next, pre);
        pre.next.prev = newFirst;
        pre.next = newFirst;

        ++size;
    }
    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null) throw new IllegalArgumentException();

        Node newLast = new Node(item, post, post.prev);
        post.prev.next = newLast;
        post.prev = newLast;
        
        ++size;
    }
    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = pre.next.item;
        
        pre.next.next.prev = pre;
        pre.next = pre.next.next;
        
        --size;
        return item;
    }
    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = post.prev.item;

        post.prev.prev.next = post;
        post.prev = post.prev.prev;

        --size;
        return item;
    }
    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private transient Node current;

        DequeIterator() {
            current = pre.next;
        }

        @Override
        public boolean hasNext() {
            return current.item != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
         deque.addLast(43);
         deque.addLast(21);

         for (Integer i : deque)
         {
            for (Integer j : deque)
            {
                System.out.println(i + " " + j);
            }
         }

    }
}