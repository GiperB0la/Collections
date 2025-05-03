package Util;

public interface map<K, V> extends Iterable<K> {
    V put(K key, V value);
    V get(K key);
    boolean containsKey(K key);
    V remove(K key);
    void clear();
    boolean isEmpty();
    int size();
}