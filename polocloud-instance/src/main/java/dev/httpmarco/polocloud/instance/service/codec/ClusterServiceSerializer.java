package dev.httpmarco.polocloud.instance.service.codec;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.instance.service.ClusterServiceInstance;

import java.lang.reflect.Type;
import java.util.UUID;

public final class ClusterServiceSerializer implements JsonSerializer<ClusterService>, JsonDeserializer<ClusterService> {

    @Override
    public ClusterService deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var properties = json.getAsJsonObject();
        return new ClusterServiceInstance(
                properties.get("id").getAsInt(),
                UUID.fromString(properties.get("uniqueId").getAsString()),
                ClusterServiceState.ONLINE,
                null);
    }

    @Override
    public JsonElement serialize(ClusterService src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
