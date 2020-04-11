package org.battleshipgame.core;

import org.battleshipgame.geometry.Point;
import org.battleshipgame.utils.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Info {
    private Set<Point> misses;
    private Set<Point> flames;
    private Set<Point> wrecks;
    private List<InfoChangeListener> listenersList;
    private Stream<InfoChangeListener> listeners;

    public Info() {
        misses = new HashSet<>();
        flames = new HashSet<>();
        wrecks = new HashSet<>();
        listeners = new Stream<>(listenersList = new ArrayList<>());
    }

    public Collection<Point> misses() {
        return misses;
    }

    public Collection<Point> flames() {
        return flames;
    }

    public Collection<Point> wrecks() {
        return wrecks;
    }

    public void tryAgain(Point point) {
        listeners.forEach((listener, prevent) -> {
            listener.onTryAgain(point);
            return null;
        });
    }

    public void miss(Point point) {
        if(point.x < 0 || point.y < 0 || point.x > 9 || point.y > 9) {
            return;
        }

        if(misses.contains(point) || wrecks.contains(point)) {
            return;
        }

        if(misses.add(point)) {
            listeners.forEach((listener, prevent) -> {
                listener.onMiss(point);
                return null;
            });
        }
    }

    public void flame(Point point) {
        if(point.x < 0 || point.y < 0 || point.x > 9 || point.y > 9) {
            return;
        }

        flames.add(point);
        listeners.forEach((listener, prevent) -> {
            listener.onFlame(point);
            return null;
        });
    }

    public void wreck(Point point) {
        if(point.x < 0 || point.y < 0 || point.x > 9 || point.y > 9) {
            return;
        }

        wrecks.add(point);
        listeners.forEach((listener, prevent) -> {
            listener.onWreck(point);
            return null;
        });
    }

    public void addListener(InfoChangeListener listener) {
        this.listenersList.add(listener);
    }
}
