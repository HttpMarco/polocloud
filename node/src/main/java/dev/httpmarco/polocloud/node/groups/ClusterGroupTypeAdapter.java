package dev.httpmarco.polocloud.node.groups;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.FallbackClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public final class ClusterGroupTypeAdapter implements JsonDeserializer<ClusterGroup>, JsonSerializer<ClusterGroup> {

    @Override
    public ClusterGroup deserialize(JsonElement json, Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        var object = (JsonObject) json;
        var group = (ClusterGroup) context.deserialize(object, ClusterGroupImpl.class);

        if (object.has("fallback") && object.get("fallback").getAsBoolean()) {
            return new ClusterGroupFallbackImpl(group);
        }

        return group;
    }

    @Override
    public @Nullable JsonElement serialize(ClusterGroup src, Type typeOfSrc, @NotNull JsonSerializationContext context) {

        var group = (JsonObject) context.serialize(src);

        if (src.platform().type() == PlatformType.SERVER) {
            group.addProperty("fallback", (src instanceof FallbackClusterGroup));
        }

        return group;
    }
}
