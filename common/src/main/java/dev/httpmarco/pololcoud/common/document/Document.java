package dev.httpmarco.pololcoud.common.document;

import com.google.gson.Gson;
import dev.httpmarco.pololcoud.common.GsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public final class Document<V> {

    private final Gson documentGson;
    private final Path path;

    @Setter
    private V value;

    public Document(Path path, V value, DocumentTypeAdapter<?> @NotNull ... typeAdapters) {
        this.path = path;
        this.value = value;

        if (typeAdapters.length == 0) {
            documentGson = GsonUtils.GSON;
        } else {
            var gsonBuilder = GsonUtils.GSON.newBuilder();

            for (var adapter : typeAdapters) {
                gsonBuilder.registerTypeHierarchyAdapter(adapter.type(), adapter);
                gsonBuilder.registerTypeAdapter(adapter.type(), adapter);
            }

            documentGson = gsonBuilder.create();
        }

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
        this.value = (V) documentGson.fromJson(Files.readString(path), value.getClass());
    }

    @SneakyThrows
    public void save() {
        Files.writeString(this.path, documentGson.toJson(value));
    }
}
