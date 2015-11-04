package com.zlk.bigdemo.framework.cache;

import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExpireCache<K, V> extends Cache<K, V>{

    private long expireTime;
    private final Lock lock = new ReentrantLock();
    private ConcurrentHashMap<K, ExpireCacheItem<V>> eden;      // 实际缓存
    private WeakHashMap<K, ExpireCacheItem<V>> perm;             // 缓存溢出后的弱引用临时区


    public ExpireCache(String accountId, int capacity, long expireTime) {
        super(accountId, capacity);
        this.expireTime = expireTime;
        this.eden = new ConcurrentHashMap<K, ExpireCacheItem<V>>(capacity);
        this.perm= new WeakHashMap<K, ExpireCacheItem<V>>(capacity);
    }

    @Override
    public V get(K key) {
        ExpireCacheItem<V> v = this.eden.get(key);
        if (v == null) {
            lock.lock();
            try{
                v = this.perm.get(key);
            }finally{
                lock.unlock();
            }
            if (v != null) {
                this.eden.put(key, v);
            }
        }
        return v == null ? null : v.getValue(expireTime);
    }

    @Override
    public void put(K k, V v) {
        if (this.eden.size() >= capacity) {
            lock.lock();
            try{
                this.perm.putAll(this.eden);
            }finally{
                lock.unlock();
            }
            this.eden.clear();
        }
        this.eden.put(k, new ExpireCacheItem(v));
    }

    @Override
    public void clear() {
        this.eden.clear();
        this.perm.clear();
    }

    public void reset(String accountId, int capacity, long expireTime) {
        super.reset(accountId, capacity);
        this.expireTime = expireTime;
    }

}


