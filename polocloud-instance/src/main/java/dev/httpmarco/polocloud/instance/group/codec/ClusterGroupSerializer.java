package dev.httpmarco.polocloud.instance.group.codec;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.instance.group.ClusterInstanceGroup;

import java.lang.reflect.Type;
import java.util.Arrays;

public final class ClusterGroupSerializer implements JsonSerializer<ClusterGroup>, JsonDeserializer<ClusterGroup> {


    @Override
    public ClusterGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var properties = json.getAsJsonObject();

        var name = properties.get("name").getAsString();
        var minMemory = properties.get("minMemory").getAsInt();
        var maxMemory = properties.get("maxMemory").getAsInt();
        var minOnlineServices = properties.get("minOnlineServices").getAsInt();
        var maxOnlineServices = properties.get("maxOnlineServices").getAsInt();
        var percentageToStartNewService = properties.get("percentageToStartNewService").getAsDouble();

        String[] templates = context.deserialize(properties.get("templates"), String[].class);
        SharedPlatform platform = context.deserialize(properties.get("platform"), SharedPlatform.class);

        return new ClusterInstanceGroup(name,
                platform,
                minMemory,
                maxMemory,
                minOnlineServices,
                maxOnlineServices,
                percentageToStartNewService,
                Arrays.stream(templates).toList());
    }

    @Override
    public JsonElement serialize(ClusterGroup src, Type typeOfSrc, JsonSerializationContext context) {
        var properties = new JsonObject();

        properties.addProperty("name", src.name());
        properties.addProperty("minMemory", src.minMemory());
        properties.addProperty("maxMemory", src.maxMemory());
        properties.addProperty("minOnlineServices", src.minOnlineService());
        properties.addProperty("maxOnlineServices", src.maxOnlineService());
        properties.addProperty("percentageToStartNewService", src.percentageToStartNewService());

        properties.add("templates", context.serialize(src.templates()));
        properties.add("platform", context.serialize(src.platform()));

        return properties;
    }
}
