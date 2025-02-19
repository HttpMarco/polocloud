package dev.httpmarco.polocloud.suite.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Component {

    public abstract void enabling();

    public abstract void disabling();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {

        String name();

        String version();

    }
}
