package org.battleshipgame.utils;

public interface Consumer<T, V> {
    public V consume(T item, V prevent);
}
