package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public final class ConfigurationIndex {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationIndex.class);
    private final Path placeHolderOrder;

    public ConfigurationIndex(Path placeHolderOrder) {
        this.placeHolderOrder = placeHolderOrder;
    }

    public <T> Builder<T> bind(Class<T> clazz) {
        return new Builder<>(clazz, placeHolderOrder);
    }

    public class Builder<T> {

        private final Class<T> clazz;
        private Function<Void, T> defaultValue;
        private final Path placeHolderOrder;

        public Builder(Class<T> clazz, Path placeHolderOrder) {
            this.clazz = clazz;
            this.placeHolderOrder = placeHolderOrder;
        }

        public Builder<T> defaultValue(Function<Void, T> newInstance) {
            this.defaultValue = newInstance;
            return this;
        }

        public T build() {
            var path = placeHolderOrder.toAbsolutePath();

            if (clazz.isAnnotationPresent(ConfigurationDetails.class)) {
                var details = clazz.getAnnotation(ConfigurationDetails.class);
                path = path.resolve(details.name());
            }

            if (Files.exists(path)) {
                try {
                    return GsonInstance.DEFAULT.fromJson(Files.readString(path), clazz);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    return generateDefaultValue();
                }
            } else {
                var value = generateDefaultValue();
                try {
                    Files.writeString(path, GsonInstance.DEFAULT.toJson(value));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                return value;
            }
        }

        private T generateDefaultValue() {
            if (defaultValue != null) {
                return defaultValue.apply(null);
            }
            try {
                return (T) defaultValue.getClass().getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }
}
