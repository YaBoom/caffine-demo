package com.xiaozhu.caffeine.aspect;

import com.xiaozhu.caffeine.agent.CacheCreator;
import com.xiaozhu.caffeine.annotation.CacheEvict;
import com.xiaozhu.caffeine.annotation.Cacheable;
import com.xiaozhu.caffeine.common.CacheInstance;
import com.xiaozhu.caffeine.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zyt
 * @description cache的切面处理类
 */
@Slf4j
@Aspect
@Component
public class CacheAspectHandler {
    @Autowired
    private CacheCreator cacheCreator;

    //缓存开启状态 也可以配置到配置文件中读取yml
    private Boolean enableCache = Boolean.TRUE;

    /**
     * 获取缓存, 没有则添加缓存在返回
     * @param pjp
     * @param cacheable
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cacheable)")
    public Object cacheResponse(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
        Object result;

        if (enableCache) {
            //获取参数
            String[] argNames = ((CodeSignature) pjp.getSignature()).getParameterNames();
            Object[] args = pjp.getArgs();
            //生成参数键值对
            Map<String, Object> argMap = new HashMap<>();
            for (int i = 0; i < argNames.length; i++) {
                argMap.put(argNames[i], args[i]);
            }

            String key;
            if(cacheable.keys().length != 0){
                key = CacheUtil.generateCacheKeyByMapAndSpecifiedKeys(argMap, cacheable.keys());
            }else {
                key = CacheUtil.generateCacheKeyByMap(argMap);
            }

            Cache cache = cacheCreator.getCache(cacheable.cacheName(), Arrays.asList(cacheable.cacheNameSuffix()));
            result = cache.get(key, Object.class);
            if (result != null) {
                log.info(String.format("命中缓存,实例：%s, 键：%s ", cache.getName(), key));

            } else {
                result = pjp.proceed();
                cache.put(key, result);
                log.info(String.format("缓存成功,实例：%s, 键：%s ", cache.getName(), key));
            }
        } else {
            //不开启缓存 直接过方法
            result = pjp.proceed();
        }

        return result;

    }

    /**
     * 删除缓存
     * @param pjp
     * @param cacheEvict
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cacheEvict)")
    public Object evictCacheResponse(ProceedingJoinPoint pjp, CacheEvict cacheEvict) throws Throwable {

        CacheInstance[] cacheInstances = cacheEvict.cacheName();
        Arrays.stream(cacheInstances).forEach(cacheInstance -> {
            Cache cache = cacheCreator.getCache(cacheInstance, Arrays.asList(cacheEvict.cacheNameSuffix()));
            if (null != cache) {
                cache.clear();
                log.info(String.format("清除缓存成功,实例：%s ", cache.getName()));
            }
        });
        return pjp.proceed();

    }
}
