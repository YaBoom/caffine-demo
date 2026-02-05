package com.xiaozhu.caffeine.common;


import cn.hutool.core.util.RandomUtil;

/**
 * @author Zyt
 * @description 缓存实例的枚举类
 */
public enum  CacheInstance {
    //枚举自行定义
    STUDENT_INFO,				//学生信息
    CLASS_INFO(600, 1024),		//班级信息, 可自定义过期时间和最大数量
    ;

    private int ttl = RandomUtil.randomInt(300, 360);        //默认过期时间  5分钟~6分钟
    private int maxSize = 1024;    //最大數量

    CacheInstance() {
    }


    CacheInstance(int ttl) {
        this.ttl = ttl;
    }

    CacheInstance(int ttl, int maxSize) {
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
