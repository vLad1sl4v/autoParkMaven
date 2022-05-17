package application.infrastructure.config.impl;

import application.infrastructure.config.Config;
import application.infrastructure.core.Scanner;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class JavaConfig implements Config {
    private final Scanner scanner;
    private final Map<Class<?>, Class<?>> interfaceToImplementation;

    @Override
    public <T> Class<? extends T> getImplementation(Class<T> target) {
        Set<Class<? extends T>> realizators = scanner.getSubTypesOf(target);

        if (realizators.size() != 1) {
            if(interfaceToImplementation.get(target) == null) {
                throw new RuntimeException("target interface has 0 or more than 1 impl");
            }

            return (Class<? extends T>) interfaceToImplementation.get(target);
        } else {
            return realizators.iterator().next();
        }
    }

    @Override
    public Scanner getScanner() {
        return scanner;
    }
}
