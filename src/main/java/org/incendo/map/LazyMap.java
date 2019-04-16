package org.incendo.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Map implementation with the ability to lazy load values
 *
 * @param <K> Key type
 * @param <V> Value type
 */
@SuppressWarnings("unused") public abstract class LazyMap<K, V> implements Map<K, V> {

    @Override public V get(@NotNull final Object o) {
        V value = this.getLoaded(o);
        if (value == null) {
            final Supplier<V> supplier = getValueSupplier(o);
            if (supplier != null) {
                value = supplier.get();
                if (value != null) {
                    this.put((K) o, value);
                }
            }
        }
        return value;
    }

    /**
     * Add a supplier to the map
     *
     * @param key      Key
     * @param supplier Value supplier
     * @return The previous value for the specified key, or null
     */
    @Nullable public abstract Supplier<V> put(@NotNull final K key, @NotNull final Supplier<V> supplier);

    /**
     * Check if there there is a stored value for the given key.
     *
     * @param key non-null key
     * @return true if the value is loaded, false if not
     */
    public abstract boolean containsLoaded(@NotNull final Object key);

    /**
     * Get a loaded value. Will return null if there is no pre-loaded
     * value for the given key.
     *
     * @param key non-null key
     * @return value if it exists, or null if it doesn't
     */
    @Nullable public abstract V getLoaded(@NotNull final Object key);

    /**
     * Get a value supplier. Will return null if there is no value
     * supplier for the given key.
     *
     * @param key non-null key
     * @return value supplier
     */
    @Nullable public abstract Supplier<V> getValueSupplier(@NotNull final Object key);

    @NotNull @Override public Set<Entry<K, V>> entrySet() {
        final Set<K> keySet = this.keySet();
        final Set<Entry<K, V>> entrySet = new HashSet<>();
        for (final K key : keySet) {
            if (this.containsLoaded(key)) {
                entrySet.add(new AbstractMap.SimpleEntry<>(key, this.getLoaded(key)));
            } else if (this.containsKey(key)) {
                final Supplier<V> supplier = this.getValueSupplier(key);
                if (supplier != null) {
                    entrySet.add(new LazyEntry<>(key, supplier));
                }
            }
        }
        return entrySet;
    }

}
