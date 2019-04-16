package org.incendo.map;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess") public class LazyEntry<K, V> implements Map.Entry<K, V> {

    private final K key;
    private final Supplier<V> valueSupplier;

    public LazyEntry(@NotNull final K key, @NotNull final Supplier<V> valueSupplier) {
        this.key = key;
        this.valueSupplier = valueSupplier;
    }

    @Override public K getKey() {
        return this.key;
    }

    @Override public V getValue() {
        return this.valueSupplier.get();
    }

    @Override public V setValue(V v) {
        throw new UnsupportedOperationException("Cannot replace value");
    }

}
