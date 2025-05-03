package Util;

import Util.set;
import java.util.Iterator;

public class hash_set<T> implements set<T> {
    public hash_set() {
        capacity = 16;
        size = 0;
        table = (Node<T>[]) new Node[capacity];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int bucketIndex = 0;
            private Node<T> current = getNextNonEmptyBucket();

            private Node<T> getNextNonEmptyBucket() {
                while (bucketIndex < capacity && table[bucketIndex] == null) {
                    bucketIndex++;
                }
                return bucketIndex < capacity ? table[bucketIndex] : null;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T result = current.value;
                current = current.next;
                if (current == null) {
                    bucketIndex++;
                    current = getNextNonEmptyBucket();
                }
                return result;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = 0;
        for (T value : this) {
            result += (value == null ? 0 : value.hashCode());
        }
        return result;
    }

    @Override
    public boolean add(T value) {
        int index = hash(value);
        Node<T> current = table[index];

        while (current != null) {
            if (value == null) {
                if (current.value == null) return false;
            }
            else {
                if (value.equals(current.value)) return false;
            }
            current = current.next;
        }

        table[index] = new Node<>(value, table[index]);
        ++size;

        if ((float) size / capacity >= 0.75f) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(T value) {
        int index = hash(value);
        Node<T> current = table[index];
        Node<T> prev = null;

        while (current != null) {
            if ((value == null && current.value == null) ||
                    (value != null && value.equals(current.value))) {

                if (prev == null) {
                    table[index] = current.next;
                }
                else {
                    prev.next = current.next;
                }

                --size;
                return true;
            }

            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(T value) {
        int index = hash(value);
        Node<T> current = table[index];

        while (current != null) {
            if (value == null) {
                if (current.value == null) return true;
            }
            else {
                if (value.equals(current.value)) return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; ++i) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private int hash(T value) {
        return (value == null ? 0 : Math.abs(value.hashCode())) % capacity;
    }

    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Node<T>[] oldTable = table;
        table = (Node<T>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; ++i) {
            Node<T> current = oldTable[i];
            while (current != null) {
                add(current.value);
                current = current.next;
            }
        }
    }

    private static class Node<T> {
        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        T value;
        Node<T> next;
    }

    private Node<T>[] table;
    private int capacity;
    private int size;
}