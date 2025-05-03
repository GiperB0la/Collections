package Util;

import Util.collection;

public interface list<T> extends collection<T> {
    T get(int index);
    T remove(int index);
    T set(int index, T value);
    boolean add(int index, T value);
    int indexOf(T value);
}