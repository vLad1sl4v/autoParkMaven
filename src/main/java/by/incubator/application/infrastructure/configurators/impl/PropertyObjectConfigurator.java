package by.incubator.application.infrastructure.configurators.impl;

import by.incubator.application.infrastructure.core.Context;
import by.incubator.application.infrastructure.configurators.ObjectConfigurator;
import by.incubator.application.infrastructure.core.annotations.Property;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class PropertyObjectConfigurator implements ObjectConfigurator {
    private final Map<String, String> properties;

    @SneakyThrows
    public PropertyObjectConfigurator() {
        URL path = this.getClass().getClassLoader()
                .getResource("application.properties");

        if (path == null) {
            throw new FileNotFoundException(String.format("File '%s' not found",
                    "by.incubator.application properties"));
        }

        Stream<String> lines = new BufferedReader(new InputStreamReader(path.openStream())).lines();
        properties = lines.map(line -> line.split("="))
                .collect(toMap(arr -> arr[0], arr -> arr[1]));
    }

    @Override
    @SneakyThrows
    public void configure(Object object, Context context) {
        String key;
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Property.class)) {

                key = field.getAnnotation(Property.class).value();

                if (key.isEmpty()) {
                    key = field.getName();
                }

                field.setAccessible(true);
                field.set(object, properties.get(key));
            }
        }
    }
}
