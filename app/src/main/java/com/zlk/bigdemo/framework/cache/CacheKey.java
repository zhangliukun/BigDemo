package com.zlk.bigdemo.framework.cache;

public enum CacheKey {
    MIXED_CACHE,
    PUBLIC_SERVICE_CACHE,
    APP_LIST_CACHE,
    APP_LIST_PERSIST,
    FREQUENT_CONTACTS_CACHE
    ;

    public int getKey(){
        return ordinal();
    }
}
