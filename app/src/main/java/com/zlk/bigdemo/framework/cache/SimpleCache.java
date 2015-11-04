package com.zlk.bigdemo.framework.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单缓存器，内部为HashMap
 * 特性：支持线程安全； 支持超时节点；无限容量
 * 接口：void put(K k, V v)：如果不存在，则添加无超时判断的节点
 * 接口：void put(K k, V v, long expireTime)：如果不存在，则添加超时时间为expireTime的节点
 *
 */
public class SimpleCache<K, V> extends Cache<K, V>{

    private ConcurrentHashMap<K, ExpireCacheItem<V>> eden;      // 实际缓存

    public SimpleCache(String accountId, int capacity) {
        super(accountId, capacity);
        this.eden = new ConcurrentHashMap<K, ExpireCacheItem<V>>(capacity);
    }

    @Override
    public V get(K key) {
        ExpireCacheItem<V> v = this.eden.get(key);

        return v == null ? null : v.getValue();
    }

    @Override
    public void put(K k, V v) {
        ExpireCacheItem<V> item = eden.get(k);
        if (item == null) {
            eden.put(k, new ExpireCacheItem(v));
        } else {
            item.setValue(v);
        }
    }

    public void put(K k, V v, long expireTime) {
        ExpireCacheItem<V> item = eden.get(k);
        if (item == null) {
            eden.put(k, new ExpireCacheItem(v, expireTime));
        } else {
            item.setValue(v, expireTime);
        }
    }

    @Override
    public void clear() {
        this.eden.clear();
    }

}


