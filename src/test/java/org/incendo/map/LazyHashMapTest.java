package org.incendo.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LazyHashMapTest {

    private LazyMap<String, String> lazyMap = new LazyHashMap<>();
    private AtomicInteger counter = new AtomicInteger(1);

    @Before public void setUp() {
        lazyMap.put("loaded_key", "value");
        lazyMap.put("unloaded_key", () -> "loaded value");
        lazyMap.put("illegal_key", () -> {
            throw new UnsupportedOperationException("Cannot load this value!");
        });
        lazyMap.put("incrementing_value", () -> String.format("value (%d)", counter.incrementAndGet()));
    }

    @Test public void containsLoaded() {
        Assert.assertTrue(lazyMap.containsLoaded("loaded_key"));
    }

    @Test public void getLoaded() {
        Assert.assertEquals("value", lazyMap.get("loaded_key"));
    }

    @Test public void getValueSupplier() {
        Assert.assertNotNull(lazyMap.get("unloaded_key"));
    }

    @Test public void size() {
        Assert.assertEquals(4, lazyMap.size());
    }

    @Test public void isEmpty() {
        Assert.assertFalse(lazyMap.isEmpty());
    }

    @Test public void get() {
        Assert.assertEquals("value", lazyMap.get("loaded_key"));
        Assert.assertEquals("loaded value", lazyMap.get("unloaded_key"));
        boolean threw = false;
        try {
            lazyMap.get("illegal_key");
        } catch (final Exception e) {
            threw = true;
        }
        Assert.assertTrue(threw);
        // Make sure that loaded values are stored, and so that values are only calculated once
        Assert.assertEquals(lazyMap.get("incrementing_value"), lazyMap.get("incrementing_value"));
    }

    @Test public void keySet() {
        final String[] expectedKeys = new String[] {"loaded_key", "unloaded_key", "illegal_key"};
        final Collection<String> keys = lazyMap.keySet();
        for (final String expectedKey : expectedKeys) {
            Assert.assertTrue(keys.contains(expectedKey));
        }
    }

    @Test public void entrySet() {
        for (final Map.Entry<String, String> entry : lazyMap.entrySet()) {
            Assert.assertNotNull(entry.getKey());
        }
    }

}
