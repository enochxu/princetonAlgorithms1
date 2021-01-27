import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int queueLength;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        queueLength = 0;
    }

    private void resize(int newArrayLength) {
        Item[] temp = (Item[]) new Object[newArrayLength];
        for (int i = 0; i < queueLength; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueLength == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueLength;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Arguement is null.");
        
        if (queueLength == queue.length) 
            resize(queue.length * 2);
        queue[queueLength] = item;
        queueLength++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty.");

        int randomIndex = StdRandom.uniform(queueLength);
        Item randomItem = queue[randomIndex];
        queueLength--;
        queue[randomIndex] = queue[queueLength]; // Remove random Item and replace with last item.
        queue[queueLength] = null;

        if (queueLength > 0 && queueLength == queue.length / 4)
            resize(queue.length / 2);
        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty.");

        return queue[StdRandom.uniform(queueLength)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }
    
    private class RandomizedIterator implements Iterator<Item> {
        // currently not random, randomize
        private final int[] indeces; // needed to shuffle through.
        private int currentIndex; // indicates position in the shuffled indeces array.

        public RandomizedIterator() {
            indeces = new int[queueLength];
            for (int i = 0; i < queueLength; i++) {
                indeces[i] = i;
            }
            StdRandom.shuffle(indeces);
            currentIndex = 0;
        }


        @Override
        public boolean hasNext() {
            return currentIndex < queueLength;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not implemented.");
        }

        @Override
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException("No more items.");
            
            Item item = queue[indeces[currentIndex]];
            currentIndex++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> animals = new RandomizedQueue<String>();
        System.out.println("Deque Empty: " + animals.isEmpty());
        System.out.println("Current Size: " + animals.size());
        animals.enqueue("Lion");
        animals.enqueue("Fish");
        animals.enqueue("Cat");
        animals.enqueue("Dog");
        System.out.println("Random Item: " + animals.sample());
        System.out.println("Current Size: " + animals.size());
        System.out.println("Deque Empty: " + animals.isEmpty());
        System.out.println("Random Item: " + animals.dequeue());
        animals.enqueue("Seal");
        animals.dequeue();

        for (String animal: animals) {
            System.out.println(animal);
        }
        System.out.println("Current Size: " + animals.size());

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
    }

}