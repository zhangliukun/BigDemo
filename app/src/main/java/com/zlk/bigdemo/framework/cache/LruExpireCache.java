package com.zlk.bigdemo.framework.cache;

import android.support.v4.util.LruCache;

public class LruExpireCache<K, V> extends Cache<K, V> {

    private long expireTime;
    private LruCache<K, ExpireCacheItem<V>> cache;

    /**
     * 如果不需要超时判断，expireTime设置为-1（小于0）
     */
    public LruExpireCache(String accountId, int capacity, long expireTime) {
        super(accountId, capacity);
        cache = new LruCache<K, ExpireCacheItem<V>>(capacity);
        this.expireTime = expireTime;
    }

    @Override
    public V get(K key) {
        ExpireCacheItem<V> v = this.cache.get(key);
        return v == null ? null : v.getValue(expireTime);
    }

    @Override
    public void put(K k, V v) {
        ExpireCacheItem<V> item = cache.get(k);
        if (item == null) {
            cache.put(k, new ExpireCacheItem(v));
        } else {
            item.setValue(v);
        }
    }

    @Override
    public void clear() {
        cache.evictAll();
    }

    public void reset(String accountId, int capacity, long expireTime) {
        super.reset(accountId, capacity);
        this.expireTime = expireTime;
    }
}
