package com.zlk.bigdemo.framework.cache;

import com.zlk.bigdemo.freeza.util.LogUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>统一缓存管理模块
 * <p>支持特性：
 * <li>1. 能够实现超时管理；
 * <li>2. 支持多账号管理；
 * <li>3. 支持全局缓存（即账号无关）
 * <li>4. disk缓存
 * <li>5. 缓存cache的扩展
 *
 * <p>缓存说明：
 * <li>1. 每一个cache均代表一个业务的缓存集合，不同的业务需要创建自己的cache；cache可以和账号关联，也可以定义全局cache。
 * <li>2. 如果一个cache和账号关联，则在账号登出后会被清理。请根据业务需求判断创建哪种类型的cache。
 * <li>3. 每个账号均会默认创建一个CacheId.MIXED_CACHE，用于存放那些零碎的业务数据
 *
 * <p>注意：
 * 1. cacheProvider的职能就是管理所有cache，因此各个业务方不建议持有cache引用，否则无法释放cache。每次使用时请使用
 *    getCache()或getValue()获取缓存内容。
 *
 * <p>缓存使用：</p>
 * 1. 单值业务缓存（如常用网址）
 *    <li>（1）为业务分配一个CacheID.DEF_XXXX
 *    <li>（2）getMixedValue() / putMixedValue() 获取和缓存业务值
 *    <p></p>
 * 2 常规业务缓存
 *    <li>（1）为业务分配一个CacheID.XXX
 *    <li>（2）在合适的地方创建cache，createXXXXCache()
 *    <li>（3）使用缓存：getCache() 、getValue()。
 *
 * <p>缓存创建：
 * 1. 启动创建全局缓存
 * 2. 登陆成功后创建账号相关缓存
 **/
public class CacheProvider {

    private static final String sTAG = "CacheProvider";

    /**
     * cacheMap中的key，对应全局账号无关的cache集合
     */
    static final String GLOBAL_CACHE_GROUP_ID = "global_account";
    /**
     * Key: userId
     * Value: cache map
     * 账号对应的缓存表
     */
    final private Map<String, Map<CacheKey, Cache>> cacheMap;

    private static CacheProvider sInstance;

    private CacheProvider() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public static CacheProvider getInstance() {
        if (sInstance == null) {
            sInstance = new CacheProvider();

        }
        return sInstance;
    }


    /**
     * 将新的caches添加的分组里面，但不会覆盖已经存在的cache
     * @param groupId
     * @param caches
     */
    protected void putCaches(String groupId, Map<CacheKey, Cache> caches) {
        if (caches == null || caches.size() == 0){
            return;
        }
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> curCaches;
        synchronized (cacheMap) {
            curCaches = cacheMap.get(groupId);
        }

        if (curCaches == null) {
            cacheMap.put(groupId, caches);
        } else {
            for (CacheKey id : caches.keySet()) {
                if (!caches.containsKey(id)) {
                    curCaches.put(id, caches.get(id));
                }
            }
        }
    }


