package dev.httpmarco.polocloud.node.groups;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.lang.reflect.Type;

public final class ClusterGroupTypeAdapter implements JsonDeserializer<ClusterGroup>, JsonSerializer<ClusterGroup> {

    @Override
    public ClusterGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //todo
        return null;
    }

    @Override
    public JsonElement serialize(ClusterGroup src, Type typeOfSrc, JsonSerializationContext context) {
        //todo
        return null;
    }
}
