package com.example.mavendemo.service;

public interface CacheService {
    // 存
    void setCommonCache(String key, Object value);

    // 取
    Object getFromCommonCache(String key);
}
