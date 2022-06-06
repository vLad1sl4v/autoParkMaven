package by.incubator.application.infrastructure.configurators;

import by.incubator.application.infrastructure.core.Context;

public interface ObjectConfigurator {
    void configure(Object object, Context context);
}
