package by.incubator.application.infrastructure.threads.configutators;

import by.incubator.application.infrastructure.configurators.ProxyConfigurator;
import by.incubator.application.infrastructure.core.Context;
import by.incubator.application.infrastructure.threads.annotations.Schedule;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.concurrent.*;

public class ScheduleConfigurator implements ProxyConfigurator {
    @Override
    public <T> T makeProxy(T object, Class<T> implementation, Context context) {
        for (Method method : implementation.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Schedule.class)) {
                checkPublicVoid(method);
                return (T) Enhancer.create(implementation, (MethodInterceptor) this::invoke);
            }
        }

        return object;
    }

    @SneakyThrows
    private Object invoke(Object object, Method method, Object[] args, MethodProxy methodProxy) {
        Schedule scheduleSync = method.getAnnotation(Schedule.class);
        if (scheduleSync != null) {
            System.out.println(method);
            Thread thread = new Thread(() -> this.invoker(object, methodProxy, args, scheduleSync.timeout(), scheduleSync.delta()));

            thread.setDaemon(true);
            thread.start();

            return null;
        }

        return  methodProxy.invokeSuper(object, args);
    }

    private void invoker(Object object, MethodProxy method, Object[] args, int milliseconds, int delta) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread invokeThread = new Thread(() -> {
                        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
                            Thread fThread = Executors.defaultThreadFactory().newThread(r);
                            fThread.setDaemon(true);
                            return fThread;
                        });
                        try {
                            executorService.submit(() -> {
                                try {
                                    return method.invokeSuper(object, args);
                                } catch (Throwable ignore) {}
                                return null;
                            }).get(milliseconds, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            executorService.shutdownNow();
                        }
                        executorService.shutdown();
                    });
                    invokeThread.setDaemon(true);
                    invokeThread.start();
                    Thread.currentThread().sleep(delta);
                } catch (InterruptedException ignore) {}
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void checkPublicVoid(Method method) {
        if (!method.getReturnType().equals(void.class)) {
            throw new IllegalStateException("Schedule method's return type isn't void");
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("Schedule method's visibility isn't public");
        }
    }
}
