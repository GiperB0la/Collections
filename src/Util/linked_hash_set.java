package Util;

import Util.set;
import java.util.Iterator;

public class linked_hash_set<T> implements set<T> {
    public linked_hash_set() {
        capacity = 16;
        size = 0;
        table = (Node<T>[]) new Node[capacity];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T val = current.value;
                current = current.nextOrdered;
                return val;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Node<T> current = head;

        while (current != null) {
            sb.append(current.value);
            if (current.nextOrdered != null) sb.append(", ");
            current = current.nextOrdered;
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean add(T value) {
        if (contains(value)) return false;

        if ((float) size / capacity >= loadFactor) {
            resize();
        }

        int index = hash(value);
        Node<T> newNode = new Node<>(value);
        newNode.nextInBucket = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.nextOrdered = newNode;
            newNode.prevOrdered = tail;
            tail = newNode;
        }

        ++size;
        return true;
    }

    @Override
    public boolean contains(T value) {
        int index = hash(value);
        Node<T> current = table[index];

        while (current != null) {
            if ((value == null && current.value == null) ||
                    (value != null && value.equals(current.value))) {
                return true;
            }
            current = current.nextInBucket;
        }

        return false;
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
                    table[index] = current.nextInBucket;
                }
                else {
                    prev.nextInBucket = current.nextInBucket;
                }

                if (current.prevOrdered != null) {
                    current.prevOrdered.nextOrdered = current.nextOrdered;
                }
                else {
                    head = current.nextOrdered;
                }

                if (current.nextOrdered != null) {
                    current.nextOrdered.prevOrdered = current.prevOrdered;
                }
                else {
                    tail = current.prevOrdered;
                }

                --size;
                return true;
            }

            prev = current;
            current = current.nextInBucket;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; ++i) {
            table[i] = null;
        }
        head = tail = null;
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

    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Node<T>[] oldTable = table;
        table = (Node<T>[]) new Node[capacity];

        Node<T> current = head;
        head = tail = null;
        size = 0;

        while (current != null) {
            add(current.value);
            current = current.nextOrdered;
        }
    }

    private int hash(T value) {
        return (value == null ? 0 : Math.abs(value.hashCode())) % capacity;
    }

    private static class Node<T> {
        T value;
        Node<T> nextInBucket;
        Node<T> prevOrdered;
        Node<T> nextOrdered;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T>[] table;
    private int capacity;
    private int size;
    private Node<T> head;
    private Node<T> tail;
    private final float loadFactor = 0.75f;
}