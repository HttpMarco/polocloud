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
                ClusterServiceState.valueOf(properties.get("state").getAsString()),
                context.deserialize(properties.get("group"), ClusterServiceInstance.class),
                properties.get("hostname").getAsString(),
                properties.get("port").getAsInt()
        );
    }

    @Override
    public JsonElement serialize(ClusterService src, Type typeOfSrc, JsonSerializationContext context) {
        //todo
        return null;
    }
}
