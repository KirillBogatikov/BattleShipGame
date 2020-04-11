package org.battleshipgame.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stream<T> {
    private List<T> items;

    public Stream(List<T> items) {
        this.items = items;
    }

    public List<T> where(Predicate<T> predicate) {
        if(predicate == null) {
            return Collections.emptyList();
        }

        ArrayList<T> result = new ArrayList<>();
        for(T item : items) {
            if(predicate.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public T firstOrNull(Predicate<T> predicate) {
        if(predicate == null) {
            return null;
        }

        ArrayList<T> result = new ArrayList<>();
        for(T item : items) {
            if(predicate.apply(item)) {
                return item;
            }
        }

        return null;
    }

    public <V> V forEach(Consumer<T, V> consumer) {
        V prevent = null;
        for(T item : items) {
            prevent = consumer.consume(item, prevent);
        }
        return prevent;
    }
}
