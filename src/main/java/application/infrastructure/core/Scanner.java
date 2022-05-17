package application.infrastructure.core;

import org.reflections.Reflections;

import java.util.Set;

public interface Scanner {
    <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type);
    Reflections getReflections();
}
