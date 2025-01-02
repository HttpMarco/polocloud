package dev.httpmarco.polocloud.common.configuration;

import dev.httpmarco.polocloud.common.gson.GsonPool;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;

@Accessors(fluent = true)
public class Configuration<T> {

    private final Path target;

    @Getter
    private T value;

    public Configuration(String target, T defaultValue) {
        this.target = Path.of(target);
        this.value = defaultValue;

        // read default configuration
        this.read();
    }

    @SneakyThrows
    public void update() {
        Files.writeString(target, GsonPool.PRETTY_GSON.toJson(value));
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public T read() {
        if (Files.exists(target)) {
            this.value = (T) GsonPool.PRETTY_GSON.fromJson(Files.readString(target), value.getClass());
        } else {
            this.update();
        }
        return value;
    }
}
