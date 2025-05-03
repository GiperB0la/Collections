package Util;

public interface collection<T> extends Iterable<T> {
    boolean add(T value);
    boolean remove(T value);
    boolean contains(T value);
    void clear();
    boolean isEmpty();
    int size();
}