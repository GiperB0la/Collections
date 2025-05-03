package Util;

import Util.set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class tree_set<T extends Comparable<T>> implements set<T> {
    public tree_set() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> next = minNode(root);
            Node<T> lastReturned = null;

            private Node<T> successor(Node<T> node) {
                if (node.right != null) return minNode(node.right);
                Node<T> parent = node.parent;
                while (parent != null && node == parent.right) {
                    node = parent;
                    parent = parent.parent;
                }
                return parent;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public T next() {
                if (next == null) throw new NoSuchElementException();
                lastReturned = next;
                next = successor(next);
                return lastReturned.value;
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
    public boolean add(T value) {
        if (value == null) throw new NullPointerException();

        if (root == null) {
            root = new Node<>(value);
            size++;
            return true;
        }

        Node<T> current = root;
        while (true) {
            int cmp = value.compareTo(current.value);
            if (cmp == 0) return false;
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node<>(value);
                    current.left.parent = current;
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node<>(value);
                    current.right.parent = current;
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    @Override
    public boolean contains(T value) {
        return findNode(value) != null;
    }

    private Node<T> findNode(T value) {
        Node<T> current = root;
        while (current != null) {
            int cmp = value.compareTo(current.value);
            if (cmp == 0) return current;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    @Override
    public boolean remove(T value) {
        Node<T> node = findNode(value);
        if (node == null) return false;

        deleteNode(node);
        size--;
        return true;
    }

    private void deleteNode(Node<T> node) {
        if (node.left != null && node.right != null) {
            Node<T> successor = minNode(node.right);
            node.value = successor.value;
            deleteNode(successor);
        } else {
            Node<T> child = (node.left != null) ? node.left : node.right;
            if (node.parent == null) {
                root = child;
            } else if (node == node.parent.left) {
                node.parent.left = child;
            } else {
                node.parent.right = child;
            }
            if (child != null) child.parent = node.parent;
        }
    }

    private Node<T> minNode(Node<T> node) {
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

    private static class Node<T> {
        T value;
        Node<T> left, right, parent;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root;
    private int size;
}