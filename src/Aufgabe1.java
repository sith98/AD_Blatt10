import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class Node<T extends Comparable<T>> {
    int height;
    T value;
    List<Node<T>> next;

    Node(T value, int height) {
        this.value = value;
        this.height = height;
        next = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            next.add(null);
        }
    }

    public void insert(Node<T> node) {
        while (next.size() < node.height) {
            next.add(node);
        }

        Node<T> firstNode;
        for (int i = next.size() - 1; i >= 0; i--) {
            firstNode = next.get(i);
            if (firstNode != null && firstNode.value.compareTo(value) < 0) {
                firstNode.insert(node);
                return;
            }
        }
        
        for (int i = 0; i < node.height; i++) {
            if (next.get(i) != node) {
                node.next.set(i, next.get(i));
                next.set(i, node);
            }
        }
    }
}

class SkipList<T extends Comparable<T>> {
    ArrayList<Node<T>> heads = new ArrayList<>();

    void insert(T value) {
        int height = randomHeight();
        Node<T> node = new Node<>(value, height);

        int previousHeight = heads.size();

        while (heads.size() < node.height) {
            heads.add(node);
        }

        Node<T> firstNode;
        for (int i = previousHeight - 1; i >= 0; i--) {
            firstNode = heads.get(i);
            if (firstNode != null && firstNode.value.compareTo(value) < 0) {
                firstNode.insert(node);
                return;
            }
        }
        for (int i = 0; i < node.height; i++) {
            if (heads.get(i) != node) {
                node.next.set(i, heads.get(i));
                heads.set(i, node);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Node<T> node = heads.get(0);
        while (node != null) {
            builder
                .append(node.value)
                .append(": ")
                .append(
                    node.next.stream()
                        .map(it -> it == null ? null : it.value.toString())
                        .collect(Collectors.joining(", "))
                )
                .append('\n');
            node = node.next.get(0);
        }
        return builder.toString();
    }

    private static int randomHeight() {
        Random random = new Random();
        int height = 1;
        while (random.nextBoolean()) {
            height += 1;
        }
        return height;
    }
}

public class Aufgabe1 {
    public static void main(String[] args) {
        int[] numbers = {2, 7, 12, 11, 23, 14, 8, 5};
        SkipList<Integer> list = new SkipList<>();
        for (int number : numbers) {
            list.insert(number);
            System.out.println(list);
        }
    }
}
