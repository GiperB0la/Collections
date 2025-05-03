package Util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import Util.map;

public class tree_map<K extends Comparable<K>, V> implements map<K, V> {
    public tree_map() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            Node<K, V> current = minNode(root);
            Node<K, V> lastReturned = null;

            private Node<K, V> successor(Node<K, V> node) {
                if (node.right != null) return minNode(node.right);
                Node<K, V> parent = node.parent;
                while (parent != null && node == parent.right) {
                    node = parent;
                    parent = parent.parent;
                }
                return parent;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                if (current == null) throw new NoSuchElementException();
                lastReturned = current;
                current = successor(current);
                return lastReturned.key;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<K> it = iterator();
        while (it.hasNext()) {
            K key = it.next();
            sb.append(key).append("=").append(get(key));
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException();

        if (root == null) {
            root = new Node<>(key, value);
            size++;
            return null;
        }

        Node<K, V> current = root;
        while (true) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                V old = current.value;
                current.value = value;
                return old;
            }
            else if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node<>(key, value);
                    current.left.parent = current;
                    size++;
                    return null;
                }
                current = current.left;
            }
            else {
                if (current.right == null) {
                    current.right = new Node<>(key, value);
                    current.right.parent = current;
                    size++;
                    return null;
                }
                current = current.right;
            }
        }
    }

    @Override
    public V get(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) return current;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        Node<K, V> node = findNode(key);
        if (node == null) return null;

        V oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node<K, V> node) {
        if (node.left != null && node.right != null) {
            Node<K, V> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            deleteNode(successor);
        }
        else {
            Node<K, V> child = (node.left != null) ? node.left : node.right;
            if (node.parent == null) {
                root = child;
            }
            else if (node == node.parent.left) {
                node.parent.left = child;
            }
            else {
                node.parent.right = child;
            }
            if (child != null) child.parent = node.parent;
        }
    }

    private Node<K, V> minNode(Node<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public void clear() {
        root = null;
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

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right, parent;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;
}