package com.example.mavendemo.service.impl;

import com.example.mavendemo.service.CacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CommonCacheImpl implements CacheService {
    private Cache<String,Object> commonCache = null;

    // 首先执行
    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                                .initialCapacity(10)
                                .maximumSize(100)
                                // 超期后按照LRU算法回收
                                .expireAfterWrite(60, TimeUnit.SECONDS)
                                .build();

    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
