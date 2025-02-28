package dev.httpmarco.polocloud.component.api;

import dev.httpmarco.polocloud.component.api.system.SuiteSystemProvider;

public abstract class ComponentSuite {

    private static ComponentSuite instance;

    public static ComponentSuite instance() {
        return instance;
    }

    public abstract SuiteSystemProvider suiteSystemProvider();

    public ComponentSuite() {
        instance = this;
    }
}
