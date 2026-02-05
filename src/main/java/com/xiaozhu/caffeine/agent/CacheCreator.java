package com.xiaozhu.caffeine.agent;

import cn.hutool.core.util.ReflectUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiaozhu.caffeine.common.CacheInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyt
 * @description cache的代理端
 */
@Component
public class CacheCreator {
    @Autowired
    private AbstractCacheManager cacheManager;

    /**
     * 获取缓存，如果获取不到创建一个
     *
     * @param cacheInstance
     * @param values
     * @return
     */
    public Cache getCache(CacheInstance cacheInstance, List<String> values) {

        String cacheNameSuffix = String.join("&", values);
        String cacheName = cacheInstance.name() + "&" + cacheNameSuffix;
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            synchronized (cacheName.intern()) {
                cache = new CaffeineCache(cacheName, Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(cacheInstance.getTtl(), TimeUnit.SECONDS)
                        .build());
                Map<String, Cache> caches = (ConcurrentHashMap<String, Cache>) ReflectUtil.getFieldValue(cacheManager, "cacheMap");
                caches.put(cacheName, cache);
            }
        }
        return cache;
    }
}
