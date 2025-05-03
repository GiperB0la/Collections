package Util;

import java.util.Iterator;
import Util.map;

public class linked_hash_map<K, V> implements map<K, V> {
    public linked_hash_map() {
        capacity = 16;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        head = tail = null;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            Node<K, V> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                K result = current.key;
                current = current.nextOrdered;
                return result;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Node<K, V> current = head;

        while (current != null) {
            sb.append(current.key).append("=").append(current.value);
            if (current.nextOrdered != null) sb.append(", ");
            current = current.nextOrdered;
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null) ||
                    (key != null && key.equals(current.key))) {
                V old = current.value;
                current.value = value;
                return old;
            }
            current = current.nextInBucket;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.nextInBucket = table[index];
        table[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        }
        else {
            tail.nextOrdered = newNode;
            newNode.prevOrdered = tail;
            tail = newNode;
        }

        ++size;
        if ((float) size / capacity >= loadFactor) {
            resize();
        }

        return null;
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.nextInBucket;
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if ((key == null && current.key == null) ||
                    (key != null && key.equals(current.key))) {

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
                return current.value;
            }

            prev = current;
            current = current.nextInBucket;
        }

        return null;
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
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;

        Node<K, V> current = head;
        head = tail = null;

        while (current != null) {
            put(current.key, current.value);
            current = current.nextOrdered;
        }
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> nextInBucket;
        Node<K, V> prevOrdered;
        Node<K, V> nextOrdered;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private final float loadFactor = 0.75f;
    private Node<K, V> head;
    private Node<K, V> tail;
}