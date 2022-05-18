package by.incubator.application.infrastructure.core;

import by.incubator.application.infrastructure.config.Config;

public interface Context {
    <T> T getObject(Class<T> type);
    Config getConfig();
}
