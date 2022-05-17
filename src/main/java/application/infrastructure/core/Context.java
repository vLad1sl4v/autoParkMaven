package application.infrastructure.core;

import application.infrastructure.config.Config;

public interface Context {
    <T> T getObject(Class<T> type);
    Config getConfig();
}
