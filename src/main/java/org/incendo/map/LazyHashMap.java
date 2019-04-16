package org.incendo.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@link HashMap} implementation of {@link LazyMap}
 * @param <K> Key type
 * @param <V> Value type
 * {@inheritDoc}
 */
@SuppressWarnings({"SuspiciousMethodCalls", "unused"}) public final class LazyHashMap<K, V> extends LazyMap<K, V> {

    private final Map<K, V> loadedValues;
    private final Map<K, Supplier<V>> valueSuppliers;

    /**
     * Initialize a new lazy hash map with an expected map size
     *
     * @param expectedSize expected size
     */
    public LazyHashMap(@Range(from = 1L, to = 9223372036854775807L) final int expectedSize) {
        this.loadedValues = new HashMap<>(expectedSize);
        this.valueSuppliers = new HashMap<>(expectedSize);
    }

    /**
     * Initialize a new lazy hash map
     */
    public LazyHashMap() {
        this.loadedValues = new HashMap<>();
        this.valueSuppliers = new HashMap<>();
    }

    @Override public boolean containsLoaded(@NotNull final Object key) {
        return this.loadedValues.containsKey(key);
    }

    @Nullable @Override public V getLoaded(@NotNull final Object key) {
        return this.loadedValues.get(key);
    }

    @Override public @Nullable Supplier<V> getValueSupplier(@NotNull Object key) {
        return this.valueSuppliers.get(key);
    }

    @Override public int size() {
        return this.valueSuppliers.size() + this.loadedValues.size();
    }

    @Override public boolean isEmpty() {
        return this.valueSuppliers.isEmpty() && this.loadedValues.isEmpty();
    }

    @Override public boolean containsKey(final Object o) {
        return this.loadedValues.containsKey(o) || this.valueSuppliers.containsKey(o);
    }

    @Override public boolean containsValue(final Object o) {
        throw new UnsupportedOperationException("Cannot check for values");
    }

    @Nullable @Override public V put(@NotNull final K k, @Nullable final V v) {
        final V value = this.loadedValues.put(k, v);
        this.valueSuppliers.remove(k); // Make sure there's no supplier, if the value is specified
                                       // directly
        return value;
    }

    @Override public Supplier<V> put(@NotNull final K key, @NotNull final Supplier<V> supplier) {
        return this.valueSuppliers.put(key, supplier);
    }

    @Nullable @Override public V remove(@NotNull final Object o) {
        V value = this.loadedValues.remove(o);
        if (value == null) {
            this.valueSuppliers.remove(o);
        }
        return value;
    }

    @Override public void putAll(@NotNull Map<? extends K, ? extends V> map) {
        this.loadedValues.putAll(map);
    }

    @Override public void clear() {
        this.loadedValues.clear();
        this.valueSuppliers.clear();
    }

    @NotNull @Override public Set<K> keySet() {
        final Set<K> keys = new HashSet<>(this.size());
        keys.addAll(this.loadedValues.keySet());
        keys.addAll(this.valueSuppliers.keySet());
        return keys;
    }

    @NotNull @Override public Collection<V> values() {
        // TODO: Should this be allowed?
        final Collection<V> values = new ArrayList<>(this.size());
        values.addAll(this.loadedValues.values());
        for (final Supplier<V> valueSupplier : this.valueSuppliers.values()) {
            values.add(valueSupplier.get());
        }
        return values;
    }

}
