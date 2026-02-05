package com.xiaozhu.caffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiaozhu.caffeine.common.CacheInstance;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyt
 * @description 缓存配置
 */
@Configuration
public class CacheConfig {
    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean
    @Primary
    public AbstractCacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，CaffeineCache实现了org.springframework.cache.Cache接口
        List<CaffeineCache> caches = new ArrayList<>();
        Arrays.asList(CacheInstance.values()).forEach(cacheInstance -> {
            CaffeineCache caffeineCache = new CaffeineCache(cacheInstance.name(), Caffeine.newBuilder()
                    .recordStats()
                    .expireAfterWrite(cacheInstance.getTtl(), TimeUnit.SECONDS)
                    .build());
            caches.add(caffeineCache);
        });
        cacheManager.setCaches(caches);
         return cacheManager;
    }

    @Bean
    public SimpleKeyGenerator simpleKeyGenerator() {
        return new SimpleKeyGenerator();
    }

}
