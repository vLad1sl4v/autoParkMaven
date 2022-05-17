package application.infrastructure.configurations;

import application.infrastructure.core.Context;

public interface ObjectConfigurator {
    void configure(Object object, Context context);
}
