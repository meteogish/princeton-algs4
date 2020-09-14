import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private transient Item[] items;
    private transient int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this(10);
    }

    private RandomizedQueue(int size) {
        items = (Item[]) new Object[size];
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return last == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return last;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        
        items[last++] = item;

        if (last == items.length) 
            resize(items.length*2);

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        
        int randomIndex = StdRandom.uniform(last);
        Item randomItem = items[randomIndex];

        items[randomIndex] = items[last-1];
        items[--last] = null; //NOPMD

        if (last >= 0 && last < items.length / 4)
            resize(items.length / 2);
        
        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return items[StdRandom.uniform(last)];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int newSize)
    {
        Item[] newItems = (Item[]) new Object[newSize];

        for (int i = 0; i < last; i++) {
            newItems[i] = items[i];
        }

        items = newItems;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final transient RandomizedQueue<Item> queue;

        RandomizedQueueIterator() {
            queue = new RandomizedQueue<>(last);

            for (int i = 0; i < last; i++) {
                queue.enqueue(items[i]);
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Item next() {
            if (queue.isEmpty()) throw new NoSuchElementException();
            return queue.dequeue();
        }
    }
}