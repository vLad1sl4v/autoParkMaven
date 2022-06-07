package by.incubator.application.infrastructure.core;

import by.incubator.application.infrastructure.configurators.ObjectConfigurator;
import org.reflections.Reflections;

import java.util.Set;

public interface Scanner {
    <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type);
    Reflections getReflections();
}
