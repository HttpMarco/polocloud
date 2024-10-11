package dev.httpmarco.polocloud.api;

public interface ClassSupplier {
    Class<?> classByName(String name) throws ClassNotFoundException;
}
