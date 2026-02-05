# caffine 基础使用步骤


#### 介绍
caffine 本地缓存应用实践

#### 基础操作步骤
1、创建依赖
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>2.9.3</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.5.7</version>
</dependency>


2、配置cacheManager(注册cache)
@Bean
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
3、创建缓存代理类（CacheCreator代替cacheManager获取缓存和刷选）
根据业务cache名称和前缀来做缓存业务处理


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

4、创建业务数据进行区分的缓存枚举类
CacheInstance 

5、注解
将所有参数作为缓存key, 无需配置keys
@Cacheable(cacheName = CacheInstance.STUDENT_INFO,  //枚举类存放的缓存名
           cacheNameSuffix = "selectStudentList")
情况2:部分参数作为缓存key, 配置keys
@Cacheable(cacheName = CacheInstance.STUDENT_INFO,  //枚举类存放的缓存名
           cacheNameSuffix = "selectStudentList",	//缓存前缀, 对这部分缓存的唯一标识, 这里可以使用方法名, 方便查找和删除
           keys= {"conditon"})	

6、缓存切面
针对获取缓存和删除缓存的标有业务枚举类的cachename业务处理	


7、redis+caffeine（基于java8的高性能缓存库）的高性能缓存方案
  （1）、首先查询caffenine二级缓存有没有数据，如果有直接返回，相反直接走下一步
   （2）、查询一级缓存redis，如果有直接返回，相反直接走下一步
   （3）、查询数据库信息，并针对redis缓存信息保存，然后更新本机caffenine缓存信息，并sub/pub通知其他caffenine节点清楚缓存信息,一旦二级缓存信息不存在，则直接从一级缓存（redis）
加载数据。极端情况下，可以设置caffenine缓存的有效期（即便未能及时更新， 也只是短暂影响）后续会接入专业的消息队列来保障消息的可靠性。


