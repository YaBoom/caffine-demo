package com.xiaozhu.caffeine.annotation;

import com.xiaozhu.caffeine.common.CacheInstance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aunero
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {
    CacheInstance[] cacheName() ;
    /**
     * cache缓存名拼接后缀的参数，注意区分顺序
     * @return
     */
    String[] cacheNameSuffix() default {};
}
