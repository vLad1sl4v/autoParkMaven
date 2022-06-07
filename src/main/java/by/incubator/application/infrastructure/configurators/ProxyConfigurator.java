package by.incubator.application.infrastructure.configurators;

import by.incubator.application.infrastructure.core.Context;

public interface ProxyConfigurator {
    <T> T makeProxy(T object, Class<T> implementation, Context context);
}
