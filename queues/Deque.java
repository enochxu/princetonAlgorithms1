import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int length;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Arguement is null.");

        if (isEmpty()) {
            first = new Node();
            first.item = item;
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        length++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Arguement is null.");

        if (isEmpty()) {
            last = new Node();
            last.item = item;         
            first = last;
        } else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.previous = oldLast;
            oldLast.next = last;
        }
        length++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty.");
            
        Item item = first.item;
        first = first.next;
        if (first != null)
            first.previous = null;
        length--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty.");
        
        Item item = last.item;
        last = last.previous;
        if (last != null)
            last.next = null;
        length--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not implemented.");
        }

        @Override
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException("No more items.");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> animals = new Deque<String>();
        System.out.println("Deque Empty: " + animals.isEmpty());
        animals.addFirst("Lion");
        animals.addFirst("Fish");
        animals.addLast("Cat");
        animals.addLast("Dog");
        System.out.println("Current Size: " + animals.size());
        System.out.println("Deque Empty: " + animals.isEmpty());
        animals.removeFirst();
        animals.addFirst("Seal");
        animals.removeLast();

        for (String animal: animals) {
            System.out.println(animal);
        }
        System.out.println("Current Size: " + animals.size());

        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.removeLast();
        System.out.println(deque.isEmpty());
    }

}
