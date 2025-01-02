package dev.httpmarco.polocloud.common.configuration;

import dev.httpmarco.polocloud.common.gson.GsonPool;
import lombok.SneakyThrows;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration<T> {

    private final Path target;
    public T value;

    public Configuration(String target, T defaultValue) {
        this.target = Path.of(target);
        this.value = defaultValue;
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
