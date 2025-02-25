package dev.httpmarco.polocloud.suite.components;

import dev.httpmarco.polocloud.suite.config.ConfigurationIndex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;

public abstract class Component {

    private final ConfigurationIndex configurationIndex;

    public Component() {
        this.configurationIndex = new ConfigurationIndex(Path.of(getClass().getDeclaredAnnotation(Info.class).name() + "/"));
    }

    public abstract void start();

    public abstract void stop();

    public ConfigurationIndex config() {
        return this.configurationIndex;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {

        String name();

        String version();

    }
}
