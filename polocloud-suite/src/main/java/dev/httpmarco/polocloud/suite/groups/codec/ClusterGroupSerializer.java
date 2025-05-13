package dev.httpmarco.polocloud.suite.groups.codec;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupImpl;

import java.lang.reflect.Type;
import java.util.List;

public final class ClusterGroupSerializer implements JsonSerializer<ClusterGroup>, JsonDeserializer<ClusterGroup> {

    @Override
    public ClusterGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var properties = json.getAsJsonObject();

        var name = properties.get("name").getAsString();

        var platformProperties = properties.get("platform").getAsJsonObject();

        var platform = new SharedPlatform(
                platformProperties.get("name").getAsString(),
                Version.parse(platformProperties.get("version").getAsString()),
                PlatformType.valueOf(platformProperties.get("type").getAsString())
        );
        var minMemory = properties.get("minMemory").getAsInt();
        var maxMemory = properties.get("maxMemory").getAsInt();
        var minOnlineService = properties.get("minOnlineService").getAsInt();
        var maxOnlineService = properties.get("maxOnlineService").getAsInt();
        var percentageToStartNewService = properties.get("percentageToStartNewService").getAsDouble();

        List<String> templates = context.deserialize(properties.get("templates"), List.class);

        return new ClusterGroupImpl(name, platform, minMemory,maxMemory,minOnlineService,maxOnlineService,percentageToStartNewService, templates);
    }

    @Override
    public JsonElement serialize(ClusterGroup src, Type typeOfSrc, JsonSerializationContext context) {
        var properties = new JsonObject();

        properties.addProperty("name", src.name());

        var platform = new JsonObject();
        platform.addProperty("name", src.platform().name());
        platform.addProperty("version", src.platform().version().toString());
        platform.addProperty("type", src.platform().type().name());

        properties.add("platform", platform);


        properties.addProperty("minMemory", src.minMemory());
        properties.addProperty("maxMemory", src.maxMemory());
        properties.addProperty("minOnlineService", src.minOnlineService());
        properties.addProperty("maxOnlineService", src.maxOnlineService());
        properties.addProperty("percentageToStartNewService", src.percentageToStartNewService());

        properties.add("templates", context.serialize(src.templates()));
        return properties;
    }
}
