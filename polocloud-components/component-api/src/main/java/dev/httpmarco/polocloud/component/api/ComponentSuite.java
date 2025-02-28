package dev.httpmarco.polocloud.component.api;

public abstract class ComponentSuite {

    private static ComponentSuite instance;

    public static ComponentSuite instance() {
        return instance;
    }

    public abstract void shutdownSuite();

    public ComponentSuite() {
        instance = this;
    }
}
