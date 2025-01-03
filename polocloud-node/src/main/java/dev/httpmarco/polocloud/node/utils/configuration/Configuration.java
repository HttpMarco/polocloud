package dev.httpmarco.polocloud.node.utils.configuration;

import com.google.gson.Gson;
import dev.httpmarco.polocloud.common.gson.GsonPool;
import dev.httpmarco.polocloud.node.i18n.serializer.LocalSerializer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

@Accessors(fluent = true)
public class Configuration<T> {

    private static final Gson CONFIGURATION_GSON = GsonPool.newInstance(it -> it.registerTypeAdapter(Locale.class, new LocalSerializer()));
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
        Files.writeString(target, CONFIGURATION_GSON.toJson(value));
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public T read() {
        if (Files.exists(target)) {
            this.value = (T) CONFIGURATION_GSON.fromJson(Files.readString(target), value.getClass());
        } else {
            this.update();
        }
        return value;
    }
}
