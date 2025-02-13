package dev.httpmarco.polocloud.suite.dependencies.pool.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.suite.dependencies.external.ExternalDependency;

import java.lang.reflect.Type;

public final class DependencySerializer implements JsonDeserializer<ExternalDependency> {
    @Override
    public ExternalDependency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();

        return new ExternalDependency(object.get("group").getAsString(), object.get("name").getAsString(), object.get("version").getAsString());
    }
}
