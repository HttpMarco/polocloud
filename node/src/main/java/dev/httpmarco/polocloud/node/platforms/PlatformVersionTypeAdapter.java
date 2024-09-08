package dev.httpmarco.polocloud.node.platforms;

import com.google.gson.*;
import dev.httpmarco.polocloud.node.platforms.versions.PlatformPathVersion;
import dev.httpmarco.polocloud.node.platforms.versions.PlatformUrlVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public final class PlatformVersionTypeAdapter implements JsonSerializer<PlatformVersion>, JsonDeserializer<PlatformVersion> {

    @Override
    public @Nullable PlatformVersion deserialize(@NotNull JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();
        var version = object.get("version").getAsString();

        if (object.has("fileName")) {
            return new PlatformPathVersion(version, object.get("fileName").getAsString());
        }
        if (object.has("url")) {
            return new PlatformUrlVersion(version, object.get("url").getAsString());
        }
        return null;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull PlatformVersion src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();

        object.addProperty("version", src.version());

        if (src instanceof PlatformPathVersion pathVersion) {
            object.addProperty("fileName", pathVersion.fileName());
        } else if (src instanceof PlatformUrlVersion urlVersion) {
            object.addProperty("url", urlVersion.url());
        }

        return object;
    }
}
