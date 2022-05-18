package by.incubator.application.infrastructure.config;

import by.incubator.application.infrastructure.core.Scanner;

public interface Config {
    <T> Class<? extends  T> getImplementation(Class<T> target);
    Scanner getScanner();
}
