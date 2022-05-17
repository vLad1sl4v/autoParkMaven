package application.infrastructure.core.impl;

import application.infrastructure.config.Config;
import application.infrastructure.config.impl.JavaConfig;
import application.infrastructure.core.Cache;
import application.infrastructure.core.Context;
import application.infrastructure.core.ObjectFactory;

import java.util.Map;

public class ApplicationContext implements Context {
    private final Config config;
    private final Cache cache;
    private final ObjectFactory factory;

    public ApplicationContext(String packageToScan, Map<Class<?>,Class<?>> interfaceToImplementation) {
        this.config = new JavaConfig(new ScannerImpl(packageToScan), interfaceToImplementation);
        this.cache = new CacheImpl();
        cache.put(Context.class, this);
        this.factory = new ObjectFactoryImpl(this);
    }

    @Override
    public <T> T getObject(Class<T> type) {
        T cacheObj;

        if (cache.contains(type)) {
            return cache.get(type);
        }

        if (type.isInterface()) {
            cacheObj = factory.createObject(config.getImplementation(type));
        } else {
            cacheObj = factory.createObject(type);
        }

        cache.put(type, cacheObj);

        return cacheObj;
    }

    @Override
    public Config getConfig() {
        return config;
    }
}
