package application.infrastructure.configurations.impl;

import application.infrastructure.core.Context;
import application.infrastructure.core.annotations.Autowired;
import application.infrastructure.configurations.ObjectConfigurator;
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
