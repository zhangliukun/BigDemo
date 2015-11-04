package com.zlk.bigdemo.framework.cache;

import java.io.Serializable;

/**
 * 支持超时判断特性的cache item，如果希望永久有效，设置超时时间为负数即可
 */
public class ExpireCacheItem<V> implements Serializable {
    private long lastModifyTime;
    private V value;
    private long expireTime;

    public ExpireCacheItem(V value) {
        this.value = value;
        lastModifyTime = System.currentTimeMillis();
        expireTime = -1;
    }

    /**
     * 可以指定value的过期时间
     * @param value
     * @param expireTime 小于0表示不过期
     */
    public ExpireCacheItem(V value,long expireTime) {
        this.value = value;
        lastModifyTime = System.currentTimeMillis();
        this.expireTime = expireTime;
    }

    /**
     * 根据item的超时时间判断并返回相应值
     * @return 如果value过期，则返回null
     */
    public V getValue() {
        if (expireTime <= 0) {
            return value;
        } else {
            long current = System.currentTimeMillis();
            if (current - lastModifyTime > expireTime) {
                return null;
            } else {
                return value;
            }
        }
    }

    /**
     * 根据expireTime超时时间判断并返回相应值
     * @param expireTime 超时时间
     * @return 如果value过期，则返回null
     */
    public V getValue(long expireTime) {
        if (expireTime <= 0) {
            return value;
        } else {
            long current = System.currentTimeMillis();
            if (current - lastModifyTime > expireTime) {
                return null;
            } else {
                return value;
            }
        }
    }

    /**
     * 设置value
     * @return 返回原value值
     */
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        lastModifyTime = System.currentTimeMillis();
        return old;
    }

    public V setValue(V value,long expireTime){
        V old = this.value;
        this.value = value;
        this.expireTime = expireTime;
        lastModifyTime = System.currentTimeMillis();
        return old;
    }
}
