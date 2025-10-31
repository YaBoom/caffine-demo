package com.xiaozhu.caffeine.annotation;


import com.xiaozhu.caffeine.common.CacheInstance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 新增缓存
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {


    CacheInstance cacheName();

    /**
     * cache缓存名拼接后缀的参数
     * 可填方法名或者针对这个方法独一无二的标识
     * @return
     */
    String[] cacheNameSuffix() default {};

    /**
     * 缓存的键, 可以填入需要作为缓存依据的参数名,
     * 不写默认所有参数作为依据
     * @return
     */
    String [] keys() default {};

}