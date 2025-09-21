package org.lilbrocodes.elixiry.common.util;

import java.util.HashMap;
import java.util.Map;

public class Counter<T> {
    private final Map<T, Integer> map;
    private final Integer max;

    public Counter(Integer max) {
        this.map = new HashMap<>();
        this.max = max;
    }

    public void add(T key) {
        if (map.containsKey(key)) map.put(key, map.get(key) + 1);
        else map.put(key, 1);
    }

    public boolean valid(T key) {
        return map.getOrDefault(key, 0) <= max;
    }
}
