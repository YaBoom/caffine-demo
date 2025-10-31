package com.xiaozhu.caffeine.util;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zyt
 * @description
 */
public class CacheUtil {
    public static <T> T getValue(Cache cache, Object key, Class<T> returnClass) {
        if (cache == null || key == null) {
            return null;
        }
        return cache.get(key, returnClass);
    }

    public static String generateCacheKey(Object... keys) {
        new SimpleKey(keys);
        List<Object> objects = Arrays.asList(keys);
        return objects.stream().map(o -> o == null ? "null" : String.valueOf(o)).collect(Collectors.joining("&"));

    }

    public static void cacheValue(Cache cache, Object value, Object... keys) {
        if (null == cache) {
            throw new IllegalArgumentException("内部错误：缓存器为空");
        }
        cache.put(generateCacheKey(keys), value);
    }


    public static String generateCacheKeyByMap(Map<String, Object> argMap) {
        //直接将参数转为json作为缓存key
        return JSONUtil.toJsonStr(argMap);

    }

    public static String generateCacheKeyByMapAndSpecifiedKeys(Map<String, Object> argMap, String... keys) {
        if (ArrayUtil.isEmpty(keys)) {
            throw new IllegalArgumentException("请指定缓存的key");
        }
        //将需要参数作为缓存key
        Map<String, Object> keysMap = new HashMap<>();
        Arrays.stream(keys).forEach(key -> keysMap.put(key, argMap.get(key)));
        return generateCacheKeyByMap(keysMap);
    }
}
