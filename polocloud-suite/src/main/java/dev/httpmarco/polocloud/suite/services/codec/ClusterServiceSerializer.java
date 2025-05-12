package dev.httpmarco.polocloud.suite.services.codec;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.services.ClusterService;

import java.lang.reflect.Type;

public final class ClusterServiceSerializer implements JsonSerializer<ClusterService>, JsonDeserializer<ClusterService> {

    @Override
    public ClusterService deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(ClusterService src, Type typeOfSrc, JsonSerializationContext context) {
        var properties = new JsonObject();

        properties.add("id", new JsonPrimitive(src.id()));
        properties.add("uniqueId", new JsonPrimitive(src.uniqueId().toString()));

        return properties;
    }
}
