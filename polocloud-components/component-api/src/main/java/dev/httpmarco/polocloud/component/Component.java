package dev.httpmarco.polocloud.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Component {

    public abstract void start();

    public abstract void stop();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {

        String name();

        String version();

    }
}
