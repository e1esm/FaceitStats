package com.esm.faceitstats.component;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SynchronizedCacheManager<K, V> {

    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public SynchronizedCacheManager() {
        startCleanupTask();
    }

    public synchronized void put(K key, V value, long ttlMillis) {
        long expiryTime = System.currentTimeMillis() + ttlMillis;
        cache.put(key, new CacheEntry<>(value, expiryTime));
    }

    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        cache.remove(key);
        return null;
    }

    public synchronized void evict(K key) {
        cache.remove(key);
    }

    public synchronized void clear() {
        cache.clear();
    }

    private void startCleanupTask() {
        executorService.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        }, 0, 1, TimeUnit.MINUTES);
    }

    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;

        CacheEntry(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        public boolean isExpired(long currentTime) {
            return currentTime > expiryTime;
        }
    }
}

