package Util;

import Util.list;
import java.util.Iterator;

public class linked_list<T> implements list<T> {
    public linked_list() {
        head = null;
        tail = null;
        size = 0;
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
                T value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        linked_list<T> other = (linked_list<T>) obj;
        if (this.size != other.size) return false;

        Node<T> current1 = this.head;
        Node<T> current2 = other.head;

        while (current1 != null) {
            if (current1.value == null) {
                if (current2.value != null) return false;
            }
            else {
                if (!current1.value.equals(current2.value)) return false;
            }
            current1 = current1.next;
            current2 = current2.next;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (T item : this) {
            result = 31 * result + (item == null ? 0 : item.hashCode());
        }
        return result;
    }

    @Override
    public boolean add(T value) {
        Node<T> newNode = new Node<T>(value);
        if (head == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        ++size;
        return true;
    }

    @Override
    public boolean add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }
        Node<T> newNode = new Node<T>(value);
        if (index == 0) {
            newNode.next = head;
            if (head != null) head.prev = newNode;
            head = newNode;
            if (tail == null) tail = newNode;
        }
        else if (index == size) {
            newNode.prev = tail;
            if (tail != null) tail.next = newNode;
            tail = newNode;
            if (head == null) head = newNode;
        }
        else {
            Node<T> current = head;
            for (int i = 0; i < index; ++i) {
                current = current.next;
            }
            newNode.prev = current.prev;
            newNode.next = current;
            current.prev.next = newNode;
            current.prev = newNode;
        }
        ++size;
        return true;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }

        Node<T> current = head;
        for (int i = 0; i < index; ++i) {
            current = current.next;
        }

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }

        T removed = current.value;
        current.next = current.prev = null;
        current.value = null;
        --size;
        return removed;
    }

    @Override
    public boolean remove(T value) {
        int idx = indexOf(value);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; ++i) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.prev = null;
            current.next = null;
            current.value = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public T set(int index, T value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }

        Node<T> current = head;
        for (int i = 0; i < index; ++i) {
            current = current.next;
        }

        T old = current.value;
        current.value = value;
        return old;
    }

    @Override
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    @Override
    public int indexOf(T value) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (value == null) {
                if (current.value == null) return index;
            } else {
                if (value.equals(current.value)) return index;
            }
            current = current.next;
            ++index;
        }
        return -1;
    }

    private class Node<T> {
        Node(T value) {
            this.value = value;
        }

        T value;
        Node<T> next;
        Node<T> prev;
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
}