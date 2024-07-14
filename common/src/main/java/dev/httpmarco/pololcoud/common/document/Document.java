package dev.httpmarco.pololcoud.common.document;

import dev.httpmarco.pololcoud.common.GsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public final class Document<V> {

     private final Path path;

    @Setter
    private V value;

    public Document(Path path, V value) {
        this.path = path;
        this.value = value;

        if (Files.exists(path)) {
            this.update();
        } else {
            this.save();
        }
    }

    @SneakyThrows
    public void delete() {
        Files.delete(this.path);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void update() {
        this.value = (V) GsonUtils.GSON.fromJson(Files.readString(path), value.getClass());
    }

    @SneakyThrows
    public void save() {
        Files.writeString(this.path, GsonUtils.GSON.toJson(value));
    }
}
