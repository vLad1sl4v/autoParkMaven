package by.incubator.application.infrastructure.configurations;

import by.incubator.application.infrastructure.core.Context;

public interface ObjectConfigurator {
    void configure(Object object, Context context);
}