    /**
     * 获取指定账号的cache
     * 尽可能不要在业务层代码全局维护cache，否则造成cache无法释放
     * @param groupId longNick，如果请求全局账号无关cache，请传null
     * @return 如果不存在，则为null
     */
    public Cache getCache(String groupId, CacheKey id) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
        }
        if (caches != null) {
            return caches.get(id);
        } else {
            return null;
        }
    }

    /**
     * 移除指定账号的cache
     * 如果请求全局账号无关cache，accountId传null
     */
    public void removeCache(String groupId, CacheKey id) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
        }
        if (caches != null) {
            caches.remove(id);
        }
    }

    /**
     * 直接获取指定账号的cache中的value
     * @param groupId 如果请求全局账号无关cache，accountId传null
     * @param key cache中value的key
     */
    public Object getValue(String groupId, CacheKey id, Object key) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
        }
        if (caches == null) {
            return null;
        }

        Cache cache = caches.get(id);
        if (cache != null) {
            return cache.get(key);
        }
        return null;
    }


    /**
     * 获取MIXED_CACHE中的零散缓存数据
     */
    public Object getMixedValue(String groupId, CacheKey id) {
        return getValue(groupId, CacheKey.MIXED_CACHE, id);
    }

    /**
     * 将数据放到MIXED_CACHE缓存中
     * @param id 数据分配的ID
     */
    public void putMixedValue(String groupId, CacheKey id, Object value) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
        }
        if (caches == null) {
            return;
        }
        Cache cache = caches.get(CacheKey.MIXED_CACHE);
        if (cache != null) {
            cache.put(id, value);
        }
    }


    /**
     * 给指定账号创建SimpleCache，注意如果该cache已经存在，则不再创建并返回已有cache
     * @param groupId 输入null，创建全局性账号无关cache
     * @return cache
     */
    public <K, V> Cache<K, V> createSimpleCache(String groupId, CacheKey cacheID, int capacity) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
            if (caches == null) {
                caches = new ConcurrentHashMap<>();
                cacheMap.put(groupId, caches);
                LogUtil.w(sTAG, groupId + " caches not exist, warning.");
            }
        }

        return addSimpleCache(groupId, caches, cacheID, capacity);
    }

    private <K, V> Cache<K, V> addSimpleCache(String groupId, Map<CacheKey, Cache> caches, CacheKey cacheID, int capacity) {
        if (caches != null) {
            Cache<K,V> cache = caches.get(cacheID);
            if (cache == null) {
                cache = new SimpleCache<>(groupId, capacity);
                caches.put(cacheID, cache);
            } else {
                cache.setCapacity(capacity);
            }
            return cache;
        }
        return null;
    }

    /**
     * 给指定账号创建cache，注意如果该cache已经存在，则不再创建并返回已有cache
     * cache支持超时功能，但容量capacity固定，超限后会丢失老数据
     * @param groupId 输入null，创建全局性账号无关cache
     * @param expireTime 小于0（通常为-1）则不检查超时
     */
    public <K, V> Cache<K, V> createExpireCache(String groupId, CacheKey cacheID, int capacity, long expireTime) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }

        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
            if (caches == null) {
                caches = new ConcurrentHashMap<>();
                cacheMap.put(groupId, caches);
                LogUtil.w(sTAG, groupId + " caches not exist, warning.");
            }
        }
        Cache<K,V> cache = caches.get(cacheID);
        if (cache == null) {
            cache = new ExpireCache<>(groupId, capacity, expireTime);
            caches.put(cacheID, cache);
        } else {
            cache.setCapacity(capacity);
        }
        return cache;
    }


    /**
     * 创建一个具有超时功能的lruCache, 如果该cache已经存在，则不再创建并返回已有cache
     * @param groupId 输入null，创建全局性账号无关cache
     * @param expireTime 小于0（通常为-1）则不检查超时
     */
    public <K, V> Cache<K, V> createLruExpireCache(String groupId, CacheKey cacheID, int capacity, long expireTime) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
            if (caches == null) {
                caches = new ConcurrentHashMap<>();
                cacheMap.put(groupId, caches);
                LogUtil.w(sTAG, groupId + " caches not exist, warning.");
            }
        }

        return addLruExpireCache(groupId, caches, cacheID, capacity, expireTime);
    }


    private <K, V> Cache<K, V> addLruExpireCache(String groupId, Map<CacheKey, Cache> caches, CacheKey cacheID, int capacity, long expireTime) {
        if (caches != null) {
            Cache<K,V> cache = caches.get(cacheID);
            if (cache == null) {
                cache = new LruExpireCache<>(groupId, capacity, expireTime);
                caches.put(cacheID, cache);
            }
            return cache;
        }
        return null;
    }

    /**
     * 如果该cache已经存在，则不再创建并返回已有cache
     * @param groupId 如果cache和账号无关，置为null，创建全局cache
     * @param expireTime 小于0（比如-1）则忽略超时
     */
    public <K, V> Cache<K, V> createPersistedLruExpireCache(String groupId, CacheKey cacheID, int capacity, long expireTime) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.get(groupId);
            if (caches == null) {
                caches = new ConcurrentHashMap<>();
                cacheMap.put(groupId, caches);
                LogUtil.w(sTAG, groupId + " caches not exist, warning.");
            }
        }
        Cache<K,V> cache = caches.get(cacheID);
        if (cache == null) {
            try {
                cache = new PersistedLruExpireCache(cacheID.getKey(), groupId, capacity, 1, expireTime);
                caches.put(cacheID, cache);
            } catch (IOException e) {
                LogUtil.e(sTAG, e.getMessage(), e);
            }
        } else {
            cache.setCapacity(capacity);
        }
        return cache;
    }


    public void clean(String groupId) {
        if (groupId == null) {
            groupId = GLOBAL_CACHE_GROUP_ID;
        }
        Map<CacheKey, Cache> caches;
        synchronized (cacheMap) {
            caches = cacheMap.remove(groupId);
        }

        if (caches != null) {
            for (Cache cache : caches.values()) {
                cache.clear();
            }
        }
    }

}
