package dev.httpmarco.polocloud.inject;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class Injector {

    private final Map<Class<?>, Object> injections = new HashMap<>();

    @SneakyThrows
    public void bind(Class<?> clazz) {
        if(injections.containsKey(clazz)) {
            return;
        }
        injections.put(clazz, clazz.getConstructor().newInstance());
    }

    @SneakyThrows
    public <T> T instance(Class<T> clazz) {
        var object = clazz.getConstructor().newInstance();

        for (var field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(object, injectFiled(field));
        }

        return object;
    }

    private Object injectFiled(Field field) {
        return injections.get(field.getType());
    }

    public static Injector newInjector(){
        return new Injector();
    }
}