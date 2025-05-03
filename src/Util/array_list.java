package Util;

import Util.list;
import java.util.Iterator;

public class array_list<T> implements list<T>, Iterable<T> {
    public array_list() {
        capacity = 10;
        size = 0;
        array_list = (T[]) new Object[capacity];
    }

    public array_list(int initial_capacity) {
        capacity = initial_capacity;
        size = 0;
        array_list = (T[]) new Object[capacity];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int current = 0;
            @Override
            public boolean hasNext() {
                return current < size;
            }
            @Override
            public T next() {
                return array_list[current++];
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; ++i) {
            sb.append(array_list[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        array_list<?> other = (array_list<?>) obj;
        if (this.size != other.size) return false;

        for (int i = 0; i < size; ++i) {
            T a = this.array_list[i];
            Object b = other.array_list[i];
            if (a == null) {
                if (b != null) return false;
            }
            else {
                if (!a.equals(b)) return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; ++i) {
            T element = array_list[i];
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }

    @Override
    public boolean add(T value) {
        if (size >= capacity) {
            ensureCapacity();
        }
        array_list[size] = value;
        ++size;
        return true;
    }

    @Override
    public boolean add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }
        if (size >= capacity) {
            ensureCapacity();
        }
        for (int i = size; i > index; --i) {
            array_list[i] = array_list[i - 1];
        }
        array_list[index] = value;
        ++size;
        return true;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }
        T removed = array_list[index];
        for (int i = index; i < size - 1; ++i) {
            array_list[i] = array_list[i + 1];
        }
        array_list[size - 1] = null;
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
        return array_list[index];
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
        for (int i = 0; i < size; ++i) {
            array_list[i] = null;
        }
        size = 0;
    }

    @Override
    public T set(int index, T value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("[-] Index out of bounds");
        }
        T old = array_list[index];
        array_list[index] = value;
        return old;
    }

    @Override
    public boolean contains(T value) {
        for (int i = 0; i < size; ++i) {
            if (array_list[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(T value) {
        for (int i = 0; i < size; ++i) {
            if (array_list[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private void ensureCapacity() {
        capacity *= 2;
        T[] new_array_list = (T[]) new Object[capacity];
        for (int i = 0; i < size; ++i) {
            new_array_list[i] = array_list[i];
        }
        array_list = new_array_list;
    }

    private T[] array_list;
    private int capacity;
    private int size;
}