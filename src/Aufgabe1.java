import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

class Node<T extends Comparable<T>> {
    private T value;
    private int height;
    private Node<T>[] next;
    
    @SuppressWarnings("unchecked cast")
    Node(T value) {
        this.value = value;
        height = randomSize();
        next = (Node<T>[])new Node[height];
    }
    
    T getValue() {
        return value;
    }
    
    int getHeight() {
        return height;
    }
    
    Node<T> getNext(int index) {
        return next[index];
    }
    
    void setNext(int index, Node<T> node) {
        next[index] = node;
    }
    
    static private int randomSize() {
        Random random = new Random();
        
        int size = 1;
        
        while (random.nextBoolean()) {
            size += 1;
        }
        return size;
    }
    
    void insert(Node<T> node) {
        // Try to insert node at one of my successor nodes.
        for (int i = height - 1; i >= 0; i--) {
            if (next[i] != null && next[i].value.compareTo(node.value) < 0) {
                next[i].insert(node);
                for (int j = next[i].height; j < height && j < node.height; j++) {
                    node.next[j] = next[j];
                    next[j] = node;
                }
                return;
            }
        }
        // Insert at this
        for (int i = 0; i < height && i < node.height; i++) {
            node.next[i] = next[i];
            next[i] = node;
        }
    }
    
    @Override
    public String toString() {
        return value + ": " +
            Arrays.stream(next).map(node -> node == null ? null : node.value.toString())
                .collect(Collectors.joining(", "));
    }
}

class SkipList<T extends Comparable<T>> {
    private ArrayList<Node<T>> start = new ArrayList<>();
    
    void insert(T value) {
        Node<T> node = new Node<>(value);
        int currentHeight = start.size();
        
        while (start.size() < node.getHeight()) {
            start.add(node);
        }
        
        for (int i = currentHeight - 1; i >= 0; i--) {
            Node<T> currentNode = start.get(i);
            if (currentNode != null && currentNode.getValue().compareTo(node.getValue()) < 0) {
                currentNode.insert(node);
                for (int j = currentNode.getHeight(); j < currentHeight && j < node.getHeight(); j++) {
                    node.setNext(j, currentNode);
                    start.set(j, node);
                }
                return;
            }
        }
        
        for (int i = 0; i < currentHeight && i < node.getHeight(); i++) {
            node.setNext(i, start.get(i));
            start.set(i, node);
        }
    }
    
    boolean contains(T value) {
        Node<T> node = null;
        
        for (int i = start.size() - 1; i >= 0; i--) {
            int comparison = start.get(i).getValue().compareTo(value);
            
            if (comparison == 0) {
                return true;
            }
            if (comparison < 0) {
                node = start.get(i);
                break;
            }
        }
        
        if (node == null) {
            return false;
        }
        
        while (true) {
            Node<T> nextNode = null;
            for (int i = node.getHeight() - 1; i >= 0; i--) {
                int comparison = node.getNext(i).getValue().compareTo(value);
                
                if (comparison == 0) {
                    return true;
                }
                
                if (comparison < 0) {
                    nextNode = node.getNext(i);
                    break;
                }
            }
            
            if (nextNode == null) {
                return false;
            }
            node = nextNode;
        }
    }
    
    @Override
    public String toString() {
        Node<T> node = start.get(0);
        
        StringBuilder builder = new StringBuilder();
        while (node != null) {
            builder.append(node.toString()).append('\n');
            node = node.getNext(0);
        }
        return builder.toString();
    }
}

public class Aufgabe1 {
    public static void main(String[] args) {
        int[] numbers = {2, 7, 12, 11, 23, 14, 8, 5};
        
        var list = new SkipList<Integer>();
    
        for (int number : numbers) {
            list.insert(number);
            System.out.println(list);
        }
    
        System.out.println(list.contains(11));
        System.out.println(list.contains(13));
    }
}
