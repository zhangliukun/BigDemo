package com.zlk.bigdemo.framework.cache;

public abstract class Cache<K, V> {
    /**
     * userId用于保证切换账户后缓存内容全部失效
     */
    protected String userId;
    /**
     * 缓存容量
     */
    protected int capacity;

    public Cache(String userId, int capacity) {
        this.userId = userId;
        this.capacity = capacity;
    }

    /**
     * 判断当前缓存是否为指定的userId
     * @param userId
     * @return
     */
    public boolean equal(String userId) {
        return this.userId == userId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public abstract V get(K key);

    public abstract void put(K k, V v);

    public abstract void clear();

    /**
     * 重置缓存
     * @param userId
     * @param capacity
     */
    public void reset(String userId, int capacity) {
        this.userId = userId;
        this.capacity = capacity;
        clear();
    }
}
