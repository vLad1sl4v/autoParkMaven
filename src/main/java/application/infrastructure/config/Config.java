package application.infrastructure.config;

import application.infrastructure.core.Scanner;

public interface Config {
    <T> Class<? extends  T> getImplementation(Class<T> target);
    Scanner getScanner();
}
