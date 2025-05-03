package Util;

import java.util.Iterator;
import Util.map;

public class hash_map<K, V> implements map<K, V> {
    public hash_map() {
        capacity = 16;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            int bucketIndex = 0;
            Node<K, V> current = advance();

            private Node<K, V> advance() {
                while (bucketIndex < capacity && (table[bucketIndex] == null)) {
                    ++bucketIndex;
                }
                return bucketIndex < capacity ? table[bucketIndex] : null;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                K result = current.key;
                current = current.next;
                if (current == null) {
                    ++bucketIndex;
                    current = advance();
                }
                return result;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (K key : this) {
            if (!first) sb.append(", ");
            V value = get(key);
            sb.append(key).append("=").append(value);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                V old = current.value;
                current.value = value;
                return old;
            }
            current = current.next;
        }

        table[index] = new Node<>(key, value, table[index]);
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
            if ((key == null && current.key == null) ||
                    (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
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
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                if (prev == null) {
                    table[index] = current.next;
                }
                else {
                    prev.next = current.next;
                }

                --size;
                return current.value;
            }

            prev = current;
            current = current.next;
        }

        return null;
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

    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; ++i) {
            Node<K, V> current = oldTable[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private final float loadFactor = 0.75f;
}