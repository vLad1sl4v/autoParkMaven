package by.incubator.application.infrastructure.configurators.impl;

import by.incubator.application.infrastructure.core.Context;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.configurators.ObjectConfigurator;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class AutowiredObjectConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object t, Context context) {
        Field[] fields = t.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(t, context.getObject(field.getType()));
            }
        }
    }
}
