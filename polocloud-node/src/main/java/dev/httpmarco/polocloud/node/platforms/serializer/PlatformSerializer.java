package dev.httpmarco.polocloud.node.platforms.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class PlatformSerializer implements JsonSerializer<Platform>, JsonDeserializer<Platform> {

    @Override
    public @NotNull Platform deserialize(@NotNull JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var data = json.getAsJsonObject();

        var name = data.get("name").getAsString();
        var icon = data.get("icon").getAsString();
        var type = PlatformType.valueOf(data.get("type").getAsString());
        var url = data.get("url").getAsString();

        var platform = new Platform(name, icon, type, url);

        for (var version : data.getAsJsonArray("versions")) {
            var versionData = version.getAsJsonObject();

            platform.versions().add(new PlatformVersion(platform, Version.parse(versionData.get("version").getAsString())));
        }

        return platform;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull Platform platform, Type typeOfSrc, JsonSerializationContext context) {
        var data = new JsonObject();

        data.addProperty("name", platform.name());
        data.addProperty("icon", platform.icon());
        data.addProperty("type", platform.type().name());
        data.addProperty("url", platform.url());

        var versionCollection = new JsonArray();

        for (var version : platform.versions()) {
            var versionData = new JsonObject();
            versionData.addProperty("version", version.version().originalVersion());
            versionCollection.add(versionData);
        }

        data.add("versions", versionCollection);
        return data;
    }
}
