package org.lilbrocodes.elixiry.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBuilder<B, V> {
    private final List<V> list = new ArrayList<>();
    private final B parent;

    public ListBuilder(B parent) {
        this.parent = parent;
    }

    public ListBuilder<B, V> push(V elem) {
        list.add(elem);
        return this;
    }

    @SafeVarargs
    public final ListBuilder<B, V> push(V... elements) {
        list.addAll(Arrays.stream(elements).toList());
        return this;
    }

    public ListBuilder<B, V> pop() {
        if (!list.isEmpty()) list.remove(list.size() - 1);
        return this;
    }

    public ListBuilder<B, V> clear() {
        list.clear();
        return this;
    }

    public B end() {
        return parent;
    }

    public List<V> build() {
        return new ArrayList<>(list);
    }
}
