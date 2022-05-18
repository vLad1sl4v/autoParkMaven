package by.incubator.application.infrastructure.core.impl;

import by.incubator.application.infrastructure.core.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheImpl implements Cache {
    private Map<String, Object> cache;

    public CacheImpl(){
        cache = new HashMap<>();
    }

    @Override
    public boolean contains(Class<?> clazz) {
        String className = clazz.getName();

        return cache.containsKey(className);
    }

    @Override
    public <T> T get(Class<T> clazz) {
        String className = clazz.getName();
        return (T)cache.get(className);
    }

    @Override
    public <T> void put(Class<T> clazz, T value) {
        String className = clazz.getName();
        cache.put(className, value);
    }
}
