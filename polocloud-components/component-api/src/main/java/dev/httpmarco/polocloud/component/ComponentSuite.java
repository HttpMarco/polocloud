package dev.httpmarco.polocloud.component;

public abstract class ComponentSuite {

    private static ComponentSuite instance;

    public static ComponentSuite instance() {
        return instance;
    }

    public ComponentSuite() {
        instance = this;
    }
}
