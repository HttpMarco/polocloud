package dev.httpmarco.pololcoud.common.document;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public abstract class DocumentTypeAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    private Class<T> type;
}
